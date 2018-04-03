package com.yunshu.baseandroid.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yunshu.baseandroid.R;
import com.yunshu.baseandroid.entity.NewsContainerData;
import com.yunshu.baseandroid.smart.BaseRecyclerAdapter;
import com.yunshu.baseandroid.smart.SmartViewHolder;
import com.yunshu.baseandroid.util.ConstantApi;
import com.yunshu.commonlib.okhttp.ResultDisposer;
import com.yunshu.commonlib.util.LogWrapper;
import com.yunshu.commonlib.util.ToastUtil;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.layout.simple_list_item_2;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ListActivity extends BaseActivity {

    private static final String NEWS_URL = ConstantApi.NEWS_LIST;
    private static final int PAGE_SIZE = 10;
    private static final int NEWS_TYPE = 3;
    private int index = 1;

    private RecyclerView mList;
    private BaseRecyclerAdapter<NewsContainerData> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mList = findViewById(R.id.recyclerView);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setAdapter(mAdapter = new BaseRecyclerAdapter<NewsContainerData>(simple_list_item_2) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, NewsContainerData model, int position) {
                holder.text(android.R.id.text1, String.format(Locale.CHINA, model.getTitle(), position));
                holder.text(android.R.id.text2, String.format(Locale.CHINA, model.getCreateDate(), position));
//                holder.textColorId(android.R.id.text2, R.color.colorTextAssistant);
            }
        });
        final RefreshLayout refresher = findViewById(R.id.refreshLayout);

        refresher.setEnableAutoLoadmore(true);
        refresher.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                String url = NEWS_URL + "/" + 1 +"/" + PAGE_SIZE + "/" + NEWS_TYPE;
                LogWrapper.d("=====onRefresh" + url);
                okHttpService.get(url, new ResultDisposer() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtil.textToast(ListActivity.this, error);
                    }

                    @Override
                    public void onSuccess(String data) {
                        try {
                            ArrayList<NewsContainerData> dd = gson.jsonToArrayList(data, NewsContainerData.class);
                            index = 1;
                            refreshlayout.finishRefresh();
                            refreshlayout.resetNoMoreData();
                            mAdapter.refresh(dd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        refresher.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                String url = NEWS_URL + "/" + index +"/" + PAGE_SIZE + "/" + NEWS_TYPE;
                LogWrapper.d("=====load more" + url);
                okHttpService.get(url, new ResultDisposer() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtil.textToast(ListActivity.this, error);
                    }

                    @Override
                    public void onSuccess(String data) {
                        try {
                            ArrayList<NewsContainerData> dd = gson.jsonToArrayList(data, NewsContainerData.class);
                            index ++;
                            if (dd.size() >= PAGE_SIZE) {
                                refreshlayout.finishLoadmore();
                            } else {
                                refreshlayout.finishLoadmoreWithNoMoreData();//将不会再次触发加载更多事件
                            }
                            mAdapter.loadmore(dd);
                            LogWrapper.d(data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        refresher.autoRefresh();
    }

}
