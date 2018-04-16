package com.softgarden.baselibrary.base;


/**
 * 引入 IBaseDisplay mView  控制生命周期
 */
public class BasePresenter<T extends IBaseDisplay> implements IBasePresenter<T> {
    protected T mView;


    @Override
    public void attachView(T display) {
        this.mView = display;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
