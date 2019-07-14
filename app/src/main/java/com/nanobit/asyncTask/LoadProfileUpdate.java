package com.nanobit.asyncTask;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.webkit.MimeTypeMap;

import com.nanobit.interfaces.LoginListener;
import com.nanobit.items.ItemUser;
import com.nanobit.utils.Constant;
import com.nanobit.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LoadProfileUpdate extends AsyncTask<String, String, String> {

    private Context context;
    private String suc = "";
    private LoginListener loginListener;

    public LoadProfileUpdate(Context context, LoginListener loginListener) {
        this.context = context;
        this.loginListener = loginListener;
    }

    @Override
    protected void onPreExecute() {
        loginListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String nm = strings[0];
        String email1 = strings[1];
        String password = strings[2];
        String phone = strings[3];
        String address1 = strings[4];
        String imagePath = strings[5];

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", Constant.itemUser.getId())
                .addFormDataPart("name", nm)
                .addFormDataPart("email", email1)
                .addFormDataPart("password", password)
                .addFormDataPart("phone", phone)
                .addFormDataPart("address", address1);
        if (!imagePath.equals("")) {
            File file = new File(imagePath);
            builder.addFormDataPart("user_image", file.getName(), RequestBody.create(getMediaType(file.toURI()), file));
        }

        String json_strng = JsonUtils.okhttpPost(Constant.URL_PROFILE_EDIT, builder.build());

        try {

            JSONObject jsonObj = new JSONObject(json_strng);

            JSONArray jsonArray = jsonObj.getJSONArray(Constant.TAG_ROOT);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String id = Constant.itemUser.getId();
            String name = jsonObject.getString(Constant.TAG_USER_NAME);
            String email = jsonObject.getString(Constant.TAG_USER_EMAIL);
            String mobile = jsonObject.getString(Constant.TAG_USER_PHONE);
            String address = jsonObject.getString(Constant.TAG_USER_ADDRESS);
            String image = jsonObject.getString(Constant.TAG_USER_IMAGE);
            suc = jsonObject.getString(Constant.TAG_SUCCESS);

            Constant.itemUser = new ItemUser(id, name, email, mobile, image, address);

            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        loginListener.onEnd(s, suc);
        super.onPostExecute(s);
    }

    private okhttp3.MediaType getMediaType(URI uri1) {
        Uri uri = Uri.parse(uri1.toString());
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return okhttp3.MediaType.parse(mimeType);
    }
}
