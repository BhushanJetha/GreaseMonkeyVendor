package com.greasemonkey.vendor.fragments;

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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MenuItemViewHolder> {
    List<HistoryModel> historyList;
    HistoryAdapter.OnItemClickListener mItemClickListener;

    public HistoryAdapter(List<HistoryModel> notification) {
        this.historyList = notification;
    }

    @Override
    public HistoryAdapter.MenuItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_cell, viewGroup, false);
        return new HistoryAdapter.MenuItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MenuItemViewHolder meuItemViewHolder, int i) {

        try{
            meuItemViewHolder.tvServiceType.setText(historyList.get(i).getServiceType());
            meuItemViewHolder.tvTotalAmount.setText("Rs. "+historyList.get(i).getAmcType());
            meuItemViewHolder.tvStatus.setText(historyList.get(i).getOrderStatus());
            meuItemViewHolder.tvDate.setText(historyList.get(i).getOrderDate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setItemSelected(int position, boolean isSelected) {
        if (position != -1) {
            // historyList.get(position).setSelected(isSelected);
            notifyDataSetChanged();
        }
    }

    List<HistoryModel> getMenuItem(){
        return historyList;
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final HistoryAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setMenuItem(List<HistoryModel> people) {
        this.historyList = people;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView tvServiceType,tvTotalAmount,tvDate,tvStatus;

        public MenuItemViewHolder(View view) {
            super(view);
            tvServiceType = (TextView) view.findViewById(R.id.tvServiceType);
            tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
            tvDate = (TextView) view.findViewById(R.id.requestDate);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
