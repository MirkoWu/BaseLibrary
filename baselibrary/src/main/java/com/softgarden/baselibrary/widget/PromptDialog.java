package com.softgarden.baselibrary.widget;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.base.BaseDialogFragment;
import com.softgarden.baselibrary.base.IBasePresenter;
import com.softgarden.baselibrary.utils.ContextUtil;
import com.softgarden.baselibrary.utils.L;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * @author by DELL
 * @date on 2018/1/5
 * @describe
 */

public class PromptDialog extends BaseDialogFragment<IBasePresenter> implements View.OnClickListener {

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
    private OnDialogClickListener listener;

    @Override
    public int getLayoutId() {
        return R.layout.dialog_prompt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void initialize() {
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
        L.d("title=="+title);
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
                listener.onDialogClick(this, true);
            } else if (i == R.id.tvNegative) {
                listener.onDialogClick(this, false);
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

    public PromptDialog setTitle(@StringRes int id) {
        return setTitle(ContextUtil.getString(id));
    }

    public PromptDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public PromptDialog setContent(@StringRes int id) {
        return setContent(ContextUtil.getString(id));
    }


    /**
     * 左边的 （默认取消）
     *
     * @param negativeLabel
     * @return
     */
    public PromptDialog setNegativeButton(@StringRes int negativeLabel, @ColorRes int textColorInt) {
        return setNegativeButton(ContextUtil.getString(negativeLabel), textColorInt);
    }

    public PromptDialog setNegativeButton(String negativeLabel, @ColorRes int textColorInt) {
        this.negativeLabel = negativeLabel;
        this.negativeTextColor = textColorInt;
        return this;
    }


    public PromptDialog setNegativeButton(@StringRes int negativeLabel) {
        return setNegativeButton(ContextUtil.getString(negativeLabel));
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
    public PromptDialog setPositiveButton(@StringRes int positiveLabel, @ColorRes int textColorInt) {
        return setPositiveButton(ContextUtil.getString(positiveLabel), textColorInt);
    }

    public PromptDialog setPositiveButton(String positiveLabel, @ColorRes int textColorInt) {
        this.positiveLabel = positiveLabel;
        this.positiveTextColor = textColorInt;
        return this;
    }

    public PromptDialog setPositiveButton(@StringRes int positiveLabel) {
        return setPositiveButton(ContextUtil.getString(positiveLabel));
    }

    public PromptDialog setPositiveButton(String positiveLabel) {
        this.positiveLabel = positiveLabel;
        return this;
    }


    public PromptDialog setOnButtonClickListener(OnDialogClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 显示Dialog
     */
    public void show(AppCompatActivity activity) {
        this.show(activity.getSupportFragmentManager(), "");
    }


    public interface OnDialogClickListener {

        /**
         * 当窗口按钮被点击
         */
        void onDialogClick(PromptDialog dialog, boolean isPositiveClick);
    }


}
