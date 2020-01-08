package com.greasemonkey.vendor.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greasemonkey.vendor.R;

import java.util.List;


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
            meuItemViewHolder.tvUserName.setText(notificationList.get(i).getServiceType());
            meuItemViewHolder.tvServiceType.setText("Total Amount: Rs. "+notificationList.get(i).getServiceType());
            meuItemViewHolder.tvAMCType.setText(notificationList.get(i).getAmcType());
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

        public TextView tvUserName,tvServiceType,tvAMCType,tvDate,tvStatus;

        public MenuItemViewHolder(View view) {
            super(view);
            tvUserName = (TextView) view.findViewById(R.id.userName);
            tvServiceType = (TextView) view.findViewById(R.id.serviceType);
            tvAMCType = (TextView) view.findViewById(R.id.amcType);
            tvDate = (TextView) view.findViewById(R.id.requestDate);
            tvStatus = (TextView) view.findViewById(R.id.requestStatus);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
