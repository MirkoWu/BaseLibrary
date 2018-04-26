package com.mirkowu.baselibrary.ui.testRefresh;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.RefreshActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.base.BaseRVAdapter;
import com.softgarden.baselibrary.base.BaseRVHolder;

import java.util.ArrayList;
import java.util.List;

public class TestRefreshActivity extends RefreshActivity {

    private BaseRVAdapter<String> mAdapter;

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_refresh;
    }

    @Override
    protected void initialize() {
        initRefreshLayout();
        initRecyclerView();
        mAdapter = new BaseRVAdapter<String>(R.layout.item_bottom) {
            @Override
            public void onBindVH(BaseRVHolder holder, String data, int position) {
                holder.setText(R.id.tvContent, "" + data);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        loadData();
        setSupportActionBar(findViewById(R.id.mToolbar));
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                List<String> data = new ArrayList<>();
                int count;
                if(mPage==3){
                    count=3;
                }else {
                    count=10;
                }
                for (int i = 0; i < count; i++) {
                    data.add(mPage + "" + i);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mPage==0){
                            mAdapter.setNewData(null);
                        }
                        setLoadMore(mRecyclerView, mAdapter, data);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        loadData();
    }
}
