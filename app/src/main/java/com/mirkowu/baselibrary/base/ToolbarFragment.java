package com.mirkowu.baselibrary.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mirkowu.baselibrary.R;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.ContextUtil;

import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 */

public abstract class ToolbarFragment extends RefreshFragment {
    private BaseToolbar mBaseToolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        mView = setSupportToolbar(mView);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private View setSupportToolbar(View mView) {
        /*** 这里可以对Toolbar进行统一的预设置 */
        BaseToolbar.Builder builder = new BaseToolbar.Builder(getContext())
                //  .setBackButton(R.mipmap.back)//统一设置返回键
               // .setStatusBarColor(Color.TRANSPARENT)//统一设置颜色
                .setBackgroundColor(ContextUtil.getColor(R.color.colorPrimary))
                .setSubTextColor(Color.WHITE)
                .setTitleTextColor(Color.WHITE);

        builder = setToolbar(builder);
        if (builder != null) {
            mBaseToolbar = builder.build();
        }


        if (mBaseToolbar != null) {
            //添加Toolbar
            LinearLayout layout = new LinearLayout(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            layout.setLayoutParams(params);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(mBaseToolbar);
            layout.addView(mView);
            return layout;
        }

        return mView;
    }


    public BaseToolbar getToolbar() {
        return mBaseToolbar;
    }


    public void showToolbar() {
        if (mBaseToolbar != null) mBaseToolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        if (mBaseToolbar != null) mBaseToolbar.setVisibility(View.GONE);
    }


    /**
     * 不需要toolbar的 可以不用管
     *
     * @return
     */
    @Nullable
    protected abstract BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder);
}
