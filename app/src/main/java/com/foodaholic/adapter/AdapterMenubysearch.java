package com.foodaholic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.main.HotelDetailsActivity;
import com.foodaholic.main.R;
import com.foodaholic.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMenubysearch extends BaseQuickAdapter<ItemMenu, BaseViewHolder> {
    Activity activity;
    public AdapterMenubysearch(Activity activity,@Nullable List<ItemMenu> data) {
        super(R.layout.layout_menu, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ItemMenu itemMenu) {
        ImageView imageView_type = holder.getView(R.id.iv_menu_type);
        TextView textView_title = holder.getView(R.id.tv_menu_name);
        TextView textView_price = holder.getView(R.id.tv_menu_price);
        TextView textView_rest_name = holder.getView(R.id.tv_menu_rest);
        LinearLayout linearLayout =  holder.getView(R.id.ll_menu);
        TextView tv_currency =  holder.getView(R.id.tv);
        ImageView imageView = holder.getView(R.id.iv_menu_image);

        tv_currency.setTypeface(null, Typeface.BOLD);

        if (itemMenu.getType().equals(Constant.TAG_VEG)) {
           imageView_type.setImageResource(R.mipmap.veg);
        } else if (itemMenu.getType().equals(Constant.TAG_NONVEG)) {
            imageView_type.setImageResource(R.mipmap.nonveg);
        }

        textView_title.setText(itemMenu.getName());
        textView_price.setText(itemMenu.getPrice());
        textView_rest_name.setText(itemMenu.getRest_name());
        Picasso.get()
                .load(itemMenu.getImage())
                .placeholder(R.drawable.placeholder_menu)
                .into(imageView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HotelDetailsActivity.class);
                Constant.itemRestaurant =itemMenu.getAssociatedRestaurant();
                activity.startActivity(intent);
            }
        });



    }
}
