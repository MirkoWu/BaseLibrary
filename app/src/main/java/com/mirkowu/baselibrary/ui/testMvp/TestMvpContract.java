package com.mirkowu.baselibrary.ui.testMvp;

import com.softgarden.baselibrary.base.IBaseDisplay;
import com.softgarden.baselibrary.base.IBasePresenter;

/**
 * @author by DELL
 * @date on 2018/4/12
 * @describe
 */
public class TestMvpContract {
    //这里可以继承IBaseRefreshDispaly
    interface Display extends IBaseDisplay{
        void getIndexData(String bean);

        void switchOnOff(String bean);

        void switchBluetooth(String bean);
    }

    interface Presenter extends IBasePresenter<Display> {
        void getIndexData();

        void switchOnOff();

        void switchBluetooth();

    }
}
