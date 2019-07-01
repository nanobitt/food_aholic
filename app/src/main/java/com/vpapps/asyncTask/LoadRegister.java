package com.vpapps.asyncTask;

import android.os.AsyncTask;

import com.vpapps.interfaces.LoginListener;
import com.vpapps.utils.Constant;
import com.vpapps.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoadRegister extends AsyncTask<String, String, String> {

    private String msg = "";
    private String suc = "";
    private LoginListener loginListener;

    public LoadRegister(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String name = strings[0];
        String email = strings[1];
        String pass = strings[2];
        String phone = strings[3];

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("email", email)
                .addFormDataPart("password", pass)
                .addFormDataPart("phone", phone)
                .addFormDataPart("user_image", "")
                .build();

        try {
//            JSONObject jsonObject = JsonUtils.makeHttpRequest(Constant.URL_REGISTER,"POST",nameValuePairs);
            String json = JsonUtils.okhttpPost(Constant.URL_REGISTER, requestBody);
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);
            JSONObject obj = jsonArray.getJSONObject(0);

            msg = obj.getString(Constant.TAG_MSG);
            suc = obj.getString(Constant.TAG_SUCCESS);

            return "1";

        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loginListener.onEnd(s, msg);
        super.onPostExecute(s);
    }
}
