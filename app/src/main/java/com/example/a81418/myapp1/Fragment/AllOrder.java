//package com.example.a81418.myapp1.Fragment;
//
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
//import com.example.a81418.myapp1.Adapter.OrderListAdapter;
//import com.example.a81418.myapp1.DataManager.DataManager;
//import com.example.a81418.myapp1.DataManager.OrderBean;
//import com.example.a81418.myapp1.R;
//import com.example.a81418.myapp1.view.recyclerview.MyRecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AllOrder extends Fragment {
//    private SwipeToLoadLayout swipeToLoadLayout;
//    private MyRecyclerView swipetarget;
//    private OrderListAdapter adapter;
//    private SharedPreferences sp;
//    private String user_name;
//    private boolean islogin;
//    private DataManager mdataManager=new DataManager();
//    //全部订单
//    private static List<OrderBean> listAll = new ArrayList<>();
//    //待发货订单
//    private static   List<OrderBean> listDisPatch = new ArrayList<>();
//    //待收货订单
//    private static List<OrderBean> listReceive = new ArrayList<>();
//    //已完成订单
//    private static List<OrderBean> listFinish = new ArrayList<>();
//
//
//    public int type;
//
//    public AllOrder() {
//    }
//    //统一的Fragment构建方法
//    public  OrderList newInstance(int flag) {
//        Bundle args = new Bundle();
//        //type代表页签，0：全部订单 1：待发货 2：待收货 3：已完成
//        args.putInt("type", flag);
//        OrderList fragment = new OrderList();
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public int getType() {
//        return type;
//    }
//
//    public void returnList(List<OrderBean> list) {
//        adapter.flush(list);
////        adapter.getData(list);
//    }
//    //刷新订单数据
////    public void  fresh(){
////        returnList(getList(type));
////    }
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        adapter=new OrderListAdapter(getContext());
//
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView =inflater.inflate(R.layout.fragment_all_order, container, false);
//        Bundle args=getArguments();
//        if (args!=null){
//            type=args.getInt("type");
//        }
//
//        swipeToLoadLayout=rootView.findViewById(R.id.swipeToLoadLayout);
//        swipetarget=rootView.findViewById(R.id.swipe_target);
//
//        adapter.setOnItemClickListener(new OrderListAdapter.onItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position, OrderBean data) {
//                Toast.makeText(getContext(),""+data.getId(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View v, int position) {
//
//            }
//        });//订单的点击事件
//        return rootView;
//    }
//
//}
