package com.sproboticworks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sproboticworks.R;
import com.sproboticworks.model.OrderHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    final ArrayList<OrderHistoryModel.Datum> orderHistoryModels;
    final Context context;

    public OrderHistoryAdapter(ArrayList<OrderHistoryModel.Datum> orderHistoryModels, Context context) {
        this.orderHistoryModels = orderHistoryModels;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_order_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistoryModel.Datum selectedItem = orderHistoryModels.get(position);
        OrderHistoryListAdapter orderHistoryListAdapter = new OrderHistoryListAdapter(selectedItem.getOrderItemArray(), context);
        holder.recyclerView.setAdapter(orderHistoryListAdapter);
        holder.textView.setText(selectedItem.getOrderId().toString());
    }

    @Override
    public int getItemCount() {
        return orderHistoryModels.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.orderHistoryRecyclerview);
            textView = itemView.findViewById(R.id.orderHistoryOrderNo);
        }
    }
}


class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.OrderHistoryListViewHolder> {

    final List<OrderHistoryModel.OrderItemArray> orderItemArrayArrayList;
    final Context context;

    public OrderHistoryListAdapter(List<OrderHistoryModel.OrderItemArray> orderItemArrayArrayList, Context context) {
        this.orderItemArrayArrayList = orderItemArrayArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHistoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryListViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_order_history_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryListViewHolder holder, int position) {
        OrderHistoryModel.OrderItemArray selectedItem = orderItemArrayArrayList.get(position);
        holder.orderHistoryListName.setText(selectedItem.getItemName() == null ? "No Name" : "" + selectedItem.getItemName());
        holder.orderHistoryListQuantity.setText("Quantity: "+selectedItem.getQuantity().toString());
        holder.orderHistoryListPrice.setText("Price: "+selectedItem.getPrice().toString());
        holder.orderHistoryListSubTotal.setText("Subtotal: "+ selectedItem.getSubTotal().toString());
        holder.orderHistoryListNetTotal.setText("NetTotal: "+selectedItem.getNetTotal().toString());
    }

    @Override
    public int getItemCount() {
        return orderItemArrayArrayList.size();
    }

    class OrderHistoryListViewHolder extends RecyclerView.ViewHolder {
        TextView orderHistoryListName, orderHistoryListOrderStatus, orderHistoryListQuantity, orderHistoryListPrice, orderHistoryListSubTotal, orderHistoryListNetTotal;

        public OrderHistoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            orderHistoryListName = itemView.findViewById(R.id.orderHistoryListName);
            orderHistoryListOrderStatus = itemView.findViewById(R.id.orderHistoryListOrderStatus);
            orderHistoryListQuantity = itemView.findViewById(R.id.orderHistoryListQuantity);
            orderHistoryListPrice = itemView.findViewById(R.id.orderHistoryListPrice);
            orderHistoryListSubTotal = itemView.findViewById(R.id.orderHistoryListSubTotal);
            orderHistoryListNetTotal = itemView.findViewById(R.id.orderHistoryListNetTotal);
        }
    }
}