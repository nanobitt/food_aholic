package com.foodaholic.asyncTask;

import android.os.AsyncTask;

import com.foodaholic.interfaces.FaqListenser;
import com.foodaholic.items.ItemFaq;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadFaq extends AsyncTask<String, String, Boolean> {

    private FaqListenser faqListenser;
    private ArrayList<ItemFaq> arrayListFaq;

    public LoadFaq(FaqListenser faqListenser) {
        this.faqListenser = faqListenser;
        arrayListFaq = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        faqListenser.onStart();

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
                String id = c.getString(Constant.TAG_ID);
                String ques = c.getString(Constant.TAG_FAQ_QUES);
                String ans = c.getString(Constant.TAG_FAQ_ANS);

                ItemFaq itemFaq = new ItemFaq(id, ques, ans);
                arrayListFaq.add(itemFaq);

            }

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
        super.onPostExecute(aBoolean);
        faqListenser.onEnd(String.valueOf(aBoolean), arrayListFaq);
    }
}
