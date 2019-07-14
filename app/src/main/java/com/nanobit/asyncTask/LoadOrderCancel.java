package com.nanobit.asyncTask;

import android.os.AsyncTask;

import com.nanobit.interfaces.OrderCancelListener;
import com.nanobit.utils.Constant;
import com.nanobit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadOrderCancel extends AsyncTask<String, String, Boolean> {

    private String suc = "0";
    private String msg = "";
    private OrderCancelListener orderCancelListener;

    public LoadOrderCancel(OrderCancelListener orderCancelListener) {
        this.orderCancelListener = orderCancelListener;
    }

    @Override
    protected void onPreExecute() {
        orderCancelListener.onStart();
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
                suc = c.getString(Constant.TAG_SUCCESS);
                msg = c.getString(Constant.TAG_MSG);
            }
            return true;
        } catch (Exception ee) {
            suc = "0";
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        orderCancelListener.onEnd(suc, msg);
        super.onPostExecute(s);
    }
}
