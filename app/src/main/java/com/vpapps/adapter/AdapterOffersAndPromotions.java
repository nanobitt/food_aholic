package com.vpapps.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.vpapps.Foodaholic.R;
import com.vpapps.items.ItemOfferAndPromotion;

import java.util.ArrayList;

public class AdapterOffersAndPromotions extends BaseQuickAdapter<ItemOfferAndPromotion, BaseViewHolder> {
    public AdapterOffersAndPromotions(ArrayList<ItemOfferAndPromotion> promotions) {
        super(R.layout.layout_offer_and_promotion, promotions);
    }

    @Override
    protected void convert(BaseViewHolder holder, ItemOfferAndPromotion promotion) {
        holder.setText(R.id.tvOfferTitle, promotion.getHeading())
                .setText(R.id.tvOfferDescription, promotion.getDescription());
        Picasso.get()
                .load(promotion.getImage_link())
                .placeholder(R.drawable.placeholder_hotel)
                .into((ImageView) holder.getView(R.id.iv_offer_and_promotion));


    }
}
