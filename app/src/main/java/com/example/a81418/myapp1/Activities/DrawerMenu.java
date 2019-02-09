package com.example.a81418.myapp1.Activities;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.ChangeFragmentFromActivity;
import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.UserData;
import com.example.a81418.myapp1.Fragment.OrderBookFragment;
import com.example.a81418.myapp1.Fragment.OrderListForAdminFragment;
import com.example.a81418.myapp1.Fragment.UserInfoFragment;
import com.example.a81418.myapp1.Fragment.home0Fragment;
import com.example.a81418.myapp1.Fragment.home2Fragment;
import com.example.a81418.myapp1.Fragment.home3Fragment;
import com.example.a81418.myapp1.Fragment.homeFragment;
import com.example.a81418.myapp1.R;
import com.example.a81418.myapp1.dialogLoadding.WeiboDialogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class DrawerMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,ChangeFragmentFromActivity{

    private Fragment[] fragments;
    private String Identify;
    private DataManager mdatamanager =new DataManager();
//    private UserData userData=new UserData();
    private int lastfragment;//用于记录上个选择的Fragment
    private int index=0;//记录当前选择的fragment
    private SharedPreferences sp;
    private ImageView head_img;
    private TextView nav_UserName;
    private NavigationView navigationView;
    private View DrawerHeadview;
    private BottomNavigationView bottomNavigationView;

    //与服务器进行socket
    private String Ip;
    private Socket s = null;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private boolean cont = false;
    //通知栏
    private NotificationManager nm=null;
    static final int NOTIFICATION_ID =0x123;
    SharedPreferences.Editor editor;
    Handler mHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    editor.putBoolean("isconnected",true);
                    editor.apply();
                    break;
                case 2:
                    editor.putBoolean("isconnected",false);editor.apply();
                    Toast.makeText(getApplication(),"校园网络出现故障",Toast.LENGTH_SHORT).show();
                    sendNotify();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_menu);
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Ip="118.24.10.164";
        sp=this.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        editor=sp.edit();
        Identify=sp.getString("identify","");
        if (Identify.equals("管理员")){
                Timer timer = new Timer();
                        //前一次执行程序结束后 2000ms 后开始执行下一次程序
                timer.schedule(new TimerTask() {
                  @Override
                  public void run() {
                      Looper.prepare();
                      Log.e("网络监控定时任务", " "+this.scheduledExecutionTime() );
                      connect();

                      if (s!=null){
                          Message message=new Message();
                          message.what=1;
                          mHandler.sendMessage(message);
//                          Recvmessage();
                          sendmessage();
                      }else {

                          Message message=new Message();
                          message.what=2;
                          mHandler.sendMessage(message);

                      }
                      Looper.loop();

                                     }
              }, 0,30000);

//            SharedPreferences.Editor editor=sp.edit();
//            if (s!=null){
//                Recvmessage();
//                sendmessage();
//                editor.putBoolean("isconnected",true);
//            }else {
//                editor.putBoolean("isconnected",false);
//                Toast.makeText(getApplication(),"校园网络出现故障",Toast.LENGTH_SHORT).show();
//                sendNotify();
//            }
//            editor.apply();
        }
        initFragment();
        initDrawer();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
