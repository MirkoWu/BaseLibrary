package com.mirkowu.baselibrary.ui.testRefresh;

import com.mirkowu.baselibrary.base.IBaseRefreshDisplay;
import com.mirkowu.baselibrary.bean.GoodsBean;
import com.softgarden.baselibrary.base.IBaseDisplay;
import com.softgarden.baselibrary.base.IBasePresenter;

import java.util.List;

/**
 * @author by DELL
 * @date on 2018/4/12
 * @describe
 */
public class TestRefreshContract {

    //这里可以继承IBaseRefreshDispaly
    interface Display extends IBaseDisplay, IBaseRefreshDisplay {
        void getData(List<GoodsBean> bean);
    }

    interface Presenter extends IBasePresenter<Display> {
        void getData();


    }
}
