package com.yyj.bestbase.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseRecyclerView<T extends RecyclerView.ViewHolder> {

    private RecyclerView recyclerView;
    private int orientation = OrientationHelper.VERTICAL;
    private RecyclerView.ItemDecoration itemDecoration;
    private PagedListAdapter<JSONObject, T> pagedListAdapter;
    private LinearLayoutManager layoutManager;

    public BaseRecyclerView(Activity activity) {
        itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        layoutManager = new LinearLayoutManager(activity);
    }


    //设置布局管理器，init方法之前调用
    public BaseRecyclerView setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    //设置分割线样式，init方法之前调用
    public BaseRecyclerView setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        this.itemDecoration = itemDecoration;
        return this;
    }

    //设置水平方向，init方法之前调用
    public BaseRecyclerView setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    public PagedListAdapter<JSONObject, T> getPagedListAdapter() {
        return pagedListAdapter;
    }

    public RecyclerView build(BaseActivity activity, @IdRes int id, Adapeter<T> adapeter, int pageSize, int PrefetchDistance) {
        DiffUtil.ItemCallback<JSONObject> temCallback = new DiffUtil.ItemCallback<JSONObject>() {

            @Override
            public boolean areItemsTheSame(@NonNull JSONObject jsonObject, @NonNull JSONObject t1) {
                return jsonObject == t1;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull JSONObject jsonObject, @NonNull JSONObject t1) {
                return jsonObject == t1;
            }
        };
        AsyncDifferConfig<JSONObject> asyncDifferConfig = new AsyncDifferConfig.Builder<>(temCallback).build();
        pagedListAdapter = new PagedListAdapter<JSONObject, T>(asyncDifferConfig) {

            @NonNull
            @Override
            public T onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return adapeter.onCreateViewHolder(viewGroup, i);
            }

            @Override
            public void onBindViewHolder(@NonNull T mViewHold, int i) {
                adapeter.onBindViewHolder(mViewHold, i, getCurrentList());
            }

        };
        LiveData<PagedList<JSONObject>> liveData = new LivePagedListBuilder<>(new DataSource.Factory<Integer, JSONObject>() {

            @Override
            public DataSource<Integer, JSONObject> create() {
                return adapeter.create();
            }
        }, new PagedList.Config.Builder().setPageSize(pageSize).setPrefetchDistance(PrefetchDistance).setEnablePlaceholders(false).build()).build();
        liveData.observe(activity, pagedListAdapter::submitList);
        recyclerView = activity.findViewById(id);

        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置布局
        layoutManager.setOrientation(orientation);
        //设置Adapter
        recyclerView.setAdapter(pagedListAdapter);
        //设置分隔线
        if (itemDecoration != null)
            recyclerView.addItemDecoration(itemDecoration);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastPos;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                lastPos = layoutManager.findLastVisibleItemPosition();
                if (lastPos != -1)
                    Objects.requireNonNull(liveData.getValue()).loadAround(lastPos);//触发Android Paging的加载事务逻辑。
            }
        });
        return recyclerView;
    }

    public RecyclerView build(BaseActivity activity, View view, @IdRes int id, Adapeter<T> adapeter, int pageSize, int PrefetchDistance) {
        DiffUtil.ItemCallback<JSONObject> temCallback = new DiffUtil.ItemCallback<JSONObject>() {

            @Override
            public boolean areItemsTheSame(@NonNull JSONObject jsonObject, @NonNull JSONObject t1) {
                return jsonObject == t1;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull JSONObject jsonObject, @NonNull JSONObject t1) {
                return jsonObject == t1;
            }
        };
        AsyncDifferConfig<JSONObject> asyncDifferConfig = new AsyncDifferConfig.Builder<>(temCallback).build();
        PagedListAdapter<JSONObject, T> pagedListAdapter = new PagedListAdapter<JSONObject, T>(asyncDifferConfig) {

            @NonNull
            @Override
            public T onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return adapeter.onCreateViewHolder(viewGroup, i);
            }

            @Override
            public void onBindViewHolder(@NonNull T mViewHold, int i) {
                adapeter.onBindViewHolder(mViewHold, i, getCurrentList());
            }

        };
        LiveData<PagedList<JSONObject>> liveData = new LivePagedListBuilder<>(new DataSource.Factory<Integer, JSONObject>() {

            @Override
            public DataSource<Integer, JSONObject> create() {
                return adapeter.create();
            }
        }, new PagedList.Config.Builder().setPageSize(pageSize).setPrefetchDistance(PrefetchDistance).setEnablePlaceholders(false).build()).build();
        liveData.observe(activity, pagedListAdapter::submitList);
        recyclerView = view.findViewById(id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置布局
        layoutManager.setOrientation(orientation);
        //设置Adapter
        recyclerView.setAdapter(pagedListAdapter);
        //设置分隔线
        if (itemDecoration != null)
            recyclerView.addItemDecoration(itemDecoration);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastPos;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                lastPos = layoutManager.findLastVisibleItemPosition();
                if (lastPos != -1)
                    Objects.requireNonNull(liveData.getValue()).loadAround(lastPos);//触发Android Paging的加载事务逻辑。
            }
        });
        return recyclerView;
    }


    public interface Adapeter<T> {
        List<JSONObject> list = new ArrayList<>();

        T onCreateViewHolder(@NonNull ViewGroup viewGroup, int i);

        void onBindViewHolder(@NonNull T mViewHold, int i, PagedList<JSONObject> currentList);

        DataSource<Integer, JSONObject> create();
    }

}
