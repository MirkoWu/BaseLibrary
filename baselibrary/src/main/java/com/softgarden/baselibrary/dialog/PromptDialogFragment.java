package com.softgarden.baselibrary.dialog;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.base.BaseDialogFragment;
import com.softgarden.baselibrary.base.IBasePresenter;
import com.softgarden.baselibrary.utils.ContextUtil;
import com.softgarden.baselibrary.utils.ScreenUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 提示窗  继承自DialogFragment
 *
 * @author by DELL
 * @date on 2018/1/5
 * @describe
 */

public class PromptDialogFragment extends BaseDialogFragment<IBasePresenter> implements View.OnClickListener {

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

    @Override
    public int getLayoutId() {
        return R.layout.dialog_prompt;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        int width = (int) (ScreenUtil.getScreenWidth(getContext()) * 0.75);//设定宽度为屏幕宽度的0.75
        getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    public PromptDialogFragment setIcon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    public PromptDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public PromptDialogFragment setTitle(@StringRes int id) {
        return setTitle(getContext().getString(id));
    }

    public PromptDialogFragment setContent(String content) {
        this.content = content;
        return this;
    }

    public PromptDialogFragment setContent(@StringRes int id) {
        return setContent(getContext().getString(id));
    }


    /**
     * 左边的 （默认取消）
     *
     * @param negativeLabel
     * @return
     */
    public PromptDialogFragment setNegativeButton(@StringRes int negativeLabel, @ColorRes int textColorInt) {
        return setNegativeButton(getContext().getString(negativeLabel), textColorInt);
    }

    public PromptDialogFragment setNegativeButton(String negativeLabel, @ColorRes int textColorInt) {
        this.negativeLabel = negativeLabel;
        this.negativeTextColor = textColorInt;
        return this;
    }


    public PromptDialogFragment setNegativeButton(@StringRes int negativeLabel) {
        return setNegativeButton(getContext().getString(negativeLabel));
    }

    public PromptDialogFragment setNegativeButton(String negativeLabel) {
        this.negativeLabel = negativeLabel;
        return this;
    }

    /**
     * 右边的 （默认确定）
     *
     * @return
     */
    public PromptDialogFragment setPositiveButton(@StringRes int positiveLabel, @ColorRes int textColorInt) {
        return setPositiveButton(getContext().getString(positiveLabel), textColorInt);
    }

    public PromptDialogFragment setPositiveButton(String positiveLabel, @ColorRes int textColorInt) {
        this.positiveLabel = positiveLabel;
        this.positiveTextColor = textColorInt;
        return this;
    }

    public PromptDialogFragment setPositiveButton(@StringRes int positiveLabel) {
        return setPositiveButton(getContext().getString(positiveLabel));
    }

    public PromptDialogFragment setPositiveButton(String positiveLabel) {
        this.positiveLabel = positiveLabel;
        return this;
    }


    public PromptDialogFragment setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 显示Dialog
     */
    public void show(AppCompatActivity activity) {
        this.show(activity.getSupportFragmentManager(), "");
    }


    public interface OnButtonClickListener {

        /**
         * 当窗口按钮被点击
         * @param dialog
         * @param isPositiveClick true :PositiveButton点击, false :NegativeButton点击
         */
        void onButtonClick(PromptDialogFragment dialog, boolean isPositiveClick);
    }


}
