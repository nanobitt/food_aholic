package com.foodaholic.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodaholic.asyncTask.LoadAbout;
import com.foodaholic.interfaces.AboutListener;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.DBHelper;
import com.foodaholic.utils.Methods;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    Methods methods;
    WebView webView;
    DBHelper dbHelper;
    TextView textView_appname, textView_email, textView_website, textView_company, textView_contact, textView_version;
    ImageView imageView_logo;
    CardView ll_email, ll_website, ll_company, ll_contact;
    String website, email, desc, applogo, appname, appversion, appauthor, appcontact, privacy, developedby;
    ProgressDialog pbar;
    LoadAbout loadAbout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        dbHelper = new DBHelper(this);
        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), toolbar);

        toolbar = this.findViewById(R.id.toolbar_about);
        toolbar.setTitle(getString(R.string.app_name));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbar = new ProgressDialog(this);
        pbar.setMessage(getResources().getString(R.string.loading));
        pbar.setCancelable(false);

        webView = findViewById(R.id.webView);
        textView_appname = findViewById(R.id.textView_about_appname);
        textView_email = findViewById(R.id.textView_about_email);
        textView_website = findViewById(R.id.textView_about_site);
        textView_company = findViewById(R.id.textView_about_company);
        textView_contact = findViewById(R.id.textView_about_contact);
        textView_version = findViewById(R.id.textView_about_appversion);
        imageView_logo = findViewById(R.id.imageView_about_logo);

        textView_appname.setTypeface(textView_appname.getTypeface(), Typeface.BOLD);

        ll_email = findViewById(R.id.ll_email);
        ll_website = findViewById(R.id.ll_website);
        ll_contact = findViewById(R.id.ll_contact);
        ll_company = findViewById(R.id.ll_company);
        dbHelper.getAbout();

        if (methods.isNetworkAvailable()) {
            loadAboutData();
        } else {
            if (dbHelper.getAbout()) {
                setTexts();
                setVariables();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void loadAboutData() {
        loadAbout = new LoadAbout(new AboutListener() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success) {
                if (pbar.isShowing()) {
                    pbar.dismiss();
                }
                if (success.equals("1")) {
                    setTexts();
                    setVariables();
                    dbHelper.addtoAbout();
                }

            }
        });
        loadAbout.execute(Constant.URL_ABOUT);
    }

    private void setTexts() {
        appname = Constant.itemAbout.getAppName();
        applogo = Constant.itemAbout.getAppLogo();
        desc = Constant.itemAbout.getAppDesc();
        appversion = Constant.itemAbout.getAppVersion();
        appauthor = Constant.itemAbout.getAuthor();
        appcontact = Constant.itemAbout.getContact();
        email = Constant.itemAbout.getEmail();
        website = Constant.itemAbout.getWebsite();
        privacy = Constant.itemAbout.getPrivacy();
        developedby = Constant.itemAbout.getDevelopedby();
    }

    public void setVariables() {
        textView_appname.setText(appname);
        if (!email.trim().isEmpty()) {
            ll_email.setVisibility(View.VISIBLE);
            textView_email.setText(email);
        }

        if (!website.trim().isEmpty()) {
            ll_website.setVisibility(View.VISIBLE);
            textView_website.setText(website);
        }

        if (!appauthor.trim().isEmpty()) {
            ll_company.setVisibility(View.VISIBLE);
            textView_company.setText(appauthor);
        }

        if (!appcontact.trim().isEmpty()) {
            ll_contact.setVisibility(View.VISIBLE);
            textView_contact.setText(appcontact);
        }

        if (!appversion.trim().isEmpty()) {
            textView_version.setText(appversion);
        }

        if (applogo.trim().isEmpty()) {
            imageView_logo.setVisibility(View.GONE);
        } else {
            Picasso
                    .get()
                    .load(Constant.URL_ABOUT_US_LOGO + applogo)
                    .into(imageView_logo);
        }

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";

        String text = "<html><head>"
                + "<style> body{color:#cc000000 !important;text-align:left}"
                + "</style></head>"
                + "<body>"
                + desc
                + "</body></html>";


        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(text, mimeType, encoding);
    }
}