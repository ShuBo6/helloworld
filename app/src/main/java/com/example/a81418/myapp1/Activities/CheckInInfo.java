package com.example.a81418.myapp1.Activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.example.a81418.myapp1.DataManager.CheckInMingDan;
import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.R;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckInInfo extends AppCompatActivity {
    private SuperSwipeRefreshLayout swipeToLoadLayout;
   private ListView mingdanview;
   private List<CheckInMingDan> mingdan;
private Handler mHandler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        mingdanview=findViewById(R.id.mingdan_list);
        switch (msg.what){
            case 1:
                swipeToLoadLayout.setRefreshing(false);
                List<String> wenzi=new ArrayList<String>();
                for (int i=0;i<mingdan.size();i++){
                    Log.e("=======+-+-+-", ""+mingdan.get(i).getName());
//                    Log.e("/////////////", ""+mingdan.get(i).getName());
                    wenzi.add(mingdan.get(i).getName()+"    "+translate(mingdan.get(i).getState()));
            }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CheckInInfo.this,R.layout.support_simple_spinner_dropdown_item,wenzi);
                mingdanview.setAdapter(arrayAdapter);
                break;
            case 2:
                Toast.makeText(getApplication(),"请检查网络连接",Toast.LENGTH_SHORT).show();
        }
    }
};

    public String translate(int state){
        String yes="已签到";
        String no="未签到";
        if (state==0){return no;}
        else{return yes;}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_info);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                DataManager dataManager=new DataManager();
                mingdan=dataManager.getMingdan();
                if (mingdan!=null){
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

        swipeToLoadLayout=findViewById(R.id.mingdan_refresh);
        swipeToLoadLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        DataManager dataManager=new DataManager();
                        mingdan=dataManager.getMingdan();
                        if (mingdan!=null){
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

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });


//                (new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Looper.prepare();
//                        DataManager dataManager=new DataManager();
//                        mingdan=dataManager.getMingdan();
//                        if (mingdan!=null){
//                            Message msg=new Message();
//                            msg.what=1;
//                            mHandler.sendMessage(msg);
//                        }else{
//                            Message msg=new Message();
//                            msg.what=2;
//                            mHandler.sendMessage(msg);
//                        }
//                        Looper.loop();
//                    }
//                }).start();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (swipeToLoadLayout != null)
//                            swipeToLoadLayout.setRefreshing(false);
//                    }
//                }, 100);
//            }
//        });
    }
}
