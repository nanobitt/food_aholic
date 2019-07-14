package com.nanobit.asyncTask;

import android.os.AsyncTask;

import com.nanobit.interfaces.HomeListener;
import com.nanobit.items.ItemRestaurant;
import com.nanobit.utils.Constant;
import com.nanobit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadHotel extends AsyncTask<String,String,Boolean> {

    private HomeListener homeListener;
    private ArrayList<ItemRestaurant> arrayList;

    public LoadHotel(HomeListener homeListener) {
        this.homeListener = homeListener;
        arrayList = new ArrayList<>();
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
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

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
                if(c.has(Constant.TAG_CAT_NAME)) {
                    cat_name = c.getString(Constant.TAG_CAT_NAME);
                }

                ItemRestaurant itemRestaurant = new ItemRestaurant(id,name,image,type,address,avg_Rate,total_rate, cat_name);
                arrayList.add(itemRestaurant);
            }

            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        homeListener.onEnd(String.valueOf(s),arrayList, null);
        super.onPostExecute(s);
    }
}