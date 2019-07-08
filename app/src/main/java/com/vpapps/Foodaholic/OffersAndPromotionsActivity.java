package com.vpapps.Foodaholic;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


import com.vpapps.utils.Methods;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OffersAndPromotionsActivity extends AppCompatActivity {

    Methods methods;
    Toolbar toolbar;

    ProgressDialog pbar;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_and_promotions);

        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), toolbar);

        toolbar = this.findViewById(R.id.toolbar_offer_and_promotions);
        toolbar.setTitle(getString(R.string.offer_and_promotions));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbar = new ProgressDialog(this);
        pbar.setMessage(getResources().getString(R.string.loading));
        pbar.setCancelable(false);

        if(methods.isNetworkAvailable())
        {
            loadOffersAndPromotions();
        }
        else
        {
            Toast.makeText(this, R.string.net_not_conn, Toast.LENGTH_LONG).show();
        }

    }

    private void loadOffersAndPromotions()
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
