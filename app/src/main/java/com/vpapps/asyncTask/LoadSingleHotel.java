package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.SingleHotelListener;
import com.vpapps.items.ItemRestaurant;
import com.vpapps.items.ItemReview;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadSingleHotel extends AsyncTask<String,String,Boolean> {

    private SingleHotelListener singleHotelListener;
    private ItemRestaurant itemRestaurant;
    private ArrayList<ItemReview> arrayList_review = new ArrayList<>();

    public LoadSingleHotel(ItemRestaurant itemRestaurant, SingleHotelListener singleHotelListener) {
        this.singleHotelListener = singleHotelListener;
        this.itemRestaurant = itemRestaurant;
    }

    @Override
    protected void onPreExecute() {
        singleHotelListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                itemRestaurant.setMonday(c.getString(Constant.TAG_REST_MONDAY));
                itemRestaurant.setTuesday(c.getString(Constant.TAG_REST_TUESDAY));
                itemRestaurant.setWednesday(c.getString(Constant.TAG_REST_WEDNESDAY));
                itemRestaurant.setThursday(c.getString(Constant.TAG_REST_THURSDAY));
                itemRestaurant.setFriday(c.getString(Constant.TAG_REST_FRRIDAY));
                itemRestaurant.setSaturday(c.getString(Constant.TAG_REST_SATURDAY));
                itemRestaurant.setSunday(c.getString(Constant.TAG_REST_SUNDAY));
                itemRestaurant.setCid(c.getString(Constant.TAG_CAT_ID));
                itemRestaurant.setCname(c.getString(Constant.TAG_CAT_NAME));
                itemRestaurant.setCimage(c.getString(Constant.TAG_CAT_IMAGE));

                if(c.has(Constant.TAG_RATING_REVIEW)) {
                    JSONArray jA = c.getJSONArray(Constant.TAG_RATING_REVIEW);
                    for (int j = 0; j < jA.length(); j++) {
                        JSONObject cc = jA.getJSONObject(j);

                        String rate_id = cc.getString(Constant.TAG_RATING_ID);
                        String username = cc.getString(Constant.TAG_NAME_USER);
                        String rate = cc.getString(Constant.TAG_RATING);
                        String msg = cc.getString(Constant.TAG_RATING_MSG);

                        ItemReview itemReview = new ItemReview(rate_id, username, rate, msg);
                        arrayList_review.add(itemReview);
                    }
                }

                Constant.itemRestaurant.setArrayListReview(arrayList_review);
            }

            return true;
        }  catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        singleHotelListener.onEnd(String.valueOf(s),itemRestaurant);
        super.onPostExecute(s);
    }
}
