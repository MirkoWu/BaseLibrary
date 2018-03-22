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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.DisplayUtil;
import com.softgarden.baselibrary.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * 通用的toolbar
 */

public class BaseToolbar extends Toolbar {
    private final String TAG = BaseToolbar.class.getSimpleName();
    private Context mContext;
    private View mStatusBar;//状态栏
    private ImageView mIvBack;//返回按钮
    private View mBottomDivider;//状态栏底部分割线
    private LinearLayout mRootView;//根部局
    private TextView mTitleTextView;//标题
    private FrameLayout mLayoutCenter;//中心布局
    private LinearLayout mLayoutLeft, mLayoutRight;//左右布局
    private int mSubTextColorId = Color.BLACK;//副标题文本颜色


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
        mBottomDivider = view.findViewById(R.id.mBottomDivider);
        mRootView = (LinearLayout) view.findViewById(R.id.layout_toolbar);
        mTitleTextView = (TextView) view.findViewById(R.id.mTitleTextView);
        mLayoutLeft = (LinearLayout) view.findViewById(R.id.mLayoutLeft);
        mLayoutRight = (LinearLayout) view.findViewById(R.id.mLayoutRight);
        mLayoutCenter = (FrameLayout) view.findViewById(R.id.mLayoutCenter);
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
        int result = 0; //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 显示状态栏 ((此功能需配合沉浸式API>=19才会生效))
     * 默认为透明，保持和toolbar一样的颜色
     */
    public void setStatusBarColor() {
        setStatusBarColor(Color.TRANSPARENT);
    }


