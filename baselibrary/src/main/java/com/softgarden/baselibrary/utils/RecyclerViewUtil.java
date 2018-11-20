package com.softgarden.baselibrary.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author by DELL
 * @date on 2018/1/11
 * @describe
 */

public class RecyclerViewUtil {
    /**
     * 设置自适应高度
     * @param recyclerView
     * @param layoutManager
     */
    public static void autoFixHeight(RecyclerView recyclerView, LinearLayoutManager layoutManager){
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
