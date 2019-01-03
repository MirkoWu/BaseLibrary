package com.mirkowu.baselibrarysample.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author by DELL
 * @date on 2018/4/17
 * @describe
 */
public class UserBean implements Parcelable , Serializable {
    private String user_id;
    private String name;
    private int age;
    private boolean isCheck;

    public UserBean(String user_id, String name, int age, boolean isCheck) {
        this.user_id = user_id;
        this.name = name;
        this.age = age;
        this.isCheck = isCheck;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    protected UserBean(Parcel in) {
        this.user_id = in.readString();
        this.name = in.readString();
        this.age = in.readInt();
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserBean{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isCheck=" + isCheck +
                '}';
    }
}
