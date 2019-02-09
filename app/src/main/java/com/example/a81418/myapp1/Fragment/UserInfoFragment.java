package com.example.a81418.myapp1.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.Activities.DrawerMenu;
import com.example.a81418.myapp1.Activities.MainActivity;
import com.example.a81418.myapp1.Activities.SetUserInfo;
import com.example.a81418.myapp1.Activities.SocketClient;
import com.example.a81418.myapp1.DataManager.ChangeFragmentFromActivity;
import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.UserData;
import com.example.a81418.myapp1.R;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

public class UserInfoFragment extends Fragment {
    private DataManager mdatamanager = new DataManager();
    private SharedPreferences sp;
    private static final int ALBUM_REQUEST_CODE = 1; //相册请求码
    private static final int CAMERA_REQUEST_CODE = 2;   //相机请求码
    private static final int CROP_REQUEST_CODE = 3;  //剪裁请求码
    File tempFile;//调用照相机返回图片文件
    private TextView userNike;
    private ImageView head_img;
    private Button back_to_login;
    private boolean isLogin;
    private ChangeFragmentFromActivity changeFragmentFromActivity;
    private UserData userData = new UserData();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    public UserInfoFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        changeFragmentFromActivity=(ChangeFragmentFromActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        isLogin=sp.getBoolean("ISLOGIN",false );
        head_img = getActivity().findViewById(R.id.head_img_userfrag);
        back_to_login = getActivity().findViewById(R.id.exit_login);
        userNike = getActivity().findViewById(R.id.user_nike);
        swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_layout);
        Log.e("islogin", ""+isLogin+sp.getString("USER_NAME","")+sp.getString("identify",""));
        if (isLogin) {
            String ImgStringStream = sp.getString("imgStringStream", "");
            if (!ImgStringStream.equals("")) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(ImgStringStream, Base64.DEFAULT));
                Bitmap bitmap = toRoundBitmap(BitmapFactory.decodeStream(byteArrayInputStream));
                head_img.setImageBitmap(bitmap);
            }
            head_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogChoose();
                }
            });

            String s = sp.getString("USER_NICK", "");
            if (!s.equals("")) {
                userNike.setText(s);
            } else {
                userNike.setText(sp.getString("USER_NAME", ""));
            }
            back_to_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            if (sp.getString("identify","").equals("学生")){
            String ListData[] = {"个人资料", "我的报修"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, ListData);
            ListView listView = getActivity().findViewById(R.id.user_info_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        switch (position) {
                            case 0://跳转个人资料设置页面
                                Intent toSetUserInfo = new Intent(getActivity(), SetUserInfo.class);
                                startActivity(toSetUserInfo);
                                break;
                            case 1:
                                changeFragmentFromActivity.ChangeFragment();
                                break;

                        }
                }
            });
            }else if(sp.getString("identify","").equals("教工")){
                    String ListData[] = {"个人资料", "我的报修","在线语音报修"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, ListData);
                    ListView listView = getActivity().findViewById(R.id.user_info_list);
                    listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        switch (position) {
                            case 0://跳转个人资料设置页面
                                Intent toSetUserInfo = new Intent(getActivity(), SetUserInfo.class);
                                startActivity(toSetUserInfo);
                                break;
                            case 1:
                                changeFragmentFromActivity.ChangeFragment();
                                break;
                            case 2:
                                call("17854008850");
                                break;
                        }
                    }
                });
            }else if (sp.getString("identify","").equals("管理员")){
                String ListData[] = {"个人资料", "报修信息","网络数据监测"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, ListData);
                ListView listView = getActivity().findViewById(R.id.user_info_list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        switch (position) {
                            case 0://跳转个人资料设置页面
                                Intent toSetUserInfo = new Intent(getActivity(), SetUserInfo.class);
                                startActivity(toSetUserInfo);
                                break;
                            case 1:
                                changeFragmentFromActivity.ChangeFragment();
                                break;
                            case 2:
                              //网络数据监测
                                Intent intent=new Intent(getActivity(), SocketClient.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }


            swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                @Override
                public void onRefresh() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            userData = mdatamanager.InitUserData(sp.getString("USER_NAME", ""));
                            if (userData!=null){
                                Message msg=new Message();
                                msg.what=1;
                                mHandler.sendMessage(msg);
                            }
                            SharedPreferences.Editor editor = sp.edit();
                            Bitmap getHeadImg = userData.getUserHeadImg();
                            if (getHeadImg != null) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                getHeadImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                String Imgstring = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                                editor.putString("imgStringStream", Imgstring);
                                //head_img.setImageBitmap(getHeadImg);

                            }
                            if (userData.getUserNick() != null) {
                                editor.putString("USER_NICK", userData.getUserNick());
                            }
                            editor.commit();
//                            Intent refresh = new Intent(getActivity(), DrawerMenu.class);
//                            startActivity(refresh);
//                            getActivity().finish();
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
        } else{
            head_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            back_to_login.setText("请登录");
            back_to_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            userNike.setText("未登录");
            head_img.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.null_head_img));
            String ListData[] = {"个人资料", "我的报修"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, ListData);
            ListView listView = getActivity().findViewById(R.id.user_info_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0://跳转个人资料设置页面
                            Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                       Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                            break;

                    }
                }
            });
