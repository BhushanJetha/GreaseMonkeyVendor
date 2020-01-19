package com.greasemonkey.vendor.request_detail;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greasemonkey.vendor.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Bhushan
 */
public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MenuItemViewHolder> {

    List<RequestModel> menuItemModelList;
    OnItemClickListener mItemClickListener;

    public RequestListAdapter(List<RequestModel> persons) {
        this.menuItemModelList = persons;
    }

    @Override
    public MenuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.service_detail_cell, viewGroup, false);
        return new MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MenuItemViewHolder meuItemViewHolder, int i) {

        try{
            meuItemViewHolder.tvServiceNumber.setText(menuItemModelList.get(i).getRequestNumber());
            meuItemViewHolder.tvServiceType.setText(menuItemModelList.get(i).getRequestType());
            meuItemViewHolder.tvETD.setText(menuItemModelList.get(i).getEstimateDateTime());
            meuItemViewHolder.tvPickupStatus.setText(menuItemModelList.get(i).getPickupStatus());
            meuItemViewHolder.tvPaymetStatus.setText(menuItemModelList.get(i).getPaymentStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
           // menuItemModelList.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<RequestModel> getMenuItem(){
        return menuItemModelList;
    }

    @Override
    public int getItemCount() {
        return menuItemModelList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMenuItem(List<RequestModel> people) {
        this.menuItemModelList = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvServiceNumber,tvServiceType,tvETD,tvPickupStatus,tvPaymetStatus;

        public MenuItemViewHolder(View view) {
            super(view);
            tvServiceNumber = (TextView) view.findViewById(R.id.serviceNumber);
            tvServiceType = (TextView) view.findViewById(R.id.serviceType);
            tvETD = (TextView) view.findViewById(R.id.etd);
            tvPickupStatus = (TextView) view.findViewById(R.id.pickupStatus);
            tvPaymetStatus = (TextView) view.findViewById(R.id.paymentStatus);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
