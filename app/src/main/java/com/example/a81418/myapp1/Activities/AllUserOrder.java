package com.example.a81418.myapp1.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.OrderBean;
import com.example.a81418.myapp1.Fragment.AllOrderList;
import com.example.a81418.myapp1.Fragment.OrderList;
import com.example.a81418.myapp1.R;
import com.example.a81418.myapp1.dialogLoadding.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class AllUserOrder extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    View include;
    private  List<OrderBean> list;
    AllOrderList o=new AllOrderList();
    private List<String> mTitles = new ArrayList<>() ;
    private DataManager mdatamanager=new DataManager();
    private Dialog mWeiboDialog;
    Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    initSelect();
                    initViewPager();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
                case 2:
                    Log.e("msg=2", "失败");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_order);
        include=findViewById(R.id.order_all_bar);
        viewPager=findViewById(R.id.order_all_viewpager);
        tabLayout=findViewById(R.id.order_all_tab_layout);
        final SharedPreferences sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mWeiboDialog=WeiboDialogUtils.createLoadingDialog(this,"加载中...");
        initLeftButton();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                o.cleanList();
                list=mdatamanager.getAllOrderData();
                if (list!=null){
                    Message msg=new Message();
                    msg.what=1;
                    mHandler.sendMessage(msg);
                }else{
                    Message msg=new Message();
                    msg.what=2;
                    mHandler.sendMessage(msg);
                }
                Looper.loop();
            }
        }).start();

    }
    public void initLeftButton (){
        View addOrderText=include.findViewById(R.id.all_order_fl_right_container);
        addOrderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
    }
    /**
     * 初始化水平选择栏
     */
    private void initSelect() {
        //设置TabLayout的模式

        //为TabLayout添加tab名称
        //需要自定义布局时，需要在绑定viewpager前加入布局
        mTitles.add("全部");
        mTitles.add("待处理");
        mTitles.add("处理中");
        mTitles.add("已完成");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTitles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(i)));
        }
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setText("待处理"));
        tabLayout.addTab(tabLayout.newTab().setText("处理中"));
        tabLayout.addTab(tabLayout.newTab().setText("已完成"));

    }
    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        final ArrayList<Fragment> fragmentList = new ArrayList<>();

        for (int i=0;i<list.size();i++){
            o.addList(list.get(i));
        }
        AllOrderList listall=o.newInstance(0);
        AllOrderList listDisPatch=o.newInstance(1);
        AllOrderList listReceive=o.newInstance(2);
        AllOrderList listFinish=o.newInstance(3);

        fragmentList.add(listall);
        fragmentList.add(listDisPatch);//
        fragmentList.add(listReceive);//
        fragmentList.add(listFinish);//
        FragmentPagerAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList, mTitles);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                ( fragmentList.get(tab.getPosition())).fresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public class MyFragmentAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragmentList;
        private List<String> mTitles;

//        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
////            super(fm);
////            this.fragmentList = fragmentList;
////        }

        public MyFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> mTitles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.mTitles = mTitles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            boolean flag=false;
            if (null==mTitles||mTitles.isEmpty()){
                flag=true;
            }
            return !flag ? mTitles.get(position) : "";
        }

        @Override
        public Fragment getItem(int position){
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
