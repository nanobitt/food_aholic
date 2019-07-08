package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.OfferAndPromotionListener;
import com.vpapps.items.ItemOfferAndPromotion;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadOfferAndPromotions extends AsyncTask<String, String, Boolean> {

    private OfferAndPromotionListener offerAndPromotionListener;
    private ArrayList<ItemOfferAndPromotion> offerAndPromotionsList;

    public LoadOfferAndPromotions(OfferAndPromotionListener offerAndPromotionListener) {
        this.offerAndPromotionListener = offerAndPromotionListener;
        offerAndPromotionsList = new ArrayList<>();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        offerAndPromotionListener.onStart();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject c = jsonArray.getJSONObject(j);
                String id = c.getString(Constant.TAG_OFFER_ID);
                String offer_heading = c.getString(Constant.TAG_OFFER_HEADING);
                String offer_description = c.getString(Constant.TAG_OFFER_DESCRIPTION);
                String image_link = c.getString(Constant.TAG_OFFER_IMAGE_LINK);
                String post_date = c.getString(Constant.TAG_OFFER_POST_DATE);

                ItemOfferAndPromotion currentOffer = new ItemOfferAndPromotion(id, offer_heading, offer_description, image_link, post_date);
                offerAndPromotionsList.add(currentOffer);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        offerAndPromotionListener.onEnd(String.valueOf(aBoolean), offerAndPromotionsList);
    }
}

