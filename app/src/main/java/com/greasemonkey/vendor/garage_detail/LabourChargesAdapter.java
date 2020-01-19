package com.greasemonkey.vendor.garage_detail;


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

public class LabourChargesAdapter extends RecyclerView.Adapter<LabourChargesAdapter.MenuItemViewHolder> {
    List<LabourChargesModel> notificationList;
    LabourChargesAdapter.OnItemClickListener mItemClickListener;

    public LabourChargesAdapter(List<LabourChargesModel> notification) {
        this.notificationList = notification;
    }

    @Override
    public LabourChargesAdapter.MenuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.labour_charges_cell, viewGroup, false);
        return new LabourChargesAdapter.MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LabourChargesAdapter.MenuItemViewHolder meuItemViewHolder, int i) {

        try{
            meuItemViewHolder.tvServiceName.setText(notificationList.get(i).getServiceName());
            meuItemViewHolder.tvServicePrice.setText(notificationList.get(i).getBikecc1());
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

    List<LabourChargesModel> getMenuItem(){
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

    public void setOnItemClickListener(final LabourChargesAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMenuItem(List<LabourChargesModel> people) {
        this.notificationList = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvServiceName,tvServicePrice;

        public MenuItemViewHolder(View view) {
            super(view);
            tvServiceName = view.findViewById(R.id.tvServiceName);
            tvServicePrice = view.findViewById(R.id.tvServicePrice);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
