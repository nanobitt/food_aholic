package com.foodaholic.asyncTask;

import android.os.AsyncTask;

import com.foodaholic.interfaces.FoodofthedayListener;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.items.ItemRestaurant;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadFoodoftheday extends AsyncTask<String, String, Boolean> {

    private FoodofthedayListener foodofthedayListener;
    private ArrayList<ItemMenu> foods;

    public LoadFoodoftheday(FoodofthedayListener foodofthedayListener)
    {
        this.foodofthedayListener = foodofthedayListener;
        this.foods = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        foodofthedayListener.onStart();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject = jsonArray.getJSONObject(j);

                String menu_id = jsonObject.getString(Constant.TAG_MENU_ID);
                String menu_name = jsonObject.getString(Constant.TAG_MENU_NAME);
                String menu_type = jsonObject.getString(Constant.TAG_MENU_TYPE);
                String desc = jsonObject.getString(Constant.TAG_MENU_DESC);
                String price = jsonObject.getString(Constant.TAG_MENU_PRICE);
                String menu_image = jsonObject.getString(Constant.TAG_MENU_IMAGE);
                String cat_id = "";
                String res_id = jsonObject.getString(Constant.TAG_MENU_REST_ID);

                JSONObject rest_obj = jsonObject.getJSONObject(Constant.TAG_REST_ROOT);
                String rest_id = rest_obj.getString(Constant.TAG_ID);
                String name = rest_obj.getString(Constant.TAG_REST_NAME);
                String address = rest_obj.getString(Constant.TAG_REST_ADDRESS);
                String rest_image = rest_obj.getString(Constant.TAG_REST_IMAGE);
                String type = rest_obj.getString(Constant.TAG_REST_TYPE);
                float avg_Rate = Float.parseFloat(rest_obj.getString(Constant.TAG_REST_AVG_RATE));
                int total_rate = Integer.parseInt(rest_obj.getString(Constant.TAG_REST_TOTAL_RATE));
                String cat_name = "";
                String open = rest_obj.getString(Constant.TAG_REST_OPEN);

                ItemRestaurant itemRestaurant = new ItemRestaurant(rest_id,name,rest_image,type,address,avg_Rate,total_rate, cat_name, open);

                ItemMenu itemMenu = new ItemMenu(menu_id, menu_name, menu_type, menu_image, desc, price, res_id, cat_id, itemRestaurant);
                foods.add(itemMenu);
            }

            return true;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;

        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        foodofthedayListener.onEnd(String.valueOf(aBoolean), foods);
    }
}
