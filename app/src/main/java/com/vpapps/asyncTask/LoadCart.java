package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.CartListener;
import com.vpapps.items.ItemCart;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadCart extends AsyncTask<String, String, Boolean> {

    private String msg = "";
    private CartListener cartListener;
    private ArrayList<ItemCart> arrayList;

    public LoadCart(CartListener cartListener) {
        this.cartListener = cartListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        cartListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);
            JSONObject c = null;
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    c = jsonArray.getJSONObject(i);

                    String cartid = c.getString(Constant.TAG_CART_ID);
                    String rest_id = c.getString(Constant.TAG_MENU_REST_ID);
                    String rest_name = c.getString(Constant.TAG_REST_NAME);
                    String menu_id = c.getString(Constant.TAG_CART_MENU_ID);
                    String menu_name = c.getString(Constant.TAG_MENU_NAME);
                    String menu_image = c.getString(Constant.TAG_MENU_IMAGE);
                    String menu_qty = c.getString(Constant.TAG_MENU_QYT);
                    String menu_price = c.getString(Constant.TAG_MENU_PRICE);

                    ItemCart itemCart = new ItemCart(cartid, rest_id, rest_name, menu_id, menu_name, menu_image, menu_qty, menu_price, menu_qty);
                    arrayList.add(itemCart);
                }
            } catch (Exception e) {
                try {
                    msg = c.getString(Constant.TAG_MSG);
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                e.printStackTrace();
                return true;
            }
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        cartListener.onEnd(String.valueOf(s), msg, arrayList);
        super.onPostExecute(s);
    }
}