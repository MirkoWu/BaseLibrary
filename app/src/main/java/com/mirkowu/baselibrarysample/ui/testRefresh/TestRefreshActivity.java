package com.mirkowu.baselibrarysample.ui.testRefresh;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.RefreshActivity;
import com.mirkowu.baselibrarysample.bean.ImageBean;
import com.mirkowu.baselibrarysample.utils.ImageUtil;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.base.BaseRVHolder;
import com.softgarden.baselibrary.base.SelectedAdapter;
import com.softgarden.baselibrary.network.RxCallback;
import com.softgarden.baselibrary.utils.ToastUtil;

import java.util.List;

public class TestRefreshActivity extends RefreshActivity<TestRefreshPresenter> {
    private SelectedAdapter<ImageBean> mAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, TestRefreshActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_refresh;
    }

    @Override
    protected void initialize() {
        initRefreshLayout();
        initRecyclerView();
        mAdapter = new SelectedAdapter<ImageBean>(R.layout.item_images) {
            @Override
            public void onBindVH(BaseRVHolder holder, ImageBean data, int position) {
                ImageUtil.load(holder.getView(R.id.ivImage),data.getUrl());
            }
        };
        mAdapter.setSelectMode(true);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("上拉刷新，下拉加载");
    }

    public void loadData() {
        getPresenter().getData2(mPage, PAGE_COUNT).subscribe(new RxCallback<List<ImageBean>>() {
            @Override
            public void onSuccess(@Nullable List<ImageBean> data) {
                setLoadMore(mAdapter, data);
            }
        });

    }


}
