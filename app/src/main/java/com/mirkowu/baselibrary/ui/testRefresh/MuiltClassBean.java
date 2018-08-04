package com.mirkowu.baselibrary.ui.testRefresh;

/**
 * @author by DELL
 * @date on 2018/7/31
 * @describe
 */
public class MuiltClassBean implements MultiBean {

    private int type;

    public MuiltClassBean(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }


}
