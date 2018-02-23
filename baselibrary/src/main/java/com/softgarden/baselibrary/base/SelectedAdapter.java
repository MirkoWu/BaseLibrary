package com.softgarden.baselibrary.base;

import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MirkoWu on 2017/3/30 0030.
 */

public abstract class SelectedAdapter<T> extends BaseRVAdapter<T> {
    protected boolean selectMode = true;//总开关 开启选择功能
    private int defSelectIndex = 0;//默认单个选中状态
    private int curSelectIndex = 0;//当前单个选中状态
    private boolean multiSelected;//是否多选 默认为单选
    private ArrayList<Integer> selectList = new ArrayList<>();//多选下标集合


    public SelectedAdapter(@LayoutRes int mLayoutResId) {
        super(mLayoutResId);

    }


    /**
     * 设置开启模式
     *
     * @param isOpen 开启选择模式  默认单选
     */
    public void setSelectMode(boolean isOpen) {
        this.selectMode = isOpen;
        notifyDataSetChanged();
        setSelectMode(isOpen, defSelectIndex);
    }


    /**
     * @param isOpen
     * @param defaultIndex 单选的 默认选中下标
     */
    public void setSelectMode(boolean isOpen, int defaultIndex) {
        this.selectMode = isOpen;
        this.defSelectIndex = defaultIndex;
        this.curSelectIndex = this.defSelectIndex;
        notifyDataSetChanged();
    }

    /**
     * 设置开启模式
     *
     * @param isOpen        开启选择模式
     * @param multiSelected 是否多选
     */
    public void setSelectMode(boolean isOpen, boolean multiSelected) {
        this.selectMode = isOpen;
        this.multiSelected = multiSelected;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedList() {
        return selectList;
    }

    /**
     * 默认的选择下标
     *
     * @return
     */
    public int getDefSelectedIndex() {
        return defSelectIndex;
    }

    public void setDefSelectedIndex(int index) {
        defSelectIndex = index;
        curSelectIndex = defSelectIndex;
        notifyDataSetChanged();
    }

    /**
     * 当前的选择下标
     *
     * @return
     */
    public int getSelectedIndex() {
        return curSelectIndex;
    }


    public void setSelectedIndex(int index) {
        curSelectIndex = index;
        notifyDataSetChanged();
    }


    @Override
    public void setNewData(List<T> data) {
        curSelectIndex = defSelectIndex;
        selectList.clear();
        super.setNewData(data);
    }

    public void clearSelected() {
        curSelectIndex = defSelectIndex;
        selectList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemClick(View v, int position) {
        super.setOnItemClick(v, position);
        //设置选择器
        if (selectMode) {
            if (multiSelected) {
                if (selectList.contains(position)) {
                    selectList.remove((Integer) position);
                } else {
                    selectList.add(position);
                }
            } else {
                if (curSelectIndex > -1) notifyItemChanged(curSelectIndex);
                curSelectIndex = position;
            }
            notifyItemChanged(position);
        }
    }

    @Override
    public void onBindViewHolder(BaseRVHolder holder, int position) {
        if (selectMode) {
            boolean isSelected = multiSelected ? selectList.contains(position) : curSelectIndex == position;//设置状态
            holder.itemView.setSelected(isSelected);
        }
        super.onBindViewHolder(holder, position);
    }

}
