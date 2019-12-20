package com.foodaholic.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadCheckOut extends AsyncTask<String, String, Boolean> {

    private Context context;
    private String suc = "1";
    private String msg = "";
    private LoginListener loginListener;

    public LoadCheckOut(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
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
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        loginListener.onEnd(suc, msg);
        super.onPostExecute(s);
    }
}