//            swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 2000);
//                }
//
//                @Override
//                public void onPullDistance(int distance) {
//
//                }
//
//                @Override
//                public void onPullEnable(boolean enable) {
//
//                }
//            });
    }

}
//        public  void changeFragmentToBaoxiu(){
//            OrderBookFragment orderBookFragment=new OrderBookFragment();
//            getActivity().getSupportFragmentManager().beginTransaction().hide(this).add(R.id.frag_view,
//                    orderBookFragment).show(orderBookFragment).commit();
//            BottomNavigationView bottomNavigationView=getActivity().findViewById(R.id.navigation);
//            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(1).getItemId());
//        }
        public void showDialogChoose () {//显示选择图片来的消息框
            final String[] array = new String[]{"图库", "系统相机"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("请选择");
            builder.setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i == 0) {
                        getPicFromAlbm();
                    } else if (i == 1) {
                        //相机
                        getPicFromCamera();
                    }
                }
            });
            builder.show();
        }
        public void getPicFromCamera () {
            //用于保存调用相机拍照后所生成的文件
            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
            //跳转到调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.a81418.myapp1", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                Log.e("dasd", contentUri.toString());
            } else {    //否则使用Uri.fromFile(file)方法获取Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            }
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        public void getPicFromAlbm () {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
        }


        public static Bitmap toRoundBitmap (Bitmap bitmap){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
        }

        @Override public void onActivityResult ( int requestCode, int resultCode, Intent intent){
            super.onActivityResult(requestCode, resultCode, intent);
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:   //调用相机后返回
                    if (resultCode == RESULT_OK) {
                        //用相机返回的照片去调用剪裁也需要对Uri进行处理
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.example.a81418.myapp1", tempFile);
                            cropPhoto(contentUri);
                        } else {

                            cropPhoto(Uri.fromFile(tempFile));
                        }
                    }
                    break;
                case ALBUM_REQUEST_CODE:    //调用相册后返回
                    if (resultCode == RESULT_OK) {
                        Uri uri = intent.getData();
                        cropPhoto(uri);
                    }
                    break;
//            case CROP_REQUEST_CODE:     //调用剪裁后返回
//                Bundle bundle = intent.getExtras();
//                if (bundle != null) {
//                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
//
//                     final Bitmap image = bundle.getParcelable("data");
//                     Bitmap toRound=toRoundBitmap(image);
//                    head_img.setImageBitmap(toRound);
//                }
//                break;
            }
        }
        public void cropPhoto (Uri uri){
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            intent.putExtra("outputX", 100);
            intent.putExtra("outputY", 100);
            intent.putExtra("return-data", true);

            //startActivityForResult(intent, CROP_REQUEST_CODE);
            getActivity().startActivityForResult(intent, CROP_REQUEST_CODE);
        }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
