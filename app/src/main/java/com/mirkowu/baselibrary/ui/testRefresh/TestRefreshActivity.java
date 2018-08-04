package com.mirkowu.baselibrary.ui.testRefresh;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.RefreshActivity;
import com.mirkowu.baselibrary.bean.GoodsBean;
import com.mirkowu.basetoolbar.BaseToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;

public class TestRefreshActivity extends RefreshActivity<TestRefreshPresenter> implements TestRefreshContract.Display {
    //  private SelectedAdapter<String> mAdapter;
    private MuiltTypeAdapter<MuiltClassBean> mAdapter;

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
//        mAdapter = new SelectedAdapter<String>(R.layout.item_flexbox) {
//            @Override
//            public void onBindVH(BaseRVHolder holder, String data, int position) {
//                holder.setText(R.id.tvContent, "" + data);
//            }
//        };

        //  mAdapter.setSelectMode(true);
        mAdapter = new MuiltTypeAdapter();

        FlexboxLayoutManager manager = new FlexboxLayoutManager();
        //设置主轴排列方式
        manager.setFlexDirection(FlexDirection.ROW);
        //设置是否换行
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setAlignItems(AlignItems.STRETCH);

        StaggeredGridLayoutManager manager1 = new StaggeredGridLayoutManager(6, VERTICAL);

        mRecyclerView.setLayoutManager(manager1);
        mRecyclerView.setAdapter(mAdapter);

        loadData2();
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("上拉刷新，下拉加载");
    }

    private void loadData() {
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

        //   getPresenter().getData();
    }

    private void loadData2() {
        String[] tabs = new String[]{"标签", "标签标签标签"};
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showProgressDialog())
                .doFinally(() -> hideProgressDialog())
                .compose(bindToLifecycle())
                .subscribe(aLong -> {
                    List<MuiltClassBean> data = new ArrayList<>();

                    for (int i = 0; i < 30; i++) {
                        data.add(new MuiltClassBean(i % 2 + 1));
                    }

                    if (mPage == 0) {
                        mAdapter.setNewData(null);
                    }
                    setLoadMore(mAdapter, data);
                });

        //   getPresenter().getData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData2();
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        loadData2();
    }

    @Override
    public void getData(List<GoodsBean> data) {
        setLoadData(mAdapter, data);//finishRefresh都已在父类中调用
    }
}
