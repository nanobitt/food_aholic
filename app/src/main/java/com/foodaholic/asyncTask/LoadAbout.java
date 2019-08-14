package com.foodaholic.asyncTask;

import android.os.AsyncTask;

import com.foodaholic.interfaces.AboutListener;
import com.foodaholic.items.ItemAbout;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoadAbout extends AsyncTask<String, String, String> {

    private AboutListener aboutListener;

    public LoadAbout(AboutListener aboutListener) {
        this.aboutListener = aboutListener;
    }

    @Override
    protected void onPreExecute() {
        aboutListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.okhttpGET(url);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.TAG_ROOT);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                String appname = c.getString("app_name");
                String applogo = c.getString("app_logo");
                String desc = c.getString("app_description");
                String appversion = c.getString("app_version");
                String appauthor = c.getString("app_author");
                String appcontact = c.getString("app_contact");
                String email = c.getString("app_email");
                String website = c.getString("app_website");
                String privacy = c.getString("app_privacy_policy");
                String terms_and_conditins = c.getString("app_terms_conditions");
                String developedby = c.getString("app_developed_by");
                String facebook_link = c.getString("app_facebook_link");

                Constant.itemAbout = new ItemAbout(appname, applogo, desc, appversion, appauthor, appcontact, email, website, privacy, terms_and_conditins, developedby, facebook_link);
            }
            return "1";
        } catch (Exception ee) {
            ee.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        aboutListener.onEnd(s);
        super.onPostExecute(s);
    }
}
