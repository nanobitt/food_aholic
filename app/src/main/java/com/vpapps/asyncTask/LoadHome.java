package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.HomeListener;
import com.vpapps.items.ItemRestaurant;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadHome extends AsyncTask<String, String, Boolean> {

    private HomeListener homeListener;
    private ArrayList<ItemRestaurant> arrayList_featured, arrayList_latest;

    public LoadHome(HomeListener homeListener) {
        this.homeListener = homeListener;
        arrayList_latest = new ArrayList<>();
        arrayList_featured = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        homeListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONObject jsonObject = jOb.getJSONObject(Constant.TAG_ROOT);
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_FEATURED_REST);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                String id = c.getString(Constant.TAG_ID);
                String name = c.getString(Constant.TAG_REST_NAME);
                String address = c.getString(Constant.TAG_REST_ADDRESS);
                String image = c.getString(Constant.TAG_REST_IMAGE);
                String type = c.getString(Constant.TAG_REST_TYPE);
                float avg_Rate = Float.parseFloat(c.getString(Constant.TAG_REST_AVG_RATE));
                int total_rate = Integer.parseInt(c.getString(Constant.TAG_REST_TOTAL_RATE));
                String cat_name = "";
                if (c.has(Constant.TAG_CAT_NAME)) {
                    cat_name = c.getString(Constant.TAG_CAT_NAME);
                }

                ItemRestaurant itemRestaurant = new ItemRestaurant(id, name, image, type, address, avg_Rate, total_rate, cat_name);
                arrayList_featured.add(itemRestaurant);
            }

            JSONArray jArray = jsonObject.getJSONArray(Constant.TAG_LATEST_REST);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject c = jArray.getJSONObject(i);

                String id = c.getString(Constant.TAG_ID);
                String name = c.getString(Constant.TAG_REST_NAME);
                String address = c.getString(Constant.TAG_REST_ADDRESS);
                String image = c.getString(Constant.TAG_REST_IMAGE);
                String type = c.getString(Constant.TAG_REST_TYPE);
                float avg_Rate = Float.parseFloat(c.getString(Constant.TAG_REST_AVG_RATE));
                int total_rate = Integer.parseInt(c.getString(Constant.TAG_REST_TOTAL_RATE));
                String cat_name = "";
                if (c.has(Constant.TAG_CAT_NAME)) {
                    cat_name = c.getString(Constant.TAG_CAT_NAME);
                }

                ItemRestaurant itemRestaurant = new ItemRestaurant(id, name, image, type, address, avg_Rate, total_rate, cat_name);
                arrayList_latest.add(itemRestaurant);
            }
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        homeListener.onEnd(String.valueOf(s), arrayList_latest, arrayList_featured);
        super.onPostExecute(s);
    }
}