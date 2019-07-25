package com.nanobit.asyncTask;

import android.os.AsyncTask;

import com.nanobit.interfaces.RestServiceChargeListener;
import com.nanobit.utils.Constant;
import com.nanobit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadRestServiceCharge extends AsyncTask<String, String, Boolean> {


    private RestServiceChargeListener serviceChargeListener;
    String service_charge_status;
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
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);
            service_charge_status = jsonArray.getJSONObject(0).getString(Constant.TAG_REST_SERVICE_CHARGE_STATUS);

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
        serviceChargeListener.onEnd(String.valueOf(aBoolean), service_charge_status);
        super.onPostExecute(aBoolean);
    }
}
