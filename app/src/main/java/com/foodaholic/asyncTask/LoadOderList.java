package com.foodaholic.asyncTask;

import android.nfc.Tag;
import android.os.AsyncTask;

import com.foodaholic.interfaces.OrderListListener;
import com.foodaholic.items.ItemOrderList;
import com.foodaholic.items.ItemOrderMenu;
import com.foodaholic.items.ItemPromo;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadOderList extends AsyncTask<String, String, Boolean> {

    private OrderListListener orderListListener;
    private ArrayList<ItemOrderList> arrayList;

    public LoadOderList(OrderListListener orderListListener) {
        this.orderListListener = orderListListener;
        arrayList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        orderListListener.onStart();
        super.onPreExecute();
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

                String id = c.getString(Constant.TAG_ORDER_ID);
                String unique_id = c.getString(Constant.TAG_ORDER_UNIQUE_ID);
                String address = c.getString(Constant.TAG_ORDER_ADDRESS);
                String comment = c.getString(Constant.TAG_ORDER_COMMENT);
                String date = c.getString(Constant.TAG_ORDER_DATE);
                String status = c.getString(Constant.TAG_ORDER_STATUS);
                String has_promo = c.getString(Constant.TAG_HAS_PROMO);
                String service_charge = c.getString(Constant.TAG_REST_SERVICE_CHARGE);
                ItemPromo promo = null;

                if(has_promo.equals("1"))
                {
                    JSONObject mainObj = c.getJSONObject(Constant.TAG_PROMO);
                    String promo_id = mainObj.getString(Constant.TAG_ID);
                    String code = mainObj.getString(Constant.TAG_PROMO_CODE);
                    String value = mainObj.getString(Constant.TAG_PROMO_VALUE);
                    String type = mainObj.getString(Constant.TAG_PROMO_TYPE);
                    String minimum_order = mainObj.getString(Constant.TAG_MINIMUM_ORDER);

                    promo = new ItemPromo(promo_id, code, value, type, minimum_order);
                }

                JSONArray jA = c.getJSONArray(Constant.TAG_ORDER_ITEMS);

                ArrayList<ItemOrderMenu> arrayList_ordermenu = new ArrayList<>();
                int totalQnt = 0;
                float totalPrice = 0;

                for (int i = 0; i < jA.length(); i++) {
                    JSONObject jO = jA.getJSONObject(i);

                    String rest_id = jO.getString(Constant.TAG_MENU_REST_ID);
                    String rest_name = jO.getString(Constant.TAG_ORDER_REST_NAME);
                    String menu_id = jO.getString(Constant.TAG_CART_MENU_ID);
                    String menu_name = jO.getString(Constant.TAG_MENU_NAME);
                    String menu_image = jO.getString(Constant.TAG_MENU_IMAGE);
                    String menu_qty = jO.getString(Constant.TAG_MENU_QYT);
                    String menu_price = jO.getString(Constant.TAG_MENU_PRICE);
                    String menu_total_price = jO.getString(Constant.TAG_MENU_TOTAL_PRICE);
                    String menu_type = jO.getString(Constant.TAG_MENU_TYPE);



                    totalPrice = totalPrice + Float.parseFloat(menu_total_price);
                    totalQnt = totalQnt + Integer.parseInt(menu_qty);

                    ItemOrderMenu itemOrderMenu = new ItemOrderMenu(rest_id, rest_name, menu_id, menu_name, menu_image, menu_qty, menu_price, menu_total_price, menu_type);
                    arrayList_ordermenu.add(itemOrderMenu);
                }

                ItemOrderList itemOrderList = new ItemOrderList(id, unique_id, address, comment, date, String.valueOf(totalQnt), String.valueOf(totalPrice), status, arrayList_ordermenu, has_promo, promo, service_charge);
                arrayList.add(itemOrderList);
            }

            return true;
        } catch (Exception ee) {
//            ee.printStackTrace();
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        orderListListener.onEnd(String.valueOf(s), arrayList);
        super.onPostExecute(s);
    }
}