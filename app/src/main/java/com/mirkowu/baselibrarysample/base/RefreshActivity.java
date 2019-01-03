package com.mirkowu.baselibrarysample.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mirkowu.baselibrarysample.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.softgarden.baselibrary.base.IBasePresenter;
import com.softgarden.baselibrary.utils.DisplayUtil;
import com.softgarden.baselibrary.utils.EmptyUtil;
import com.softgarden.baselibrary.widget.ColorDividerDecoration;

import java.util.List;

/**
 * @author by DELL
 * @date on 2017/9/27
 * @describe 通用的列表刷新Activity
 * <p>
 * 上拉刷新用的是 SmartRefreshLayout
 * 下拉加载用的是 BaseAdapter带的
 * 注意事项：
 * 刷新控件 id必须为：mRefreshLayout
 * RecyclerView id必须为：mRecyclerView
 * 当然也可以自己调整
 */
public abstract class RefreshActivity<P extends IBasePresenter> extends ToolbarActivity<P> implements
        BaseQuickAdapter.RequestLoadMoreListener {
    /*** 每页请求数量 */
    public int PAGE_COUNT = 10;
    /*** 页码，默认从1开始 */
    protected int PageStart = 1;
    protected int mPage = 1;
    /*** 空布局类型 可以在这里设置默认值 */
    protected int emptyType = -1;
    /*** 是否启用空布局 */
    private boolean enableEmptyView = false;
    protected boolean isLoadEnd;//是否已经加载完了，没有更多数据了

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    /**
     * 设置刷新框架，需要时调用即可
     */
    protected void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.mRefreshLayout);
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
                    RefreshActivity.this.onRefresh();
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
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }

    public void setPageCount(int pageCount) {
        PAGE_COUNT = pageCount;
    }

    public void setPageStart(int pageStart) {
        PageStart = pageStart;
    }

    /**
     * 设置分割线
     *
     * @param colorId
     */
    protected void addItemDecoration(@ColorRes int colorId) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(this, colorId)));
        }
    }

    protected void addItemDecoration(@ColorRes int colorId, int dp) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(getActivity(), colorId), DisplayUtil.dip2px(getActivity(), dp)));
        }
    }

    protected void addItemDecoration() {
        //设置默认分割线
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(Color.parseColor("#CCCCCC")));
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
        adapter.setHeaderFooterEmpty(true, true);//是否显示头部和底部

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
        adapter.setHeaderFooterEmpty(true, true);//是否显示头部和底部

        finishRefresh();//结束刷新
        if (mPage == PageStart) {
            isLoadEnd = false;
            adapter.setNewData(list);
            if (EmptyUtil.isEmpty(list)) {
                isLoadEnd = true;
                setEmptyView(adapter);
            }
        } else {
            if (list != null && !list.isEmpty()) {
                adapter.addData(list);
            }
        }

        if (list == null || list.size() < PAGE_COUNT) {
            isLoadEnd = true;
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

        if (enableEmptyView) {

//            View emptyView = View.inflate(this, R.layout.layout_empty, null);
//            ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.ivEmpty);
//            TextView tvEmptyHint = (TextView) emptyView.findViewById(R.id.tvEmptyHint);
//
//            String hintDef = "亲，暂未查找到相关数据哦~";
//            int imgResId = R.mipmap.meiyouxinqing;
//            switch (emptyType) {
//                case EmptyConfig.NO_PUBLISH_MEDIA:
//                    imgResId = R.mipmap.meiyoushipin;
//                    hintDef = "您还没有拍摄的视频\n快去开拍吧~";
//                    break;
//                case EmptyConfig.NO_LIKE_MEDIA:
//                    imgResId = R.mipmap.meiyoudianzang;
//                    hintDef = "您还没有喜欢的视频\n快去点赞吧~";
//                    break;
//                case EmptyConfig.NO_FOLLOW:
//                    imgResId = R.mipmap.meiyouguanzhu;
//                    hintDef = "您还没有关注的人\n快去关注一下吧~";
//                    break;
//                case EmptyConfig.NO_MOOD:
//                    imgResId = R.mipmap.meiyouxinqing;
//                    hintDef = "您还没有发布的心情\n快去发布一下吧~";
//                    break;
//                case EmptyConfig.NO_CIRCLE:
//                    imgResId = R.mipmap.no_circle;
//                    hintDef = "您当前还没有加入圈子\n快去加入吧~";
//                    break;
//                case EmptyConfig.NO_ORDER:
//                    imgResId = R.mipmap.no_order;
//                    hintDef = "您还没有订单";
//                    break;
//                case EmptyConfig.NO_FOLLOW_ME:
//                    imgResId = R.mipmap.meiyouren_guanzhuwo;
//                    hintDef = "暂时还没有人关注您\n拍摄视频获取关注吧~";
//                    break;
//            }
//
//            ivEmpty.setImageResource(imgResId);
//            tvEmptyHint.setText(hintDef);
//            adapter.setEmptyView(emptyView);
        }
    }


    /**
     * 结束刷新
     */
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        finishRefresh();
    }

    public void onRefresh() {
        mPage = 1;
        loadData();
    }

    /**
     * 如需上拉更多 请重写该方法
     */
    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData();
    }


    public abstract void loadData();

}
