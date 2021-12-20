package com.sproboticworks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sproboticworks.R;
import com.sproboticworks.model.OrderHistoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        holder.textView.setText(selectedItem.getOrderNo());
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM, yyyy");
        Date d = null;
        try {
            d = input.parse(selectedItem.getOrderDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        holder.orderHistoryDate.setText(formatted);


    }

    @Override
    public int getItemCount() {
        return orderHistoryModels.size();
    }

    class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TextView textView, orderHistoryDate;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.orderHistoryRecyclerview);
            textView = itemView.findViewById(R.id.orderHistoryOrderNo);
            orderHistoryDate = itemView.findViewById(R.id.orderHistoryDate);
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
        holder.orderHistoryListOrderStatus.setText(selectedItem.getStatus());
        holder.orderHistoryListName.setText(selectedItem.getItemName() == null ? "No Name" : "" + selectedItem.getItemName());
        holder.orderHistoryListQuantity.setText("Quantity: " + selectedItem.getQuantity().toString());
        holder.orderHistoryListPrice.setText("Price: " + selectedItem.getPrice().toString());
        holder.orderHistoryListSubTotal.setText("Subtotal: " + selectedItem.getSubTotal().toString());
        holder.orderHistoryListNetTotal.setText("NetTotal: " + selectedItem.getNetTotal().toString());
        Glide.with(context).load(selectedItem.getMobileAppImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return orderItemArrayArrayList.size();
    }

    class OrderHistoryListViewHolder extends RecyclerView.ViewHolder {
        TextView orderHistoryListName, orderHistoryListOrderStatus, orderHistoryListQuantity, orderHistoryListPrice, orderHistoryListSubTotal, orderHistoryListNetTotal;
        ImageView imageView;

        public OrderHistoryListViewHolder(@NonNull View itemView) {
            super(itemView);
            orderHistoryListName = itemView.findViewById(R.id.orderHistoryListName);
            orderHistoryListOrderStatus = itemView.findViewById(R.id.orderHistoryListOrderStatus);
            orderHistoryListQuantity = itemView.findViewById(R.id.orderHistoryListQuantity);
            orderHistoryListPrice = itemView.findViewById(R.id.orderHistoryListPrice);
            orderHistoryListSubTotal = itemView.findViewById(R.id.orderHistoryListSubTotal);
            orderHistoryListNetTotal = itemView.findViewById(R.id.orderHistoryListNetTotal);
            imageView = itemView.findViewById(R.id.cart_image);
        }
    }
}