package com.greasemonkey.vendor.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greasemonkey.vendor.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by dell on 10/5/2019.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MenuItemViewHolder> {
    List<NotificationModel> notificationList;
    NotificationAdapter.OnItemClickListener mItemClickListener;

    public NotificationAdapter(List<NotificationModel> notification) {
        this.notificationList = notification;
    }

    @Override
    public NotificationAdapter.MenuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_cell, viewGroup, false);
        return new NotificationAdapter.MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.MenuItemViewHolder meuItemViewHolder, int i) {

        try{
           // meuItemViewHolder.tvEstimateAmount.setText(notificationList.get(i).getServiceType());
            meuItemViewHolder.tvServiceType.setText(notificationList.get(i).getServiceType());
            meuItemViewHolder.tvStatus.setText(notificationList.get(i).getOrderStatus());
            meuItemViewHolder.tvDate.setText(notificationList.get(i).getOrderDate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            // notificationList.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<NotificationModel> getMenuItem(){
        return notificationList;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final NotificationAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMenuItem(List<NotificationModel> people) {
        this.notificationList = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvEstimateAmount,tvServiceType,tvDate,tvStatus;

        public MenuItemViewHolder(View view) {
            super(view);
            tvEstimateAmount = (TextView) view.findViewById(R.id.tvEstimateAmount);
            tvServiceType = (TextView) view.findViewById(R.id.serviceType);
            tvDate = (TextView) view.findViewById(R.id.requestDate);
            tvStatus = (TextView) view.findViewById(R.id.orderStatus);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
