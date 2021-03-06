package com.foodaholic.adapter;

import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.foodaholic.main.HotelDetailsActivity;
import com.foodaholic.main.R;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFoodoftheday extends BaseQuickAdapter<ItemMenu, BaseViewHolder> {

    private FragmentActivity activity;
    public AdapterFoodoftheday(FragmentActivity activity, ArrayList<ItemMenu> food_of_the_days) {

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
