package com.softgarden.baselibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.DisplayUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 通用的toolbar
 */

public class BaseToolbar extends Toolbar {
    private Context mContext;
    private View mStatusBar;//状态栏
    private View mSplitLine;//状态栏分割线
    private LinearLayout mRootView;//根部局
    private TextView mTitleTextView;//标题
    private LinearLayout mLayoutLeft, mLayoutRight;//

    public BaseToolbar(Context context) {
        this(context, null);
    }

    public BaseToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //toolbar默认marginLeft ，所以定位到(0,0) 防止偏移
        this.setContentInsetsAbsolute(0, 0);
        this.mContext = context;
        View view = inflate(context, R.layout.layout_base_toolbar, this);
        mStatusBar = view.findViewById(R.id.mStatusBar);
        mSplitLine = view.findViewById(R.id.mSplitLine);
        mRootView = (LinearLayout) view.findViewById(R.id.layout_toolbar);
        mTitleTextView = (TextView) view.findViewById(R.id.mTitleTextView);
        mLayoutLeft = (LinearLayout) view.findViewById(R.id.mLayoutLeft);
        mLayoutRight = (LinearLayout) view.findViewById(R.id.mLayoutRight);
    }

    /**
     * 根部局
     *
     * @return
     */
    public ViewGroup getRootView() {
        return mRootView;
    }


    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    public int getStatusBarHeight() {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 显示状态栏 ((此功能需配合沉浸式API>=19才会生效))
     * 默认为透明，保持和toolbar一样的颜色
     */
    public void setStatusBarColor() {
        setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 显示状态栏 (此功能需配合沉浸式API>=19才会生效)
     *
     * @param colorId
     */
    public void setStatusBarColor(@ColorInt int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//android 19 4.4 以上才支持沉浸式
            if (getContext() instanceof Activity) {//设置 沉浸式 flag
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            mStatusBar.setVisibility(VISIBLE);
            mStatusBar.setBackgroundColor(colorId);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
            params.height = getStatusBarHeight();
        }
    }

    /**
     * 隐藏 statusBar
     */
    public void hideStatusBar() {
        mStatusBar.setVisibility(GONE);
    }

    /**
     * toolbar 和布局的分割线
     *
     * @param colorId
     * @param height
     */
    public void showSplitLine(@ColorInt int colorId, int height) {
        mSplitLine.setVisibility(VISIBLE);
        mSplitLine.setBackgroundColor(colorId);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSplitLine.getLayoutParams();
        params.height = height;
    }

    /**
     * 隐藏 statusBar
     */
    public void hideSplitLine() {
        mSplitLine.setVisibility(GONE);
    }


    /**
     * 设置返回按钮,
     * image 将资源id设置 <= 0 的值就可以隐藏返回键
     */
    public void setBackButton(@DrawableRes int resId) {
        if (resId <= 0) {
            return;
        }

        ImageView imageMenu = createImageMenu(mContext, resId, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity)
                    ((Activity) getContext()).onBackPressed();//调用activity的返回键
            }
        });
        int padding = DisplayUtil.dip2px(mContext, 12);
        imageMenu.setPadding(padding / 3, 0, padding, 0);
        addLeftView(imageMenu);
