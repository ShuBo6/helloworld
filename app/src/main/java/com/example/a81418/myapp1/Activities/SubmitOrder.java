package com.example.a81418.myapp1.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a81418.myapp1.DataManager.DataManager;
import com.example.a81418.myapp1.DataManager.OrderBean;
import com.example.a81418.myapp1.R;

import java.io.File;

public class SubmitOrder extends AppCompatActivity {
    private View include;
    private EditText didian;
    private EditText beizhu;
    private ImageView img;
    private SharedPreferences sp;
    private DataManager dataManager;
    private Bitmap image;
    File tempFile;//调用照相机返回图片文件
    private static final int ALBUM_REQUEST_CODE = 1; //相册请求码
    private static final int CAMERA_REQUEST_CODE = 2;   //相机请求码
    private static final int CROP_REQUEST_CODE = 3;  //剪裁请求码
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        switch (msg.what){
            case 1://报修成功后返回
                onBackPressed();
                break;
            case 2 ://报修失败后返回
                break;
        }
    }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager=new DataManager();
        sp=this.getSharedPreferences("userInfo",MODE_PRIVATE);
        setContentView(R.layout.activity_submit_order);
        include=findViewById(R.id.submit_header_include);
        ImageView backimg=include.findViewById(R.id.submit_order_img_left);
        TextView submitText=include.findViewById(R.id.submit_order_text_right);
        didian=findViewById(R.id.order_didian_text);
        beizhu=findViewById(R.id.order_beizhu_text);
        img=findViewById(R.id.order_img);

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChoose();
            }
        });
        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        boolean flag=dataManager.InsertOrder(initOrderData());
                        if (flag){
                            Toast.makeText(SubmitOrder.this,"订单提交成功",Toast.LENGTH_SHORT).show();
                            Message message=new Message();
                            message.what=1;
                            mhandler.sendMessage(message);
                        }else{   Toast.makeText(SubmitOrder.this,"订单提交失败",Toast.LENGTH_SHORT).show();
                        Message message=new Message();
                        message.what=2;
                        mhandler.sendMessage(message);}
                        Looper.loop();
                    }
                }).start();
            }
        });
    }
    public OrderBean initOrderData(){
        OrderBean bean=new OrderBean();
        bean.setOrderUserName(sp.getString("USER_NAME", ""));
        bean.setOrderDidian(didian.getText().toString().trim());
        bean.setOrderBeiZhu(beizhu.getText().toString().trim());
        bean.setOrderImg(image);
        bean.setOrderState(1);
        bean.setOrderUserIdentify(sp.getString("identify",""));
        return bean;
    }
    public void showDialogChoose(){//显示选择图片来的消息框
        final String[] array=new String[]{"图库","系统相机"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("请选择");
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    getPicFromAlbm();
                }else if (i==1){
                    //相机
                    getPicFromCamera();
                }
            }
        });
        builder.show();
    }
    public void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.example.a81418.myapp1", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("dasd", contentUri.toString());
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    public void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        //startActivityForResult(intent, CROP_REQUEST_CODE);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(this, "com.example.a81418.myapp1", tempFile);
                        cropPhoto(contentUri);
                    } else {

                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    cropPhoto(uri);
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //在这里获得了剪裁后的Bitmap对象，可以用于上传
                    image = bundle.getParcelable("data");
                    img.setImageBitmap(image);
                }
                break;
        }
    }
}
