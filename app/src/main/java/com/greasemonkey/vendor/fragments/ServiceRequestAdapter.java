package com.greasemonkey.vendor.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.greasemonkey.vendor.R;

import java.util.List;

/**
 * Created by dell on 12/15/2019.
 */

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.MenuItemViewHolder> {

    List<ServiceRequestModel> serviceRequestList;
    ServiceRequestAdapter.OnItemClickListener mItemClickListener;

    public ServiceRequestAdapter(List<ServiceRequestModel> notification) {
        this.serviceRequestList = notification;
    }

    @Override
    public ServiceRequestAdapter.MenuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.service_request_cell, viewGroup, false);
        return new ServiceRequestAdapter.MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ServiceRequestAdapter.MenuItemViewHolder meuItemViewHolder, int i) {

        try{
            meuItemViewHolder.tvOrderId.setText(serviceRequestList.get(i).getGmOrderId());
            meuItemViewHolder.tvServiceType.setText(serviceRequestList.get(i).getServiceType());
            meuItemViewHolder.tvStatus.setText(serviceRequestList.get(i).getOrderStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            // serviceRequestList.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<ServiceRequestModel> getMenuItem(){
        return serviceRequestList;
    }

    @Override
    public int getItemCount() {
        return serviceRequestList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final ServiceRequestAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMenuItem(List<ServiceRequestModel> people) {
        this.serviceRequestList = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvOrderId,tvServiceType,tvStatus;

        public MenuItemViewHolder(View view) {
            super(view);
            tvOrderId = (TextView) view.findViewById(R.id.tvOrderId);
            tvServiceType = (TextView) view.findViewById(R.id.tvServiceType);
            tvStatus = (TextView) view.findViewById(R.id.tvRequestStatus);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