    /**
     * 显示状态栏颜色 (此功能需配合沉浸式API>=19才会生效)
     *
     * @param colorId 状态栏颜色
     */
    public void setStatusBarColor(@ColorInt int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//android 19 4.4 以上才支持沉浸式
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
     * toolbar 和布局的分割线 (默认不显示)
     *
     * @param colorId
     * @param height
     */
    public void setBottomDivider(@ColorInt int colorId, int height) {
        mBottomDivider.setVisibility(VISIBLE);
        mBottomDivider.setBackgroundColor(colorId);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBottomDivider.getLayoutParams();
        params.height = height;
    }

    /**
     * 隐藏 statusBar
     */
    public void hideBottomDivider() {
        mBottomDivider.setVisibility(GONE);
    }


    /**
     * 设置返回按钮
     */
    public void setBackButton(@DrawableRes int resId) {
        if (resId <= 0) {
            return;
        }

        mIvBack = createImageMenu(mContext, resId, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity)
                    ((Activity) getContext()).onBackPressed();//调用activity的返回键
            }
        });
        int padding = DisplayUtil.dip2px(mContext, 12);
        mIvBack.setPadding(padding / 3, 0, padding, 0);
        addLeftView(mIvBack);
    }

    /**
     * 隐藏返回按钮
     */
    public void hideBackButton() {
        if (mIvBack != null) {
            mIvBack.setVisibility(GONE);
        }
    }


    /**
     * 设置副标题文本颜色 要在添加前调用，否则不起作用
     *
     * @param subTextColorId
     */
    public void setSubTextColor(@ColorInt int subTextColorId) {
        this.mSubTextColorId = subTextColorId;
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

    public void addLeftView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutLeft.addView(view, index, params);
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
        TextView textMenu = createTextMenu(mContext, text, mSubTextColorId, listener);
        addLeftView(textMenu);
    }

    public void addLeftText(CharSequence text, @ColorInt int colorId, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, colorId, listener);
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

    public void addRightView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutRight.addView(view, index, params);
    }


    /**
     * 显示右边文本菜单按钮
     *
     * @param text
     * @param listener
     */
    public void addRightText(CharSequence text, @ColorInt int colorId, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, colorId, listener);
        addRightView(textMenu);
    }

    public void addRightText(CharSequence text, OnClickListener listener) {
        TextView textMenu = createTextMenu(mContext, text, mSubTextColorId, listener);
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
     * 添加中间View
     *
     * @param view
     */
    public void addCenterView(View view) {
        mLayoutCenter.addView(view);
    }

    public void addCenterView(View view, int index) {
        mLayoutCenter.addView(view, index);

    }

    public void addCenterView(View view, ViewGroup.LayoutParams params) {
        mLayoutCenter.addView(view, params);
    }

    public void addCenterView(View view, int index, ViewGroup.LayoutParams params) {
        mLayoutCenter.addView(view, index, params);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        keepTitleViewCenterForParent();
    }

    /**
     * 保持TitleView和左右距离相等，文字居中
     */
    private void keepTitleViewCenterForParent() {
        int margin = Math.max(mLayoutLeft.getWidth(), mLayoutRight.getWidth());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTitleTextView.getLayoutParams();
        if (margin > ScreenUtil.getScreenWidth(getContext()) / 2) return;//超出屏幕一半，Title已经没有显示的地方了
        if (margin == params.leftMargin && margin == params.rightMargin) return;//相等就不再设置，避免死循环
        params.leftMargin = margin;
        params.rightMargin = margin;
        mTitleTextView.setLayoutParams(params);
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

    public LinearLayout getLeftLayout() {
        return mLayoutLeft;
    }

    public LinearLayout getRightLayout() {
        return mLayoutRight;
    }

    public FrameLayout getCenterLayout() {
        return mLayoutCenter;
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
                bottomDividerColorId = Color.BLACK,
                backgroundColorId = Color.BLUE,
                titleColorId = Color.BLACK,//均设置默认值
                subTextColorId = Color.BLACK;//均设置默认值
        private int bottomDividerHeight = 0;
        private ArrayList<View> leftViewList, rightViewList;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setBackButton(@DrawableRes int backResId) {
            this.backResId = backResId;
            return this;
        }

        public Builder setBottomDivider(@ColorInt int bottomDividerColorId, int bottomDividerHeight) {
            this.bottomDividerColorId = bottomDividerColorId;
            this.bottomDividerHeight = bottomDividerHeight;
            return this;
        }

        /**
         * leftImage
         *
         * @param imgResId
         * @param listener
         * @return
         */
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

        /**
         * leftText
         *
         * @param text
         * @param listener
         * @return
         */
        public Builder addLeftText(CharSequence text, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createTextMenu(mContext, text, subTextColorId, listener));
            return this;
        }

        public Builder addLeftText(CharSequence text, @ColorInt int textColorId, OnClickListener listener) {
            if (leftViewList == null) leftViewList = new ArrayList<>();
            leftViewList.add(createTextMenu(mContext, text, textColorId, listener));
            return this;
        }


        public Builder addLeftText(@StringRes int textResId, OnClickListener listener) {
            addLeftText(mContext.getString(textResId), subTextColorId, listener);
            return this;
        }

        public Builder addLeftText(@StringRes int textResId, @ColorInt int textColorId, OnClickListener listener) {
            addLeftText(mContext.getString(textResId), textColorId, listener);
            return this;
        }


        /**
         * rightText
         *
         * @param text
         * @param listener
         * @return
         */
        public Builder addRightText(CharSequence text, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createTextMenu(mContext, text, subTextColorId, listener));
            return this;
        }

        public Builder addRightText(CharSequence text, @ColorInt int textColorId, OnClickListener listener) {
            if (rightViewList == null) rightViewList = new ArrayList<>();
            rightViewList.add(createTextMenu(mContext, text, textColorId, listener));
            return this;
        }


        public Builder addRightText(@StringRes int textResId, OnClickListener listener) {
            addRightText(mContext.getString(textResId), listener);
            return this;
        }

        public Builder addRightText(@StringRes int textResId, @ColorInt int textColorId, OnClickListener listener) {
            addRightText(mContext.getString(textResId), textColorId, listener);
            return this;
        }

        /**
         * addView
         *
         * @param view
         * @return
         */
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

        /**
         * 设置副标题文本颜色
         * 要在 添加副标题前调用，否则不起作用
         *
         * @param subTextColorId
         * @return
         */
        public Builder setSubTextColor(@ColorInt int subTextColorId) {
            this.subTextColorId = subTextColorId;
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

            if (bottomDividerHeight > 0)
                toolbar.setBottomDivider(bottomDividerColorId, bottomDividerHeight);

            toolbar.setStatusBarColor(statusBarColorId);

            toolbar.setBackgroundColor(backgroundColorId);

            toolbar.setTitleTextColor(titleColorId);

            toolbar.setSubTextColor(subTextColorId);

            return toolbar;
        }
    }


    public static TextView createTextMenu(Context context, CharSequence text, @ColorInt int textColorId, OnClickListener listener) {
        return createTextMenu(context, text, textColorId, 16, listener);
    }

    public static TextView createTextMenu(Context context, CharSequence text, @ColorInt int textColorId, float textSize_SP, OnClickListener listener) {
        TextView textMenu = new TextView(context);
        textMenu.setTextColor(textColorId);
        textMenu.setTextSize(textSize_SP);
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
