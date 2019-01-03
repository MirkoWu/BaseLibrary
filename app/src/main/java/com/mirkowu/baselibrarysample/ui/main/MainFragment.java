package com.mirkowu.baselibrarysample.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.ToolbarFragment;
import com.mirkowu.baselibrarysample.ui.ScreenAdapterActivity;
import com.mirkowu.baselibrarysample.ui.TestToolbarActivity;
import com.mirkowu.baselibrarysample.ui.testMvp.TestMvpActivity;
import com.mirkowu.baselibrarysample.ui.testRefresh.TestRefreshActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.L;

import java.util.Locale;

import butterknife.OnClick;

/**
 * @author by DELL
 * @date on 2018/2/24
 * @describe
 */

public class MainFragment extends ToolbarFragment {

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.addLeftImage(R.mipmap.menu, v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
    }

    @Override
    protected void initEventAndData() {


    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.mBtnChangeDayNightMode, R.id.mBtnChangeLanguage, R.id.mBtnMvpTemp,
            R.id.mBtnRefreshTemp, R.id.mBtnToolbarTemp, R.id.mBtnDataBindingTemp})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.mBtnChangeDayNightMode://切换日夜模式
                BaseSPManager.setNightMode(!BaseSPManager.isNightMode());
                getBaseActivity().reload();
                break;
            case R.id.mBtnChangeLanguage://切换语言
                L.d("语言=" + BaseSPManager.getLanguage().getLanguage() + "  " + BaseSPManager.getLanguage().getCountry());
                Locale locale = isEqualsLanguage(BaseSPManager.getLanguage(), Locale.SIMPLIFIED_CHINESE) ? Locale.ENGLISH : Locale.SIMPLIFIED_CHINESE;
                BaseSPManager.setLanguage(locale);
                getBaseActivity().reload();//要重新启动Activity
                break;
            case R.id.mBtnMvpTemp://MVP模板
                TestMvpActivity.start(getActivity());
                break;
            case R.id.mBtnRefreshTemp://刷新页面模板
                TestRefreshActivity.start(getActivity());
                break;
            case R.id.mBtnToolbarTemp://Toolbar页面模板
                TestToolbarActivity.start(getActivity());
                break;
            case R.id.mBtnDataBindingTemp://Toolbar页面模板
                ScreenAdapterActivity.start(getActivity());
                break;

        }
    }


    @Override
    public void onRefresh() {

    }
}
