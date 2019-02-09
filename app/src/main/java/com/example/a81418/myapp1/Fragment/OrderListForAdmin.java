package com.example.a81418.myapp1.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.a81418.myapp1.Adapter.OrderListAdapter;
import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.OrderBean;
import com.example.a81418.myapp1.R;
import com.example.a81418.myapp1.view.recyclerview.MyRecyclerView;
import com.example.a81418.myapp1.view.recyclerview.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class OrderListForAdmin extends Fragment {
    private SwipeToLoadLayout swipeToLoadLayout;
    private MyRecyclerView swipetarget;
    private OrderListAdapter adapter;
    private SharedPreferences sp;
    //private String user_name;
    private DataManager mdataManager=new DataManager();
    int count=0;
    //全部订单
    private static List<OrderBean> listAll = new ArrayList<>();
    //待发货订单
    private static   List<OrderBean> listDisPatch = new ArrayList<>();
    //待收货订单
    private static List<OrderBean> listReceive = new ArrayList<>();
    //已完成订单
    private static List<OrderBean> listFinish = new ArrayList<>();


    public int type;
    private boolean isPrepared = false;
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1://刷新界面

                    swipeToLoadLayout.setLoadingMore(true);
                    break;
                case 2://下拉刷新
                    cleanList();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            refreshList();
                            Looper.loop();
                        }
                    }).start();
                    swipeToLoadLayout.setRefreshing(false);
                    break;
                case 3://上拉加载
                    fresh();
                    swipeToLoadLayout.setLoadingMore(false);
                    break;
            }
        }
    };
    public OrderListForAdmin() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sp=context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    //统一的Fragment构建方法
    public OrderListForAdmin newInstance(int flag) {
        Bundle args = new Bundle();
        //type代表页签，0：全部订单 1：待发货 2：待收货 3：已完成
        args.putInt("type", flag);
        OrderListForAdmin fragment = new OrderListForAdmin();
        fragment.setArguments(args);
        return fragment;
    }
    public int getType() {
        return type;
    }

    //处理订单列表数据
    public void returnList(List<OrderBean> list) {
        adapter.flush(list);
    }
    //刷新订单数据
    public  void  fresh(){
        returnList(getList(type));
    }
    //初始化数据
    public   void firstGetData(){
        fresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter=new OrderListAdapter(getContext(),2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView =inflater.inflate(R.layout.fragment_order_list, container, false);
//        sp=rootView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Bundle args=getArguments();
        if (args!=null){
            type=args.getInt("type");
        }

//            Thread t=new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Looper.prepare(); //mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
//                    refreshList();
//                    Looper.loop();
//                }
//            });
//            t.start();
//            mHandler.sendEmptyMessageDelayed(1, 5000);

        swipeToLoadLayout=rootView.findViewById(R.id.swipeToLoadLayout);
        swipetarget=rootView.findViewById(R.id.swipe_target);
//        sp=new OrderBookFragment()

//        adapter.getData(getList(type));
//        Log.e("asd", ""+type);
        adapter.setOnItemClickListener(new OrderListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, int position, OrderBean data) {
                Toast.makeText(getContext(),""+data.getId(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View v, int position) {

            }
        });//订单的点击事件

        // Inflate the layout for this fragment
        swipetarget.setAdapter(adapter);
        adapter.setButtonClickListener(new OrderListAdapter.ButtonClickListener() {
            @Override
            public void onClick(int position, final OrderBean data) {
                if (data.getOrderState()==1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                           ChangeToStart(data);
                            cleanList();
                            refreshList();
                            Message message=new Message();
                            message.what=1;
                            mhandler.sendMessage(message);
                            Looper.loop();
                        }
                    }).start();
                }else if (data.getOrderState()==2){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            ChangeToFinish(data);
                            cleanList();
                            refreshList();
                            Message message=new Message();
                            message.what=1;
                            mhandler.sendMessage(message);
                            Looper.loop();
                        }
                    }).start();
                }else if (data.getOrderState()==3){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            DeleteOrderData(data);
                            cleanList();
                            refreshList();
                            Message message=new Message();
                            message.what=1;
                            mhandler.sendMessage(message);
                            Looper.loop();
                        }
                    }).start();
                }
            }
        });//订单上的按钮的点击事件
        swipetarget.setHasFixedSize(true);
        swipetarget.setEmptyView(rootView.findViewById(R.id.layout_empty));
        swipetarget.setLayoutManager(new LinearLayoutManager(getContext()));
        swipetarget.addItemDecoration(new SpaceItemDecoration(getContext(),5));
        swipeToLoadLayout.setLoadingMore(true);
//        swipeToLoadLayout.setRefreshing(true);
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                refreshList();
                cleanList();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        refreshList();
                        Looper.loop();
                    }
                }).start();
//                fresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeToLoadLayout != null)
                            swipeToLoadLayout.setRefreshing(false);
                    }
                }, 100);
            }
        });
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//            refreshList();
                fresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeToLoadLayout != null)
                            swipeToLoadLayout.setLoadingMore(false);
                    }
                }, 500);
            }
        });
        swipeToLoadLayout.setDefaultToRefreshingScrollingDuration(500);//默认下拉刷新滚动时间
        swipeToLoadLayout.setReleaseToRefreshingScrollingDuration(500);//释放下拉刷新持续滚动的时间
        swipeToLoadLayout.setDefaultToLoadingMoreScrollingDuration(500);
        swipeToLoadLayout.setReleaseToLoadingMoreScrollingDuration(500);
        swipetarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    if (!ViewCompat.canScrollVertically(recyclerView,1)){
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
        isPrepared = true;
        return rootView;
    }
    public void DeleteOrderData(OrderBean orderBean){
        boolean f= mdataManager.DeleteOrderbyId(orderBean.getId());
        if (f){
            Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(getContext(),"删除失败",Toast.LENGTH_SHORT).show();}
    }
    public void ChangeToStart(OrderBean orderBean){
        boolean f= mdataManager.StartOrderbyId(orderBean.getId());
        if (f){
            Toast.makeText(getContext(),"成功",Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();}
    }
    public void ChangeToFinish(OrderBean orderBean){
        boolean f= mdataManager.FinishOrderbyId(orderBean.getId());
        if (f){
            Toast.makeText(getContext(),"成功",Toast.LENGTH_SHORT).show();
        }else {Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();}
    }
    public  List<OrderBean> getList(int type) {

        List<OrderBean> list = new ArrayList<>();
        switch (type) {
            case 0:
                list = listAll;
                break;
            case 1:
                list = listDisPatch;
                break;
            case 2:
                list = listReceive;
                break;
            case 3:
                list = listFinish;
                break;
        }
        return list;

    }
    public  void addList(OrderBean bean) {
        switch (bean.getOrderState()) {
            case 1:
                listDisPatch.add(bean);
                break;
            case 2:
                listReceive.add(bean);
                break;
            case 3:
                listFinish.add(bean);
                break;
        }
        listAll.add(bean);
    }
    public void cleanList(){
        listAll.clear();
        listDisPatch.clear();listReceive.clear();listFinish.clear();

    }
    public void refreshList(){
        OrderBean bean;
        List<OrderBean> list=mdataManager.getAllOrderData();
        Log.e("======================", " "+list.size());
        for (int i=0;i<list.size();i++){
            Log.e("iiiiiiiiiiiiii", ""+i);
            bean=list.get(i);
            addList(bean);
            Log.e("tag",bean.getOrderUserName()+bean.getOrderBeiZhu()+"，"+bean.getOrderUserIdentify()+","+bean.getOrderState()+","+bean.getOrderDidian());
        }
    }
}
