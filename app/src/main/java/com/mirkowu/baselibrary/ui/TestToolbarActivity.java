package com.mirkowu.baselibrary.ui;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.network.BaseBean;
import com.mirkowu.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrary.network.RetrofitClient;
import com.mirkowu.baselibrary.network.RxCallback;
import com.mirkowu.baselibrary.ui.testMvp.TestMvpActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.base.FragmentBasePagerAdapter;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.DisplayUtil;
import com.softgarden.baselibrary.utils.L;

import butterknife.BindView;

public class TestToolbarActivity extends ToolbarActivity {

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder
                // .setStatusBarColor(Color.GRAY)
                .addLeftText("语言切换", v ->{
                    changeLanguage(!BaseSPManager.isEnglish());
                    reload();
                })
                .addRightText("日夜切换", v -> {
                    changeDayNightMode(!BaseSPManager.isNightMode());
                    reload();
                })
                .addRightImage(R.mipmap.share, v -> startActivity(TestMvpActivity.class))
                .setBottomDivider(Color.parseColor("#E4DFE1"), DisplayUtil.dip2px(this, 3))
                .setTitleTextColor(Color.WHITE)
                .setTitle(R.string.app_name);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_toolbar;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        L.d("onConfigurationChanged");
    }

    @Override
    protected void initialize() {
        // getToolbar().getRightView(0).setBackgroundColor(Color.GRAY);


        String[] title = new String[]{"标题1", "标题2", "标题3", "标题4"};
        FragmentBasePagerAdapter adapter = new FragmentBasePagerAdapter(getSupportFragmentManager(), TestFragment.class, title);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mTabLayout.setupWithViewPager(mViewPager);
//        loadData();

    }

    private void loadData() {
        RetrofitClient.getLoginService()
                .loginThridParty(1, "", "", "")
                .compose(new NetworkTransformer(this))
                .subscribe(new RxCallback<BaseBean<String>>() {
                    @Override
                    public void onSuccess(@Nullable BaseBean<String> data) {

                    }
                });
    }

    private void loadData2() {
        RetrofitClient.getLoginService()
                .loginPhone(1, "", "")
                .compose(new NetworkTransformer(this))
                .subscribe(new RxCallback<BaseBean<String>>() {
                    @Override
                    public void onSuccess(@Nullable BaseBean<String> data) {

                    }
                });
    }


}
