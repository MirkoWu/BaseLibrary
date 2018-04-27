package com.mirkowu.baselibrary.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.bean.TabEntityBean;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.mirkowu.statusbarutil.StatusBarUtil;
import com.softgarden.baselibrary.base.FragmentBasePagerAdapter;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.softgarden.baselibrary.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends ToolbarActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @BindView(R.id.mTabLayout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return null;
    }

    @Override
    protected void initialize() {
        StatusBarUtil.setTransparentForDrawerLayout(this, mDrawerLayout);

        FragmentBasePagerAdapter adapter = new FragmentBasePagerAdapter(getSupportFragmentManager(),
                MainFragment.newInstance(), MainFragment.newInstance(),
                MainFragment.newInstance(), MainFragment.newInstance());
        mViewPager.setAdapter(adapter);

        String[] mTitles = {getString(R.string.me), getString(R.string.me), getString(R.string.me), getString(R.string.me),};
        int[] mIconUnSelectIds = {
                R.mipmap.me_unselect, R.mipmap.me_unselect,
                R.mipmap.me_unselect, R.mipmap.me_unselect};
        int[] mIconSelectIds = {
                R.mipmap.me_select, R.mipmap.me_select,
                R.mipmap.me_select, R.mipmap.me_select};
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntityBean(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        mTabLayout.setTabData(mTabEntities);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        mTabLayout.showDot(2);
        mTabLayout.showMsg(3, 10);
        mTabLayout.setMsgMargin(3, -3, 5);

    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }




    /**
     * 再按一次退出程序
     */
    private long currentBackPressedTime = 0;
    private static int BACK_PRESSED_INTERVAL = 5000;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                ToastUtil.s("再按一次，退出应用！");
                return true;
            } else {
                finish(); // 退出
            }
            return false;

        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


}