//网络异常时发送顶部通知
public void sendNotify(){
    Intent intent=new Intent(DrawerMenu.this,SocketClient.class);
    PendingIntent pendingIntent=PendingIntent.getActivity(DrawerMenu.this,0,intent,0);
    Notification notify= null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        notify = new Notification.Builder(getApplication())
                .setAutoCancel(true)
                .setTicker("校园网络出现异常")
                .setContentTitle("校园网络失去连接")
                .setContentText("校园网络可能出现异常或无连接，请尽快维护！！！")
                .setSmallIcon(R.mipmap.app_icon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .build();
    }
    nm.notify(NOTIFICATION_ID,notify);

}
    //连接
    public void connect() {

        try {
            s = new Socket("118.24.10.164", 8888);// 注意不要定义成Socket
            // s,这就成了局部变量而不是成员变量了
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            cont = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //接收服务器发来的消息
    public void  Recvmessage () {
        while (cont) {
            String str = null;
            try {
                str = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 发送消息给服务器
    public void sendmessage() {
        String str = "客户端登录";
        try {
            dos.writeUTF(str);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void initDrawer(){
        navigationView = findViewById(R.id.nav_view);
        DrawerHeadview=navigationView.inflateHeaderView(R.layout.nav_header_drawer_menu);
        nav_UserName=DrawerHeadview.findViewById(R.id.nav_user_name);

//        TextView nav_UserName;
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        View DrawerHeadview=navigationView.inflateHeaderView(R.layout.nav_header_drawer_menu);
//
//        nav_UserName=DrawerHeadview.findViewById(R.id.nav_user_name);
        String userNick=sp.getString("USER_NICK","");
        if (!userNick.equals("")){
            nav_UserName.setText(userNick);
        }else{
            nav_UserName.setText(sp.getString("USER_NAME", ""));
        }



        head_img=DrawerHeadview.findViewById(R.id.head_img);
        String ImgStringStream=sp.getString("imgStringStream","");
        if(!ImgStringStream.equals("")){
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(Base64.decode(ImgStringStream, Base64.DEFAULT));
            Bitmap bitmap=UserInfoFragment.toRoundBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
            head_img.setImageBitmap(bitmap);
        }
//        head_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //
//            }
//        });
    }


//    public static String getImagePath(Context context, Uri uri, String selection) {
//        String path = null;
//        //通过Uri和selection来获取真实的图片路径
//        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            }
//            cursor.close();
//        }
//        return path;
//    }


    public void initFragment(){
        UserInfoFragment userInfoFragment=new UserInfoFragment();
//        notifyFragment notifyFragment=new notifyFragment();
        Fragment homeFragment=null;
        Fragment orderBookFragment=null;

        if (Identify.equals("管理员")){
            orderBookFragment=new OrderListForAdminFragment();
            homeFragment=new homeFragment();
        }else if (Identify.equals("学生")){
            orderBookFragment=new OrderBookFragment();
            homeFragment=new home2Fragment();
            }else if (Identify.equals("教工")){
            orderBookFragment=new OrderBookFragment();
            homeFragment=new home3Fragment();
        }else if (!sp.getBoolean("ISLOGIN",false)){homeFragment=new home0Fragment();
        orderBookFragment=new OrderBookFragment();}


        fragments= new Fragment[]{homeFragment,orderBookFragment,userInfoFragment};
        lastfragment=0;

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_view,
                homeFragment).show(homeFragment).commit();
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.xiaoyuanshouye:
                {

                    if(lastfragment!=0)
                    {
                        index=lastfragment;
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.baoxiudongtai:
                {
                    if(lastfragment!=1)
                    {
                        index=lastfragment;
                        switchFragment(lastfragment,1);
                        lastfragment=1;

                    }
                    return true;
                }
                case R.id.gerenzhongxin:
                {
                    if(lastfragment!=2)
                    {
                        index=lastfragment;
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
                }
            }
            return false;
        }
    };
    //切换Fragment
    private void switchFragment(int lastfragment,int indexx)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();

        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(!fragments[indexx].isAdded())
        {
            transaction.add(R.id.frag_view,fragments[indexx]);
        }
//        transaction.addToBackStack(null);
        transaction.show(fragments[indexx]).commitAllowingStateLoss();
    }
    private void backFragment(){
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(index).getItemId());
//        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
//        transaction.remove(fragments[lastfragment]);//移除当前Fragment
//        if(!fragments[index].isAdded())
//        {
//            transaction.add(R.id.frag_view,fragments[index]);
//        }
//        transaction.show(fragments[index]).commitAllowingStateLoss();
        getFragmentManager().popBackStack();
        index=lastfragment;
//

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)&&keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        if(index!=lastfragment&&keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
            backFragment();
             return true;
        }return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确认退出？").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentToHome = new Intent(Intent.ACTION_MAIN, null);
                    intentToHome.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intentToHome);
                }
            }).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
/**
* 下边是抽屉内的选项
*
* */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer =findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==3){
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传

                    final Bitmap image = bundle.getParcelable("data");
                    Bitmap toRound=UserInfoFragment.toRoundBitmap(image);
                    head_img.setImageBitmap(toRound);
                    ImageView HeadImgUserFrag=findViewById(R.id.head_img_userfrag);
                    HeadImgUserFrag.setImageBitmap(toRound);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            saveHeadImg(image);
                            Looper.loop();
                        }
                    }).start();
                }
            }
        }
    }
    public void saveHeadImg(Bitmap bitmap){
        boolean flag=mdatamanager.SetUserImg(sp.getString("USER_NAME", ""),bitmap);
        Bitmap RoundCrop=UserInfoFragment.toRoundBitmap(bitmap);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        RoundCrop.compress(Bitmap.CompressFormat.PNG,100,baos);
        String imgString=new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        SharedPreferences.Editor e=sp.edit();
        e.putString("imgStringStream",imgString);
        e.commit();
        if (flag){ Toast.makeText(this,"保存头像成功",Toast.LENGTH_SHORT).show(); }
        else{Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();}
        //
    }


    @Override
    public void ChangeFragment() {
            index=lastfragment;
            switchFragment(lastfragment,1);
            lastfragment=1;
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(1).getItemId());
    }

    @Override
    public void ChangetoUserInfoFrag() {
        index=lastfragment;
        switchFragment(lastfragment,2);
        lastfragment=2;
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(2).getItemId());
    }
}
