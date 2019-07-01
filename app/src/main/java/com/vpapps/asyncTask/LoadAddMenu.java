package com.vpapps.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.vpapps.interfaces.LoginListener;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoadAddMenu extends AsyncTask<String, String, Boolean> {

    private Context context;
    private String suc = "";
    private String msg = "";
    private LoginListener loginListener;

    public LoadAddMenu(Context context, LoginListener loginListener) {
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
        String url = strings[0].replace(" ", "%20");
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                suc = c.getString(Constant.TAG_SUCCESS);
                msg = c.getString(Constant.TAG_MSG);
                if (c.has(Constant.TAG_CART_COUNT)) {
                    Constant.menuCount = Integer.parseInt(c.getString(Constant.TAG_CART_COUNT));
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
        loginListener.onEnd(suc, msg);
        super.onPostExecute(s);
    }
}