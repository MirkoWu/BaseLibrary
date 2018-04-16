package com.mirkowu.baselibrary.base;

import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mirkowu.baselibrary.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.softgarden.baselibrary.base.BaseLazyFragment;
import com.softgarden.baselibrary.widget.ColorDividerDecoration;

import java.util.List;

/**
 * @author by DELL
 * @date on 2017/9/27
 * @describe 通用的列表刷新Fragment
 */
public abstract class RefreshFragment extends BaseLazyFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    /*** 每页请求数量 */
    public static final int PAGE_COUNT = 10;

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected int mPage = 1;//默认从1开始

//    /**
//     * initialize 已被实现  需要调用super()
//     */
//    @Override
//    protected void initialize() {
//        initRefreshLayout();
//        initRecyclerView();
//    }


    protected void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) getView().findViewById(R.id.mRefreshLayout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setDisableContentWhenLoading(true);
            mRefreshLayout.setDisableContentWhenRefresh(true);
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    RefreshFragment.this.onRefresh();
                }
            });
        }
    }

    protected void initRecyclerView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.mRecyclerView);
    }

    protected void addItemDecoration(@ColorRes int id) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(getActivity(), ContextCompat.getColor(getActivity(),id)));
        }
    }

    protected void addItemDecoration() {
        //设置默认分割线
      //  addItemDecoration(R.color.grayLight);
    }


    /**
     * 自动刷新
     */
    public void autoRefresh() {
        if (mRefreshLayout != null)
            mRefreshLayout.autoRefresh(0);
    }

    /**
     * 结束刷新
     */
    public void finishRefresh() {
        if (mRefreshLayout != null)
            mRefreshLayout.finishRefresh(0);
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        finishRefresh();
    }


    public void setEnableRefresh(boolean enableRefresh) {
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableRefresh(enableRefresh);
    }

    public void setEnableLoadMore(boolean enableLoadmore) {
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableLoadMore(enableLoadmore);
    }

    /**
     * 结束刷新
     * 自动设置上拉更多
     *
     * @param adapter
     * @param list
     */
    public void setLoadMore(RecyclerView recyclerView, BaseQuickAdapter adapter, List<?> list) {
        finishRefresh();
        if (mPage == 1) {
            adapter.setNewData(list);
        } else {
            adapter.addData(list);
        }

        if (list == null || list.size() < PAGE_COUNT) {
            adapter.loadMoreEnd();
        } else {
            setEnableLoadMore(true);
            adapter.setOnLoadMoreListener(this, recyclerView);
            adapter.loadMoreComplete();
        }
    }

    /**
     * 如需上拉更多 请重新该方法
     */
    @Override
    public void onLoadMoreRequested() {

    }

    public abstract void onRefresh();
}
