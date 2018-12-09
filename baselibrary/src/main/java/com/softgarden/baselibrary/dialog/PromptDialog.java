package com.softgarden.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.ContextUtil;
import com.softgarden.baselibrary.utils.ScreenUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 提示窗  继承自Dialog
 * @author by DELL
 * @date on 2018/1/5
 * @describe
 */

public class PromptDialog extends Dialog implements View.OnClickListener {

    ImageView ivIcon;
    TextView tvTitle;
    TextView tvContent;
    TextView tvPositive;
    TextView tvNegative;

    private int icon;
    private String title;
    private String content;
    private String positiveLabel;
    private String negativeLabel;
    private int positiveTextColor;
    private int negativeTextColor;
    private OnButtonClickListener listener;

    public <T extends View> T $(@IdRes int id) {
        return findViewById(id);
    }

    public PromptDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    public PromptDialog(Context context, int themeId) {
        super(context, themeId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);

        getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.75);//设定宽度为屏幕宽度的0.75
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        initView();
    }


    private void initView() {
        ivIcon = $(R.id.ivIcon);
        tvTitle = $(R.id.tvTitle);
        tvContent = $(R.id.tvContent);
        tvPositive = $(R.id.tvPositive);
        tvNegative = $(R.id.tvNegative);
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);

        ivIcon.setImageResource(icon);
        tvTitle.setText(title);
        tvContent.setText(content);
        ivIcon.setVisibility(icon > 0 ? VISIBLE : GONE);
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        tvContent.setVisibility(TextUtils.isEmpty(content) ? GONE : VISIBLE);

        tvPositive.setText(positiveLabel);
        tvNegative.setText(negativeLabel);
        tvPositive.setVisibility(TextUtils.isEmpty(positiveLabel) ? GONE : VISIBLE);
        tvNegative.setVisibility(TextUtils.isEmpty(negativeLabel) ? GONE : VISIBLE);

        if (positiveTextColor != 0)
            tvPositive.setTextColor(ContextUtil.getColor(positiveTextColor));
        if (negativeTextColor != 0)
            tvNegative.setTextColor(ContextUtil.getColor(negativeTextColor));
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int i = v.getId();
            if (i == R.id.tvPositive) {
                listener.onButtonClick(this, true);
            } else if (i == R.id.tvNegative) {
                listener.onButtonClick(this, false);
            }
        }
        dismiss();
    }

    public PromptDialog setIcon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    public PromptDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 注意：该方法已弃用
     * 因为返回值类型不同 未能重写该方法
     * 设置标题请使用{@link #setTitle(String)} 方法
     */
    @Deprecated
    @Override
    public void setTitle(int titleId) {
        //super.setTitle(titleId);
    }


    public PromptDialog setContent(String content) {
        this.content = content;
        return this;
    }

//    public PromptDialog setContent(@StringRes int id) {
//        return setContent(ContextUtil.getString(id));
//    }


    /**
     * 左边的 （默认取消）
     *
     * @param negativeLabel
     * @return
     */
//    public PromptDialog setNegativeButton(@StringRes int negativeLabel, @ColorRes int textColorInt) {
//        return setNegativeButton(ContextUtil.getString(negativeLabel), textColorInt);
//    }

    public PromptDialog setNegativeButton(String negativeLabel, @ColorRes int textColorInt) {
        this.negativeLabel = negativeLabel;
        this.negativeTextColor = textColorInt;
        return this;
    }


//    public PromptDialog setNegativeButton(@StringRes int negativeLabel) {
//        return setNegativeButton(ContextUtil.getString(negativeLabel));
//    }

    public PromptDialog setNegativeButton(String negativeLabel) {
        this.negativeLabel = negativeLabel;
        return this;
    }

    /**
     * 右边的 （默认确定）
     *
     * @return
     */
//    public PromptDialog setPositiveButton(@StringRes int positiveLabel, @ColorRes int textColorInt) {
//        return setPositiveButton(ContextUtil.getString(positiveLabel), textColorInt);
//    }

    public PromptDialog setPositiveButton(String positiveLabel, @ColorRes int textColorInt) {
        this.positiveLabel = positiveLabel;
        this.positiveTextColor = textColorInt;
        return this;
    }

//    public PromptDialog setPositiveButton(@StringRes int positiveLabel) {
//        return setPositiveButton(ContextUtil.getString(positiveLabel));
//    }

    public PromptDialog setPositiveButton(String positiveLabel) {
        this.positiveLabel = positiveLabel;
        return this;
    }


    public PromptDialog setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void show() {
        super.show();
    }


    public interface OnButtonClickListener {

        /**
         * 当窗口按钮被点击
         * @param dialog
         * @param isPositiveClick true :PositiveButton点击, false :NegativeButton点击
         */
        void onButtonClick(PromptDialog dialog, boolean isPositiveClick);
    }


}
