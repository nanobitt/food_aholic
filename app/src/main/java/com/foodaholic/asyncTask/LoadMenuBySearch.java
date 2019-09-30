package com.foodaholic.asyncTask;

import android.os.AsyncTask;

import com.foodaholic.interfaces.MenubysearchListener;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.items.ItemRestaurant;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadMenuBySearch extends AsyncTask<String, String, Boolean> {

    private ArrayList<ItemMenu> arrayListMenu;
    private MenubysearchListener menubysearchListener;

    public LoadMenuBySearch(MenubysearchListener menubysearchListener) {
        this.menubysearchListener = menubysearchListener;
        arrayListMenu = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        menubysearchListener.onStart();
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
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String menu_id = jsonObject.getString(Constant.TAG_MENU_ID);
                String menu_name = jsonObject.getString(Constant.TAG_MENU_NAME);
                String menu_type = jsonObject.getString(Constant.TAG_MENU_TYPE);
                String price = jsonObject.getString(Constant.TAG_MENU_PRICE);
                String menu_image = jsonObject.getString(Constant.TAG_MENU_IMAGE);


                String res_id = jsonObject.getString(Constant.TAG_MENU_REST_ID);
                String rest_name = jsonObject.getString(Constant.TAG_REST_NAME);
                String rest_image = jsonObject.getString(Constant.TAG_REST_IMAGE);
                String rest_type = jsonObject.getString(Constant.TAG_REST_TYPE);
                float avg_Rate = (float) jsonObject.getDouble(Constant.TAG_REST_AVG_RATE);
                int total_rate = jsonObject.getInt(Constant.TAG_REST_TOTAL_RATE);
                String cat_name = "";

                String rest_address = jsonObject.getString(Constant.TAG_REST_ADDRESS);


                ItemRestaurant itemRestaurant = new ItemRestaurant(res_id, rest_name, rest_image, rest_type, rest_address, avg_Rate, total_rate, cat_name);

                ItemMenu itemMenu = new ItemMenu(menu_id, menu_name, menu_type, menu_image, price, res_id, rest_name);

                itemMenu.setAssociatedRestaurant(itemRestaurant);
                arrayListMenu.add(itemMenu);
            }

            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        menubysearchListener.onEnd(String.valueOf(s), arrayListMenu);
        super.onPostExecute(s);
    }

}
