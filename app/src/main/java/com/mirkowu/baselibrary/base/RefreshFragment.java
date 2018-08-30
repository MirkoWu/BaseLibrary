package com.mirkowu.baselibrary.base;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mirkowu.baselibrary.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.softgarden.baselibrary.base.BaseLazyFragment;
import com.softgarden.baselibrary.base.IBasePresenter;
import com.softgarden.baselibrary.utils.EmptyUtil;
import com.softgarden.baselibrary.widget.ColorDividerDecoration;

import java.util.List;

/**
 * @author by DELL
 * @date on 2017/9/27
 * @describe 通用的列表刷新Fragment
 * 上拉刷新用的是 SmartRefreshLayout
 * 下拉加载用的是 BaseAdapter带的
 * 注意事项：
 * 刷新控件 id必须为：mRefreshLayout
 * RecyclerView id必须为：mRecyclerView
 * 当然也可以自己调整
 */
public abstract class RefreshFragment<P extends IBasePresenter> extends BaseLazyFragment<P> implements
        BaseQuickAdapter.RequestLoadMoreListener {
    /*** 每页请求数量 */
    public static int PAGE_COUNT = 10;
    /*** 页码，默认从1开始 */
    protected int mPage = 1;
    /*** 空布局类型 可以在这里设置默认值 */
    protected int emptyType;
    /*** 是否启用空布局 */
    private boolean enableEmptyView = false;

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    /**
     * 设置刷新框架，需要时调用即可
     */
    protected void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) getView().findViewById(R.id.mRefreshLayout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(false);
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

    /**
     * 开关刷新
     *
     * @param enableRefresh
     */
    public void setEnableRefresh(boolean enableRefresh) {
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableRefresh(enableRefresh);
    }

    /**
     * 初始化列表控件
     */
    protected void initRecyclerView() {
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.mRecyclerView);
    }

    /**
     * 设置分割线
     *
     * @param id
     */
    protected void addItemDecoration(@ColorRes int id) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(getActivity(), ContextCompat.getColor(getActivity(), id)));
        }
    }

    protected void addItemDecoration() {
        //设置默认分割线
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(getActivity(), Color.parseColor("#CCCCCC")));
        }
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


    /***
     * 是否启用空布局
     * @param enableEmptyView
     */
    public void setEnableEmptyView(boolean enableEmptyView) {
        this.enableEmptyView = enableEmptyView;
    }

    public void setEnableEmptyView(boolean enableEmptyView, int emptyType) {
        this.enableEmptyView = enableEmptyView;
        this.emptyType = emptyType;
    }


    public void setLoadData(BaseQuickAdapter adapter, List<?> list) {
        setLoadData(adapter, list, emptyType);//emptyType 可以给一个默认值,作为统一设置
    }

    /**
     * 不分页的 设置数据
     *
     * @param adapter
     * @param list
     * @param emptyType
     */
    public void setLoadData(BaseQuickAdapter adapter, List<?> list, int emptyType) {
        this.emptyType = emptyType;
        finishRefresh();
        adapter.setNewData(list);
        if (EmptyUtil.isEmpty(list)) {
            setEmptyView(adapter);
        }
    }


    public void setLoadMore(BaseQuickAdapter adapter, List<?> list) {
        setLoadMore(adapter, list, emptyType);
    }

    /**
     * 如果使用的是当前的 mRecyclerView
     *
     * @param adapter
     * @param list
     * @param emptyType
     */
    public void setLoadMore(BaseQuickAdapter adapter, List<?> list, int emptyType) {
        if (mRecyclerView != null) {
            setLoadMore(mRecyclerView, adapter, list, emptyType);
        }
    }


    /**
     * 结束刷新
     * 自动设置上拉更多
     *
     * @param adapter
     * @param list
     */
    public void setLoadMore(RecyclerView recyclerView, BaseQuickAdapter adapter, List<?> list, int emptyType) {
        this.emptyType = emptyType;
        finishRefresh();//结束刷新
        if (mPage == 1) {
            adapter.setNewData(list);
            if (list == null || list.size() == 0) {
                setEmptyView(adapter);
            }
        } else {
            adapter.addData(list);
        }

        if (list == null || list.size() < PAGE_COUNT) {
            adapter.loadMoreEnd();
        } else {
            adapter.setEnableLoadMore(true);
            adapter.setOnLoadMoreListener(this, recyclerView);
            adapter.loadMoreComplete();
        }
    }

    /**
     * 设置空状态
     * <p>
     * 根据项目情况 自由定制
     *
     * @param adapter
     */
    protected void setEmptyView(BaseQuickAdapter adapter) {

//        if (enableEmptyView) {

//            View emptyView = View.inflate(this, R.layout.layout_empty, null);
//            ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.ivEmpty);
//            TextView tvEmptyHint = (TextView) emptyView.findViewById(R.id.tvEmptyHint);

//            String hintDef = "亲，暂未查找到相关数据哦~";
//            int imgResId = R.mipmap.empty_default;
//        switch (emptyType) {
//                case Constants.emptyType.SHOPCART:
//                    imgResId = R.mipmap.empty_shopcart;
//                    hintDef = "亲，你的购物车还没有宝贝哦~";
//                    break;
//                case Constants.emptyType.MESSAGE:
//                    imgResId = R.mipmap.empty_message;
//                    hintDef = "亲，你还没有消息哦~";
//                    break;
//                case Constants.emptyType.ORDER:
//                    imgResId = R.mipmap.empty_order;
//                    hintDef = "亲，你还没有订单哦~";
//                    break;
//                case Constants.emptyType.ADDRESS:
//                    imgResId = R.mipmap.empty_address;
//                    hintDef = "亲，你还没有添加地址哦~";
//                    break;
//                case Constants.emptyType.BENIFT:
//                    imgResId = R.mipmap.empty_benift;
//                    hintDef = "亲，你还没有收益哦~";
//                    break;
//                case Constants.emptyType.COUPON:
//                    imgResId = R.mipmap.empty_coupon;
//                    hintDef = "亲，你还没有购物券哦~";
//                    break;
//                case Constants.emptyType.COLLETION:
//                    imgResId = R.mipmap.empty_collection;
//                    hintDef = "亲，你还没有收藏宝贝哦~";
//                    break;
//            }
//            ivEmpty.setImageResource(imgResId);
//            tvEmptyHint.setText(hintDef);
//            adapter.setEmptyView(emptyView);
//        }
    }

    /**
     * 结束刷新
     */
    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        finishRefresh();
    }

    /**
     * 如需上拉更多 请重写该方法
     */
    @Override
    public void onLoadMoreRequested() {

    }

    public abstract void onRefresh();

}
