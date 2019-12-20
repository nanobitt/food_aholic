package com.foodaholic.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.foodaholic.interfaces.RestServiceChargeListener;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadRestServiceCharge extends AsyncTask<String, String, Boolean> {


    private RestServiceChargeListener serviceChargeListener;
    String[] status;
    public LoadRestServiceCharge(RestServiceChargeListener serviceChargeListener)
    {
        this.serviceChargeListener = serviceChargeListener;
    }

    @Override
    protected void onPreExecute() {
        serviceChargeListener.onStart();
        super.onPreExecute();

    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        Log.d("-------------------", json);
        status = new String[3];
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);
            status[0] = jsonArray.getJSONObject(0).getString(Constant.TAG_REST_SERVICE_CHARGE_STATUS);
            status[1] = jsonArray.getJSONObject(0).getString(Constant.TAG_REST_OPEN_STATUS);
            status[2] = jsonArray.getJSONObject(0).getString(Constant.TAG_REST_SERVICE_CHARGE);

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
        serviceChargeListener.onEnd(String.valueOf(aBoolean), status);
        super.onPostExecute(aBoolean);
    }
}
