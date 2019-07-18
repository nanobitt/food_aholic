package com.nanobit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nanobit.Foodaholic.HotelDetailsActivity;
import com.nanobit.Foodaholic.R;
import com.nanobit.items.ItemMenu;
import com.nanobit.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFoodoftheday extends BaseQuickAdapter<ItemMenu, BaseViewHolder> {

    private Activity activity;
    public AdapterFoodoftheday(Activity activity, ArrayList<ItemMenu> food_of_the_days) {

        super(R.layout.layout_food_of_the_day, food_of_the_days);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ItemMenu item) {
        Picasso.get()
                .load(item.getImage())
                .placeholder(R.drawable.placeholder_menu)
                .into((ImageView) holder.getView(R.id.iv_menu_image));

        holder.setText(R.id.tv_menu_name, item.getName());
        holder.setText(R.id.tv_menu_price, item.getPrice());
        holder.setText(R.id.tv_restaurant_name, item.getAssociatedRestaurant().getName());
        holder.setText(R.id.tv_menu_price, item.getPrice());

        CardView cardView = holder.getView(R.id.mainHolder);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HotelDetailsActivity.class);
                Constant.itemRestaurant = item.getAssociatedRestaurant();
                activity.startActivity(intent);
            }
        });


    }
}
