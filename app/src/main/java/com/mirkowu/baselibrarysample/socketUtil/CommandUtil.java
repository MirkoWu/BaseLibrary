package com.mirkowu.baselibrarysample.socketUtil;

import android.util.Log;

import com.softgarden.baselibrary.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Start + DataLen + Command + HostID + DATA[0…N] + BCC + Stop
 * Start   为 2 字节的起始码 0x5A5A
 * Command 为 2 字节的命令
 * HostID  为 2 字节的主机设备 ID 号(非房号)，即目的地址 DA
 * DataLen  为 2 字节的数据长度（整个一帧的长度），做到可变。
 * Reseve[2] 为保留 2 字节，如果没用到则用 0x00 表示。
 * DATA[0…N] 为有效数据 0…N
 * XOR  不包括起始码、结束码和本身 BCC 的所有字节的异或(XOR)校验.
 * Stop   结束码
 * -----
 * XOR = (Command^HostID^ DataLen^Reseve^DATA[0]^DATA[1]^...^DATA[N])
 * ----
 * Created by Administrator on 2016/10/25 0025.
 */

public class CommandUtil {
    public static final byte[] start_code = new byte[]{(byte) 0x5A, (byte) 0x5A,};//起始码
    public static final byte[] byte_reseve = new byte[]{(byte) 0x00, (byte) 0x00};//保留字节
    public static final byte[] end_code = new byte[]{(byte) 0x0D};//结束码
    //public static byte[] byte_hostId = new byte[]{(byte) 0x00, (byte) 0x00};

    public static byte[] byte_command = new byte[]{(byte) 0x00, (byte) 0xF4};


    /**
     * 直接发送 指令和数据
     * 要将命令整理好
     *
     * @param command  命令
     * @param sendData 要发送的数据 长度可变
     * @return
     */
    public static byte[] makeData(byte[] command, byte[] sendData) {
        //TODO hostId  可为已知，直接从保存下来的数据里获取
        int hostId = 0;
        // byte[] hostId = new byte[]{(byte) 0x00, (byte) 0x04};
        return makeData(command, ToByte(hostId), null, sendData);
    }

    public static byte[] makeData_old(byte[] command, byte[] sendData) {
        //TODO hostId  可为已知，直接从保存下来的数据里获取
        // byte[] hostId = new byte[]{(byte) 0x00, (byte) 0x04};
        int hostId = 0;
        return makeData_old(command, ToByte(hostId), null, sendData);
    }

    /**
     * @param command  命令
     * @param hostId   的主机设备 ID 号(非房号)，即目的地址 DA
     * @param reseve   保留字节2个
     * @param sendData 要发送的数据 长度可变
     * @return
     */
    public static byte[] makeData(byte[] command, byte[] hostId, byte[] reseve, byte[] sendData) {
        // Start+ Command + HostID + + DataLen  resve+ DATA[0…N] + BCC + Stop
        // 2 + 2 + 2 + 2 +2 DATA[0…N] + 1 + 1  除了数据 确定已知的已有12位
        int length = 12;

        if (sendData != null) length += sendData.length;//数据内容的长度
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //开始码
            outputStream.write(start_code);
            //命令
            outputStream.write(command);
            //主机id地址
            outputStream.write(hostId);
            //长度
            outputStream.write(ToByte(length));
            //预留2字节 默认为0x0000
            if (reseve == null) {
                reseve = byte_reseve;
            }
            outputStream.write(reseve);
            //发送的数据 长度可变
            outputStream.write(sendData);
            //XOR校验
            byte[] data = outputStream.toByteArray();//要去除起始码
            outputStream.write(checkXOR(Arrays.copyOfRange(data, 2, data.length)));
            //结束码
            outputStream.write(end_code);

            outputStream.flush();
            // Log.d("sendCommand", "Data: " + StringUtil.byte2Hexstr(outputStream.toByteArray()));
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 旧的通信协议 不用
     *
     * @param reseve
     * @param sendData
     * @return
     */
    public static byte[] makeData_old(byte[] command, byte[] hostId, byte[] reseve, byte[] sendData) {
        // Start + DataLen + Command + HostID + resve+ DATA[0…N] + BCC + Stop
        // 2 + 2 + 2 + 2 +2 DATA[0…N] + 1 + 1  除了数据确定已知为12位
        int length = 12;

        if (sendData != null) length += sendData.length;//数据内容的长度
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            //开始码
            outputStream.write(start_code);
            // outputStream.write(start_code[1]);
            //长度
            outputStream.write(ToByte(length));
            //命令
            outputStream.write(command);
            //outputStream.write(byte_command);
            //主机id地址
            outputStream.write(hostId);
            //outputStream.write(new byte[]{(byte) 0x00, (byte) 0x04});

            //预留2字节 0x0000
            if (reseve == null) {
                reseve = byte_reseve;
            }
            outputStream.write(reseve);
            //发送的数据 长度可变
            outputStream.write(sendData);
            //XOR校验
            byte[] data = outputStream.toByteArray();
            outputStream.write(checkXOR(Arrays.copyOfRange(data, 2, data.length)));
            //结束码
            outputStream.write(end_code);

            outputStream.flush();
            Log.d("sendCommand", "Data: " + StringUtil.byte2Hexstr(outputStream.toByteArray()));
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 异或 XOR 运算
     *
     * @param c
     * @return
     */
    public static char checkXOR(byte[] c) {
        char x = 0x0;
        for (int i = 0; i < c.length; i++)
            x ^= c[i];
        return x;
    }

    /**
     * char转化为byte
     * 此处设定为2个字节
     *
     * @param data
     * @return
     */
    public static byte[] ToByte(int data) {
        byte[] b = new byte[2];
        b[0] = (byte) ((data & 0xFF00) >> 8);
        b[1] = (byte) (data & 0xFF);
        return b;
    }

    /**
     * 将整数形转成byte数组
     *
     * @param data       欲处理的数据
     * @param isInverted 是否颠倒
     * @return
     */
    public static byte[] toByte(int data, boolean isInverted) {
        byte[] temp = new byte[4];//改变长度即可
        for (int i = 0; i < temp.length; i++) {
            int index = 8 * (isInverted ? i : temp.length - i - 1);
            temp[i] = (byte) (data >>> index & 0xFF);
        }
        return temp;
    }


}
