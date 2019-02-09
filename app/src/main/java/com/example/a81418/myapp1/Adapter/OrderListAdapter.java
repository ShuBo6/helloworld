package com.example.a81418.myapp1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.a81418.myapp1.DataManager.OrderBean;
import com.example.a81418.myapp1.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private Context mContext;
    private onItemClickListener mOnItemClickListener;
    private ButtonClickListener mButtonClickListener;
    private List<OrderBean> mList=new ArrayList<>();
    private int type;
    public OrderListAdapter(Context activity,int type) {
        super();
        mContext = activity;
        this.type=type;
    }
    /**
     * 自定义的recyclerView点击监听
     */
    public interface onItemClickListener {
        void onItemClick(View v, int position, OrderBean data);

        void onItemLongClick(RecyclerView.ViewHolder viewHolder, View v, int position);
    }

    /**
     * 每个订单按钮的点击监听
     */
    public interface ButtonClickListener {
        void onClick(int position, OrderBean data);
    }


    public List<OrderBean> getData() {
        Log.e("size",""+ mList.size());
        return mList;
    }
    public void flush(List<OrderBean> list) {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
            mList.addAll(list);
//        if (list != null && list.size() > 0) {
//            mList.clear();
//            mList=list;
//        } else {
//            mList.clear();
//        }
        notifyDataSetChanged();
//        OrderBean b=mList.get(0);
//        Log.e("asd", b.getOrderBeiZhu() +mList.size());

    }
    public void setOnItemClickListener(OrderListAdapter.onItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setButtonClickListener(OrderListAdapter.ButtonClickListener listener) {
        mButtonClickListener = listener;

    }
      class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
         TextView tvTime;
         TextView tvState;
         ImageView OrderImg;
         TextView OrderDidian;
         TextView Orderbeizhu;
         TextView OrderTextButton;

        private ViewHolder(View itemView) {
            super(itemView);
//添加按钮点击的监听事件，以及列表的点击事件
            tvTime=itemView.findViewById(R.id.tv_time);
            tvState=itemView.findViewById(R.id.tv_state);
            OrderImg=itemView.findViewById(R.id.img_merchandise);
            OrderDidian=itemView.findViewById(R.id.tv_merchandise_name);
            Orderbeizhu=itemView.findViewById(R.id.tv_merchandise_price);
            OrderTextButton=itemView.findViewById(R.id.tv_merchandise_button);
            OrderTextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mButtonClickListener != null)
                        mButtonClickListener.onClick(getAdapterPosition(), getData().get(getAdapterPosition()));
                }
            });
        }
          @Override
          public void onClick(View v) {
              if (mOnItemClickListener != null) {
                  mOnItemClickListener.onItemClick(v, getAdapterPosition(), getData().get(getAdapterPosition()));
              } else
                  Toast.makeText(mContext, "item click pos:" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
          }

          @Override
          public boolean onLongClick(View v) {
              if (mOnItemClickListener != null) {
                  mOnItemClickListener.onItemLongClick(ViewHolder.this, v, getAdapterPosition());
              } else
                  Toast.makeText(mContext, "item long click pos:" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
              return false;
          }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_merchandise,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderBean bean=getData().get(position);
        Log.e("UserInfo", ""+bean.getOrderBeiZhu()+bean.getOrderDidian()+bean.getOrderUserIdentify() );
        holder.OrderDidian.setText(bean.getOrderDidian());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        holder.tvTime.setText(String.format(mContext.getResources().getString(R.string.str_merchandise_time), sdf.format(bean.getOrderTime())));
        showState(bean.getOrderState(),holder.tvState);
        holder.OrderImg.setImageBitmap(bean.getOrderImg());
        holder.Orderbeizhu.setText(bean.getOrderBeiZhu());
        if (type==1){
            DonotShowButton(holder.OrderTextButton);
        }if (type==0){
            showButton(bean.getOrderState(),holder.OrderTextButton);
        }if (type==2){
            OnlyShowForAdmin(bean.getOrderState(),holder.OrderTextButton);
        }


    }

//    @Override
//    public int getItemViewType(int position) {
//        switch ( )
//        return super.getItemViewType(position);
//    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
    private void showState(int state, TextView textView) {
        String text = "";
        int color = Color.BLACK;
        if (state==3) {
            text = "已完成";
            color = Color.BLACK;
        } else if (state==2) {
            text = "处理中";
            color = Color.BLACK;
        } else if (state==1) {
            text = "待处理";
            color = Color.BLACK;
        }
        textView.setText(text);
        textView.setTextColor(color);
    }
    private void OnlyShowForAdmin(int state, TextView textView) {
        String text = "";
        int visible = View.GONE;
        int color = Color.BLACK;
        if (state==3) {
            text = "删除报修";
            color = Color.BLACK;
            visible = View.VISIBLE;
        } else if (state==2) {
            text = "确认完成";
            color = Color.GREEN;
            visible = View.GONE;
        } else if (state==1) {
            text = "开始维修";
            color = Color.BLACK;
            visible = View.VISIBLE;
        }
        textView.setText(text);
        textView.setTextColor(color);
        textView.setVisibility(visible);
    }
    private void DonotShowButton(TextView textView){
        textView.setVisibility(View.GONE);
    }
    private void showButton(int state, TextView textView) {
        String text = "";
        int visible = View.GONE;
        int color = Color.BLACK;
        if (state==3) {
            text = "删除订单";
            color = Color.BLACK;
            visible = View.VISIBLE;
        } else if (state==2) {
            text = "确认完成";
            color = Color.BLACK;
            visible = View.VISIBLE;
        } else if (state==1) {
            text = "取消报修";
            color = Color.BLACK;
            visible = View.VISIBLE;
        }
        textView.setText(text);
        textView.setTextColor(color);
        textView.setVisibility(visible);
    }
}
