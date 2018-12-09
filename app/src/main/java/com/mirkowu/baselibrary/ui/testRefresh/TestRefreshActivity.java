package com.mirkowu.baselibrary.ui.testRefresh;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.RefreshActivity;
import com.mirkowu.baselibrary.bean.GoodsBean;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.base.BaseRVHolder;
import com.softgarden.baselibrary.base.SelectedAdapter;
import com.softgarden.baselibrary.network.RxCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TestRefreshActivity extends RefreshActivity<TestRefreshPresenter> {
    private SelectedAdapter<String> mAdapter;

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
        setPageCount(10);
        mAdapter = new SelectedAdapter<String>(R.layout.item_flexbox) {
            @Override
            public void onBindVH(BaseRVHolder holder, String data, int position) {
                holder.setText(R.id.tvContent, "" + data);
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
        String[] tabs = new String[]{"标签", "标签标签标签"};
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showProgressDialog())
                .doFinally(() -> hideProgressDialog())
                .compose(bindToLifecycle())
                .subscribe(aLong -> {
                    List<String> data = new ArrayList<>();
                    int count;
                    if (mPage == 5) {
                        count = 3;
                    } else {
                        count = PAGE_COUNT;
                    }
                    for (int i = 0; i < count; i++) {
                        data.add(mPage + tabs[i % 2] + i);
                    }

                    if (mPage == 0) {
                        mAdapter.setNewData(null);
                    }
                    setLoadMore(mAdapter, data);
                });

    }

    private void loadData2() {

        getPresenter().getData2().subscribe(new RxCallback<List<GoodsBean>>() {
            @Override
            public void onSuccess(@Nullable List<GoodsBean> data) {

            }
        });
        getPresenter().getData2().subscribe(goodsBeans -> {

        }, throwable -> showError(throwable));
    }

}
