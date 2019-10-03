package com.foodaholic.asyncTask;

import android.os.AsyncTask;

import com.foodaholic.interfaces.PromoCodeListener;
import com.foodaholic.items.ItemPromo;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONObject;

public class LoadPromoCode extends AsyncTask<String, String, Boolean> {

    private PromoCodeListener promoCodeListener;
    private ItemPromo promo = null;
    private String msg, result;

    public LoadPromoCode(PromoCodeListener promoCodeListener) {
        this.promoCodeListener = promoCodeListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        promoCodeListener.onStart();

    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);


            msg = jOb.getJSONObject(Constant.TAG_ROOT).getString(Constant.TAG_MSG);
            result = jOb.getJSONObject(Constant.TAG_ROOT).getString(Constant.TAG_PROMO_RESULT);


            if(result.equals("1"))
            {
                JSONObject mainObj = jOb.getJSONObject(Constant.TAG_ROOT).getJSONObject(Constant.TAG_PROMO);
                String code = mainObj.getString(Constant.TAG_PROMO_CODE);
                String value = mainObj.getString(Constant.TAG_PROMO_VALUE);
                String type = mainObj.getString(Constant.TAG_PROMO_TYPE);
                String minimum_order = mainObj.getString(Constant.TAG_MINIMUM_ORDER);

                promo = new ItemPromo(code, value, type, minimum_order);
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
        promoCodeListener.onEnd(String.valueOf(aBoolean), result, msg, promo);
    }

}
