package com.mirkowu.baselibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.bean.GoodsBean;
import com.mirkowu.baselibrary.bean.UserBean;
import com.mirkowu.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrary.network.RetrofitClient;
import com.mirkowu.baselibrary.network.RxCallback;
import com.mirkowu.baselibrary.ui.testMvp.TestMvpActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.base.FragmentBasePagerAdapter;
import com.softgarden.baselibrary.dialog.PromptDialog;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.DisplayUtil;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.SPUtil;
import com.softgarden.baselibrary.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class TestToolbarActivity extends ToolbarActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, TestToolbarActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder
                // .setStatusBarColor(Color.GRAY)
                .addLeftText("语言切换", v -> {
                    changeLanguage(!BaseSPManager.isEnglish());
                    reload();
                })
                .addRightText("日夜切换", v -> {
                    changeDayNightMode(!BaseSPManager.isNightMode());
                    reload();
                })
                .addRightImage(R.mipmap.more, v -> startActivity(TestMvpActivity.class))
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
        loadData();


        UserBean userBean = new UserBean("1", "testName", 18, true);
        String key = "UseBean";
        SPUtil.putSerializableObject(key, userBean);
        UserBean userBean1 = SPUtil.getSerializableObject(key);
        L.d(userBean1.getName());
        L.d(userBean1.isCheck() + "");
        L.d(userBean1.toString());


    //    showPromptDialog();

    }

    private void showPromptDialog() {
        new PromptDialog(this)
                .setTitle("标题")
                .setContent("内容")
                .setPositiveButton("确定")
                .setNegativeButton("取消")
                .setOnButtonClickListener(new PromptDialog.OnButtonClickListener() {
                    @Override
                    public void onButtonClick(PromptDialog dialog, boolean isPositiveClick) {
                        ToastUtil.s("dianji");
                    }
                }).show();
    }

    private void loadData() {
        RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this))
                .subscribe(new RxCallback<List<GoodsBean>>() {
                    @Override
                    public void onSuccess(@Nullable List<GoodsBean> data) {

                    }
                });
    }


}
