package com.vpapps.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.vpapps.Foodaholic.FavouriteActivity;
import com.vpapps.Foodaholic.HotelDetailsActivity;
import com.vpapps.Foodaholic.R;
import com.vpapps.items.ItemOfferAndPromotion;
import com.vpapps.utils.Constant;

import java.util.ArrayList;

public class AdapterOffersAndPromotions extends BaseQuickAdapter<ItemOfferAndPromotion, BaseViewHolder> {
    private Activity activity;
    public AdapterOffersAndPromotions(Activity activity, ArrayList<ItemOfferAndPromotion> promotions) {

        super(R.layout.layout_offer_and_promotion, promotions);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ItemOfferAndPromotion promotion) {
        holder.setText(R.id.tvOfferTitle, promotion.getHeading())
                .setText(R.id.tvOfferDescription, promotion.getDescription());
        Picasso.get()
                .load(promotion.getImage_link())
                .placeholder(R.drawable.placeholder_hotel)
                .into((ImageView) holder.getView(R.id.iv_offer_and_promotion));
        Button btn = holder.getView(R.id.button_order_now);
        if(promotion.isBoundToARestaurant())
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, HotelDetailsActivity.class);
                    Constant.itemRestaurant = promotion.getAssocidatedRestaurant();
                    activity.startActivity(intent);
                }
            });

        }
        else
        {
            btn.setVisibility(View.GONE);
        }


    }
}
