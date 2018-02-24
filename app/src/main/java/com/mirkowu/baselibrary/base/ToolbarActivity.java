package com.mirkowu.baselibrary.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.mirkowu.baselibrary.R;
import com.softgarden.baselibrary.base.BaseActivity;
import com.softgarden.baselibrary.utils.ContextUtil;
import com.softgarden.baselibrary.widget.BaseToolbar;

import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class ToolbarActivity extends BaseActivity {
    private BaseToolbar mBaseToolbar;


    @Override
    protected void initContentView() {
        /*** 这里可以对Toolbar进行统一的预设置 */
        BaseToolbar.Builder builder
                = new BaseToolbar.Builder(getContext())
                // .setStatusBarColor(Color.TRANSPARENT)//统一设置颜色
                .setBackButton(R.mipmap.back)//统一设置返回键
                .setBackgroundColor(ContextUtil.getColor(R.color.colorPrimary))
                //  .setTitleTextColor(ContextCompat.getColor(this, R.color.white))
                ;

        builder = setToolbar(builder);
        if (builder != null) {
            mBaseToolbar = builder.build();
        }
        if (mBaseToolbar != null) {
            //添加Toolbar
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            layout.setLayoutParams(params);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(mBaseToolbar);
            View mView = getLayoutInflater().inflate(getLayoutId(), layout, false);
            layout.addView(mView);
            setContentView(layout);
            //将toolbar设置为actionbar
            setSupportActionBar(mBaseToolbar);
        } else {
            setContentView(getLayoutId());
        }
        //ButterKnife
        unbinder = ButterKnife.bind(this);
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
