package com.example.doanapk_qlqa_nhom9;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<FoodItem> {

    private Context mContext;
    private ArrayList<FoodItem> mFoodItemList;

    public CustomAdapter(Context context, ArrayList<FoodItem> foodItemList) {
        super(context, 0, foodItemList);
        mContext = context;
        mFoodItemList = foodItemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_food, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.textViewName = convertView.findViewById(R.id.textView);
            viewHolder.textViewPrice = convertView.findViewById(R.id.price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FoodItem currentFoodItem = getItem(position);

        viewHolder.textViewName.setText(currentFoodItem.getName());
        // Format the cost to an integer string
        String priceString = String.valueOf((int) currentFoodItem.getCost());
        viewHolder.textViewPrice.setText(priceString+" VNĐ");

        // Sử dụng Glide để tải ảnh
        Glide.with(mContext)
                .load("file:///android_asset/" + currentFoodItem.getPicture())
                .placeholder(R.drawable.add) // Ảnh placeholder khi đang tải
                .into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewPrice;
    }
}
