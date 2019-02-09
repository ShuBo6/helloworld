package com.example.a81418.myapp1.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a81418.myapp1.R;
import com.example.a81418.myapp1.zxing.encoding.EncodingHandler;
import com.google.zxing.WriterException;

public class TeacherFaqiqiandao extends AppCompatActivity {
    View include;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher_faqiqiandao);
        include=findViewById(R.id.faqiqiandao_headbar);
        initHead();
        Button xiangqing_btn=findViewById(R.id.btn_qiandaoxiangqing);
        xiangqing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherFaqiqiandao.this,CheckInInfo.class));
            }
        });
        ImageView qrImg=findViewById(R.id.qr_qiandao_img);
        String str="wangkehan";
        try {
            Bitmap bitmap= EncodingHandler.createQRCode(str,500);
            qrImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public void initHead(){
        TextView center=include.findViewById(R.id.all_order_head_title);
        center.setText("发起签到");
        View imgBack=include.findViewById(R.id.all_order_fl_right_container);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
