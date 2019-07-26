package com.foodaholic.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.items.ItemCart;
import com.foodaholic.items.ItemUser;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadLoginLocal extends AsyncTask<String,String,Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private LoginListener loginListener;

    public LoadLoginLocal(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        Constant.arrayList_cart.clear();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        Log.e("json",json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);

            JSONObject object = jsonArray.getJSONObject(0);
            suc = object.getString(Constant.TAG_SUCCESS);
            if(suc.equals("0")) {
                msg = object.getString(Constant.TAG_MSG);
            } else {
                String user_id = object.getString(Constant.TAG_USER_ID);
                Constant.menuCount = Integer.parseInt(object.getString(Constant.TAG_CART_COUNT));
                Constant.itemUser = new ItemUser(user_id,object.getString(Constant.TAG_USER_NAME),object.getString(Constant.TAG_USER_EMAIL),object.getString(Constant.TAG_USER_PHONE),object.getString(Constant.TAG_USER_IMAGE),object.getString(Constant.TAG_USER_ADDRESS));

                try {
                    JSONArray jA_cart = object.getJSONArray("cart_list");
                    for (int i = 0; i < jA_cart.length(); i++) {
                        JSONObject c = jA_cart.getJSONObject(i);

                        String cartid = c.getString(Constant.TAG_CART_ID);
                        String rest_id = c.getString(Constant.TAG_MENU_REST_ID);
                        String rest_name = c.getString(Constant.TAG_REST_NAME);
                        String menu_id = c.getString(Constant.TAG_CART_MENU_ID);
                        String menu_name = c.getString(Constant.TAG_MENU_NAME);
                        String menu_image = c.getString(Constant.TAG_MENU_IMAGE);
                        String menu_qty = c.getString(Constant.TAG_MENU_QYT);
                        String menu_price = c.getString(Constant.TAG_MENU_PRICE);

                        ItemCart itemCart = new ItemCart(cartid, rest_id, rest_name, menu_id, menu_name, menu_image, menu_qty, menu_price, menu_qty);
                        Constant.arrayList_cart.add(itemCart);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        loginListener.onEnd(String.valueOf(s),suc);
        super.onPostExecute(s);
    }
}
