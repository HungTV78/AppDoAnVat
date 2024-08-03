package com.example.doanapk_qlqa_nhom9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderHistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> orderList;

    public OrderHistoryAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder();
            holder.orderName = convertView.findViewById(R.id.order_name);
            holder.orderQuantity = convertView.findViewById(R.id.order_quantity);
            holder.orderDate = convertView.findViewById(R.id.order_date);
            holder.orderTotalAmount = convertView.findViewById(R.id.order_total_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orderList.get(position);
        holder.orderName.setText(order.getName());
        holder.orderQuantity.setText("Số lượng: " + order.getQuantity());
        holder.orderDate.setText("Ngày đặt: " + order.getOrderDate());
        holder.orderTotalAmount.setText("Tổng tiền: " + order.getTotalAmount() + " VND");

        return convertView;
    }

    static class ViewHolder {
        TextView orderName;
        TextView orderQuantity;
        TextView orderDate;
        TextView orderTotalAmount;
    }
}
