package com.softgarden.baselibrary.base;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * ViewHolder基类 封装
 */

public class BaseRVHolder extends BaseViewHolder {
    public BaseRVHolder(View view) {
        super(view);
    }

    public <T extends View> T $(int id) {
        return getView(id);
    }


    /**
     * 从新定义
     * @param viewId
     * @param visible
     * @return
     */
    @Override
    public BaseRVHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseRVHolder setSelected(@IdRes int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }


    @Override
    public BaseRVHolder setText(int viewId, CharSequence value) {
        return (BaseRVHolder) super.setText(viewId, value);
    }

    @Override
    public BaseRVHolder setText(int viewId, int strId) {
        return (BaseRVHolder) super.setText(viewId, strId);
    }

    @Override
    public BaseRVHolder setImageResource(int viewId, int imageResId) {
        return (BaseRVHolder) super.setImageResource(viewId, imageResId);
    }

    @Override
    public BaseRVHolder setBackgroundColor(int viewId, int color) {
        return (BaseRVHolder) super.setBackgroundColor(viewId, color);
    }

    @Override
    public BaseRVHolder setBackgroundRes(int viewId, int backgroundRes) {
        return (BaseRVHolder) super.setBackgroundRes(viewId, backgroundRes);
    }

    @Override
    public BaseRVHolder setTextColor(int viewId, int textColor) {
        return (BaseRVHolder) super.setTextColor(viewId, textColor);
    }

    @Override
    public BaseRVHolder setImageDrawable(int viewId, Drawable drawable) {
        return (BaseRVHolder) super.setImageDrawable(viewId, drawable);
    }

    @Override
    public BaseRVHolder setImageBitmap(int viewId, Bitmap bitmap) {
        return (BaseRVHolder) super.setImageBitmap(viewId, bitmap);
    }

    @Override
    public BaseRVHolder setAlpha(int viewId, float value) {
        return (BaseRVHolder) super.setAlpha(viewId, value);
    }

    @Override
    public BaseRVHolder setGone(int viewId, boolean visible) {
        return (BaseRVHolder) super.setGone(viewId, visible);
    }

    @Override
    public BaseRVHolder linkify(int viewId) {
        return (BaseRVHolder) super.linkify(viewId);
    }

    @Override
    public BaseRVHolder setTypeface(int viewId, Typeface typeface) {
        return (BaseRVHolder) super.setTypeface(viewId, typeface);
    }

    @Override
    public BaseRVHolder setTypeface(Typeface typeface, int... viewIds) {
        return (BaseRVHolder) super.setTypeface(typeface, viewIds);
    }

    @Override
    public BaseRVHolder setProgress(int viewId, int progress) {
        return (BaseRVHolder) super.setProgress(viewId, progress);
    }

    @Override
    public BaseRVHolder setProgress(int viewId, int progress, int max) {
        return (BaseRVHolder) super.setProgress(viewId, progress, max);
    }

    @Override
    public BaseRVHolder setMax(int viewId, int max) {
        return (BaseRVHolder) super.setMax(viewId, max);
    }

    @Override
    public BaseRVHolder setRating(int viewId, float rating) {
        return (BaseRVHolder) super.setRating(viewId, rating);
    }

    @Override
    public BaseRVHolder setRating(int viewId, float rating, int max) {
        return (BaseRVHolder) super.setRating(viewId, rating, max);
    }

    @Override
    public BaseRVHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        return (BaseRVHolder) super.setOnClickListener(viewId, listener);
    }

    @Override
    public BaseRVHolder addOnClickListener(int viewId) {
        return (BaseRVHolder) super.addOnClickListener(viewId);
    }

    @Override
    public BaseRVHolder setNestView(int viewId) {
        return (BaseRVHolder) super.setNestView(viewId);
    }

    @Override
    public BaseRVHolder addOnLongClickListener(int viewId) {
        return (BaseRVHolder) super.addOnLongClickListener(viewId);
    }

    @Override
    public BaseRVHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        return (BaseRVHolder) super.setOnTouchListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        return (BaseRVHolder) super.setOnLongClickListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        return (BaseRVHolder) super.setOnItemClickListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        return (BaseRVHolder) super.setOnItemLongClickListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        return (BaseRVHolder) super.setOnItemSelectedClickListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        return (BaseRVHolder) super.setOnCheckedChangeListener(viewId, listener);
    }

    @Override
    public BaseRVHolder setTag(int viewId, Object tag) {
        return (BaseRVHolder) super.setTag(viewId, tag);
    }

    @Override
    public BaseRVHolder setTag(int viewId, int key, Object tag) {
        return (BaseRVHolder) super.setTag(viewId, key, tag);
    }

    @Override
    public BaseRVHolder setChecked(int viewId, boolean checked) {
        return (BaseRVHolder) super.setChecked(viewId, checked);
    }

    @Override
    public BaseRVHolder setAdapter(int viewId, Adapter adapter) {
        return (BaseRVHolder) super.setAdapter(viewId, adapter);
    }

    @Override
    protected BaseRVHolder setAdapter(BaseQuickAdapter adapter) {
        return (BaseRVHolder) super.setAdapter(adapter);
    }


}