//        addLeftImage(resId,new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getContext() instanceof Activity)
//                    ((Activity) getContext()).onBackPressed();//调用activity的返回键
//            }
//        });
    }


    /**
     * 添加左边View
     *
     * @param view
     */
    public void addLeftView(View view) {
        mLayoutLeft.addView(view);
    }

    public void addLeftView(View view, int index) {
        mLayoutLeft.addView(view, index);
    }

    public void addLeftView(View view, ViewGroup.LayoutParams params) {
        mLayoutLeft.addView(view, params);
    }

    /**
     * 显示左边图形菜单按钮
     */
    public void addLeftImage(@DrawableRes int resId, OnClickListener listener) {
        ImageView imageMenu = createImageMenu(mContext, resId, listener);
        imageMenu.setImageResource(resId);
        imageMenu.setOnClickListener(listener);
        addLeftView(imageMenu);
    }

    /**
     * 显示左边文本菜单按钮
     */
    public void addLeftText(CharSequence text, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, listener);
        addLeftView(textMenu);
    }

    public void addLeftText(CharSequence text, @ColorInt int colorId, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, listener);
        textMenu.setTextColor(colorId);
        addLeftView(textMenu);
    }

    public void addLeftText(@StringRes int resId, OnClickListener listener) {
        addLeftText(getContext().getText(resId), listener);
    }

    public void addLeftText(@StringRes int resId, @ColorInt int colorId, OnClickListener listener) {
        addLeftText(getContext().getText(resId), colorId, listener);
    }


    /**
     * 添加右边View
     *
     * @param view
     */
    public void addRightView(View view) {
        mLayoutRight.addView(view);
    }

    public void addRightView(View view, int index) {
        mLayoutRight.addView(view, index);
    }

    public void addRightView(View view, ViewGroup.LayoutParams params) {
        mLayoutRight.addView(view, params);
    }


    /**
     * 显示右边文本菜单按钮
     *
     * @param text
     * @param listener
     */
    public void addRightText(CharSequence text, @ColorInt int colorId, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, listener);
        textMenu.setTextColor(colorId);
        addRightView(textMenu);
    }

    public void addRightText(CharSequence text, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, listener);
        addRightView(textMenu);
    }

    public void addRightText(@StringRes int text, OnClickListener listener) {
        addRightText(getContext().getText(text), listener);
    }

    public void addRightText(@StringRes int text, @ColorInt int colorId, OnClickListener listener) {
        addRightText(getContext().getText(text), colorId, listener);
    }


    /**
     * 显示右边图形菜单按钮
     */
    public void addRightImage(@DrawableRes int resId, OnClickListener listener) {
        ImageView imageMenu = createImageMenu(mContext, resId, listener);
        imageMenu.setImageResource(resId);
        imageMenu.setOnClickListener(listener);
        mLayoutRight.addView(imageMenu);
        addRightView(imageMenu);
    }


    /**
     * 设置 titleText
     *
     * @param titleText
     */
    @Override
    public void setTitle(CharSequence titleText) {
        mTitleTextView.setText(titleText);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    /**
     * 获取title
     *
     * @return
     */
    @Override
    public CharSequence getTitle() {
        return mTitleTextView.getText();
    }

    /**
     * 设置标题 颜色
     *
     * @param colorId
     */
    @Override
    public void setTitleTextColor(@ColorInt int colorId) {
        mTitleTextView.setTextColor(colorId);
    }


    /**
     * 设置整个toolbar背景颜色
     *
     * @param colorId
     */
    @Override
    public void setBackgroundColor(@ColorInt int colorId) {
        mRootView.setBackgroundColor(colorId);
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        mRootView.setBackgroundResource(resId);
    }


    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public View getLeftView(int index) {
        return mLayoutLeft.getChildAt(index);
    }

    public View getRightView(int index) {
        return mLayoutRight.getChildAt(index);
    }

    /**
     * builder 模式
     */
    public static class Builder {
        private Context mContext;
        private CharSequence titleText;
        private int titleTextResId;
        private int backResId;
        private int statusBarColorId = Color.TRANSPARENT,
                backgroundColorId = Color.BLUE,
                titleColorId = Color.BLACK;//均设置默认值
        private ArrayList<View> leftViewList, rightViewList;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setBackButton(@DrawableRes int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder addLeftImage(@DrawableRes int imgResId, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createImageMenu(mContext, imgResId, listener));
            return this;
        }

        public Builder addRightImage(@DrawableRes int imgResId, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createImageMenu(mContext, imgResId, listener));
            return this;
        }

        public Builder addLeftText(CharSequence text, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createTextMenu(mContext, text, listener));
            return this;
        }

        public Builder addRightText(CharSequence text, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createTextMenu(mContext, text, listener));
            return this;
        }

        public Builder addLeftText(@StringRes int textResId, OnClickListener listener) {
            addLeftText(mContext.getString(textResId), listener);
            return this;
        }

        public Builder addRightText(@StringRes int textResId, OnClickListener listener) {
            addRightText(mContext.getString(textResId), listener);
            return this;
        }

        public Builder addLeftView(View view) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(view);
            return this;
        }

        public Builder addRightView(View view) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(view);
            return this;
        }

        public Builder setTitle(CharSequence titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder setTitle(@StringRes int titleTextResId) {
            this.titleTextResId = titleTextResId;
            return this;
        }

        /**
         * setTitleTextColor  和 setAllTextColor 顺序 按最新设置的那个算
         *
         * @param titleColorId
         * @return
         */
        public Builder setTitleTextColor(@ColorInt int titleColorId) {
            this.titleColorId = titleColorId;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int backgroundColorId) {
            this.backgroundColorId = backgroundColorId;
            return this;
        }

        public Builder setStatusBarColor(@ColorInt int statusBarColorId) {
            this.statusBarColorId = statusBarColorId;
            return this;
        }

        public BaseToolbar build() {
            BaseToolbar toolbar = new BaseToolbar(mContext);

            /*** 默认隐藏 也可以将backResId 设置一个默认值*/
            toolbar.setBackButton(backResId);

            /*** titleText */
            if (TextUtils.isEmpty(titleText)) {
                if (titleTextResId > 0) toolbar.setTitle(titleTextResId);
                else toolbar.setTitle(null);
            } else toolbar.setTitle(titleText);

            /*** leftMenu */
            if (leftViewList != null && !leftViewList.isEmpty()) {
                for (View view : leftViewList) {
                    toolbar.addLeftView(view);
                }
            }

            /*** rightMenu */
            if (rightViewList != null && !rightViewList.isEmpty()) {
                for (View view : rightViewList) {
                    toolbar.addRightView(view);
                }
            }

            toolbar.setStatusBarColor(statusBarColorId);

            toolbar.setBackgroundColor(backgroundColorId);

            toolbar.setTitleTextColor(titleColorId);

            return toolbar;
        }
    }

    public static TextView createTextMenu(Context context, CharSequence text, OnClickListener listener) {
        TextView textMenu = new TextView(context);
        textMenu.setTextSize(16);
        textMenu.setGravity(Gravity.CENTER);
        int padding = DisplayUtil.dip2px(context, 5);
        textMenu.setPadding(padding, 0, padding, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        textMenu.setLayoutParams(params);
        textMenu.setText(text);
        textMenu.setOnClickListener(listener);
        return textMenu;
    }

    public static ImageView createImageMenu(Context context, @DrawableRes int imageResId, OnClickListener listener) {
        ImageView imageMenu = new ImageView(context);
        int padding = DisplayUtil.dip2px(context, 5);
        imageMenu.setPadding(padding, 0, padding, 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        imageMenu.setScaleType(ImageView.ScaleType.CENTER);
        imageMenu.setLayoutParams(params);
        imageMenu.setImageResource(imageResId);
        imageMenu.setOnClickListener(listener);
        return imageMenu;
    }
}
