package com.example.a81418.myapp1.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a81418.myapp1.R;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

public class BlankFragment extends Fragment {
    private SuperSwipeRefreshLayout superSwipeRefreshLayout;
    private View view;
    public int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_order, container, false);
    }
    public static BlankFragment newInstance(int flag) {
        Bundle args = new Bundle();
        //type代表页签，0：全部订单 1：待发货 2：待收货 3：已完成
        args.putString("type", String.valueOf(flag));
        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //下拉刷新
//    @Override
//    public void onRefresh() {
//        mPresenter.refresh();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (swipeToLoadLayout != null)
//                    swipeToLoadLayout.setRefreshing(false);
//            }
//        }, 2000);
//    }
}
