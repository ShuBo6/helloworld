package com.example.a81418.myapp1.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.a81418.myapp1.R;

import java.util.ArrayList;
import java.util.List;


public class notifyFragment extends Fragment {
//    private TextView TopBarTitle;
    private TextView TopBarAddOrder;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> mTabTitles =new ArrayList<String>();

    public notifyFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TopBarTitle=getActivity().findViewById(R.id.baoxiu_topbar_title);

    }
    private void initView() {
        mTabTitles.add("1111");
        mTabTitles.add("2222");
        mTabTitles.add("3333");
        mTabTitles.add("4444");
//        mTabTitles[2] = "科技";
//        mTabTitles[3] = "体育";
//        mTabTitles[4] = "健康";

//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式

       for (int i=0;i<mTabTitles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mTabTitles.get(i)));//添加tab选项
        }
//
        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);
        fragmentList.add(BlankFragment.newInstance(0));
        fragmentList.add(BlankFragment.newInstance(1));
        fragmentList.add(BlankFragment.newInstance(2));
        fragmentList.add(BlankFragment.newInstance(3));
//        mFragmentArrays[2] = new BlankFragment();
//        mFragmentArrays[3] = new BlankFragment();
//        mFragmentArrays[4] = new BlankFragment();
      MyPagerAdapter adapter=new MyPagerAdapter(getChildFragmentManager(),fragmentList,mTabTitles);

        mViewPager.setAdapter(adapter);
        //将ViewPager和TabLayout绑定
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> mfragmentList;
        private List<String> titles;

        public MyPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,List<String> titles) {
            super(fm);
            this.mfragmentList = fragmentList;
            this.titles=titles;
        }

        @Override
        public Fragment getItem(int position) {

            return mfragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mfragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notify,container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TopBarAddOrder=getActivity().findViewById(R.id.add_order_text);
        mTabLayout=getActivity().findViewById(R.id.notify_tablayout);
        mViewPager=getActivity().findViewById(R.id.notity_viewpager);
        initView();
    }
}
