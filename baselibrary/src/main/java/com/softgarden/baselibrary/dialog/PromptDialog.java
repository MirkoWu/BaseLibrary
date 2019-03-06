package com.softgarden.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.ScreenUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 提示窗  继承自Dialog
 *
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
    private boolean cancelable;
    private String positiveLabel;
    private String negativeLabel;
    private int positiveTextColor;
    private int negativeTextColor;
    private boolean useDefaultButton;
    private OnButtonClickListener listener;
    private float dimAmount = 0.3f;

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
        params.dimAmount = dimAmount;//遮罩透明度
        getWindow().setAttributes(params);

        initView();
    }


    private void initView() {
        ivIcon = findViewById(R.id.ivIcon);
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        tvPositive = findViewById(R.id.tvPositive);
        tvNegative = findViewById(R.id.tvNegative);
        tvPositive.setOnClickListener(this);
        tvNegative.setOnClickListener(this);

        ivIcon.setImageResource(icon);
        tvTitle.setText(title);
        tvContent.setText(content);
        ivIcon.setVisibility(icon > 0 ? VISIBLE : GONE);
        tvTitle.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        tvContent.setVisibility(TextUtils.isEmpty(content) ? GONE : VISIBLE);

        if (!useDefaultButton) {
            tvPositive.setText(positiveLabel);
            tvNegative.setText(negativeLabel);
            tvPositive.setVisibility(TextUtils.isEmpty(positiveLabel) ? GONE : VISIBLE);
            tvNegative.setVisibility(TextUtils.isEmpty(negativeLabel) ? GONE : VISIBLE);

            if (positiveTextColor != 0)
                tvPositive.setTextColor(ContextCompat.getColor(getContext(), positiveTextColor));
            if (negativeTextColor != 0)
                tvNegative.setTextColor(ContextCompat.getColor(getContext(), negativeTextColor));
        }

        setCancelable(cancelable);
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

    /**
     * 设置图标
     *
     * @param icon
     * @return
     */
    public PromptDialog setIcon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
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


    /**
     * 设置内容
     *
     * @param content
     * @return
     */
    public PromptDialog setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Dialog是否 可以取消
     *
     * @param cancelable
     * @return
     */
    public PromptDialog setDialogCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * 是否使用默认的 按钮
     *
     * @return
     */
    public PromptDialog useDefButton() {
        useDefaultButton = true;
        return this;
    }

    /**
     * 设置透明度
     *
     * @param dimAmount 0-1f 透明 - 不透明
     * @return
     */
    public PromptDialog setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    /**
     * 左边的 （默认取消）
     *
     * @param negativeLabel
     * @return
     */
    public PromptDialog setNegativeButton(String negativeLabel, @ColorRes int textColorInt) {
        this.negativeLabel = negativeLabel;
        this.negativeTextColor = textColorInt;
        return this;
    }

    public PromptDialog setNegativeButton(String negativeLabel) {
        this.negativeLabel = negativeLabel;
        return this;
    }


    /**
     * 右边的 （默认确定）
     *
     * @return
     */
    public PromptDialog setPositiveButton(String positiveLabel, @ColorRes int textColorInt) {
        this.positiveLabel = positiveLabel;
        this.positiveTextColor = textColorInt;
        return this;
    }

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
         *
         * @param dialog
         * @param isPositiveClick true :PositiveButton点击, false :NegativeButton点击
         */
        void onButtonClick(PromptDialog dialog, boolean isPositiveClick);
    }


}
