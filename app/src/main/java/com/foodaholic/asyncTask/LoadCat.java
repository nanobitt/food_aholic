package com.foodaholic.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.foodaholic.interfaces.CategoryListener;
import com.foodaholic.items.ItemCat;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadCat extends AsyncTask<String, String, Boolean> {

    private Context context;
    private CategoryListener categoryListener;
    private ArrayList<ItemCat> arrayList_cat;

    public LoadCat(Context context, CategoryListener categoryListener) {
        this.context = context;
        this.categoryListener = categoryListener;
        arrayList_cat = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        categoryListener.onStart();
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

                String id = c.getString(Constant.TAG_CAT_ID);
                String name = c.getString(Constant.TAG_CAT_NAME);
                String image = c.getString(Constant.TAG_CAT_IMAGE);

                ItemCat itemCat = new ItemCat(id, name, image);
                arrayList_cat.add(itemCat);
            }
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        categoryListener.onEnd(String.valueOf(s), "", arrayList_cat);
        super.onPostExecute(s);
    }
}
