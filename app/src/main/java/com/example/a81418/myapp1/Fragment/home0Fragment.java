package com.example.a81418.myapp1.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.banner.CustomBanner;
import com.example.a81418.myapp1.Activities.AllUserOrder;
import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.OrderBean;
import com.example.a81418.myapp1.R;
import com.example.a81418.myapp1.dialogLoadding.WeiboDialogUtils;
import com.example.a81418.myapp1.utils.CommonUtil;
import com.example.a81418.myapp1.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class home0Fragment extends Fragment {
    AllOrderList o=new AllOrderList();
    private CustomBanner<String> mBanner;
    private View mqiandao,mwangluo,mbaoxiu,mchengji;
//    private SharedPreferences sp;
    private List<OrderBean> list;
    private LinearLayout frag_list;
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;
    private Dialog mWeiboDialog;
    //控制线程与界面
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    initlistFrag();
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    break;
                case 2:
                    Log.e("msg=2", "失败");
            }
        }
    };
    //设置普通指示器
    private void setBean(final ArrayList<Integer> beans) {
        mBanner.setPages(new CustomBanner.ViewCreator<Integer>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, View view, int position, Integer integer) {
                Glide.with(context).load(integer).into((ImageView) view);
            }

        }, beans)
//                //设置指示器为普通指示器
//                .setIndicatorStyle(CustomBanner.IndicatorStyle.ORDINARY)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setIndicatorRes(R.drawable.shape_point_select, R.drawable.shape_point_unselect)
//                //设置指示器的方向
//                .setIndicatorGravity(CustomBanner.IndicatorGravity.CENTER)
//                //设置指示器的指示点间隔
//                .setIndicatorInterval(20)
                //设置自动翻页
                .startTurning(5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        o.cleanList();
        mWeiboDialog= WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                DataManager dataManager=new DataManager();
                list=dataManager.getAllOrderData();
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home0, container, false);
    }
    public void initlistFrag(){
        frag_list=getActivity().findViewById(R.id.home_frag_list);
        for (int i=0;i<list.size();i++){
            o.addList(list.get(i));
        }
        AllOrderList listall=o.newInstance(0);
        getFragmentManager().beginTransaction().replace(R.id.home_frag_list,listall).show(listall).commitAllowingStateLoss();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        sp=getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        mBanner = getActivity().findViewById(R.id.banner);
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.wlztssb);
        images.add(R.drawable.kscjzzd);
        images.add(R.drawable.ktqdyxz);
        images.add(R.drawable.bxwxzfb);
        //images.add(R.mipmap.cjtu);
        setBean(images);
        mbaoxiu = getActivity().findViewById(R.id.baoxiu);
        mbaoxiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AllUserOrder.class));
            }
        });
        mchengji = getActivity().findViewById(R.id.chengji);
        mchengji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "登陆之后才能使用该功能哦~", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse("http://123.232.97.56/default2.aspx");//此处填学舌链接
//                intent.setData(content_url);
//                startActivity(intent);
            }
        });
        mwangluo = getActivity().findViewById(R.id.shouye);
        mwangluo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.jnvc.cn/");//此处填学舌链接
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        mqiandao= getActivity().findViewById(R.id.qiandao);
        mqiandao.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getContext(),"登陆之后才能使用该功能哦~",Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //扫描结果回调
//        if (resultCode == 0xA1) { //RESULT_OK = -1
//            Bundle bundle = data.getExtras();
//            String scanResult = bundle.getString("qr_scan_result");
//            //将扫描出的信息显示出来
//            if (scanResult.equals("wangkehan")){
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        DataManager manager=new DataManager();
//                        boolean flag=manager.Studentqiandao(sp.getString("USER_NAME", "").trim());
//                        Looper.prepare();
//                        if (flag){
//                            Toast.makeText(getContext(),"签到成功",Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(getContext(),"签到失败",Toast.LENGTH_SHORT).show();
//                        }
//                        Looper.loop();
//                    }
//                }).start();
//            }
//        }
//    }
}
