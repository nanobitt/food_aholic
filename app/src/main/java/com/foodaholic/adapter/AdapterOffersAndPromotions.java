package com.foodaholic.adapter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.foodaholic.main.HotelDetailsActivity;
import com.foodaholic.main.R;
import com.foodaholic.items.ItemOfferAndPromotion;
import com.foodaholic.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterOffersAndPromotions extends BaseQuickAdapter<ItemOfferAndPromotion, BaseViewHolder> {
    private AppCompatActivity activity;
    public AdapterOffersAndPromotions(AppCompatActivity activity, ArrayList<ItemOfferAndPromotion> promotions) {

        super(R.layout.layout_offer_and_promotion, promotions);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ItemOfferAndPromotion promotion) {

        ImageView offerImage = holder.getView(R.id.iv_offer_and_promotion);
        holder.setText(R.id.tvOfferTitle, promotion.getHeading())
                .setText(R.id.tvOfferDescription, promotion.getDescription());
        Picasso.get()
                .load(promotion.getImage_link())
                .placeholder(R.drawable.placeholder_hotel)
                .into(offerImage);
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

        final ImagePopup imagePopup = new ImagePopup(activity);
        imagePopup.initiatePopupWithGlide(promotion.getImage_link());
        offerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.viewPopup();
            }
        });


    }
}
