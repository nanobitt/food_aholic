package com.foodaholic.main;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;


import com.foodaholic.adapter.AdapterOffersAndPromotions;
import com.foodaholic.asyncTask.LoadOfferAndPromotions;
import com.foodaholic.interfaces.OfferAndPromotionListener;
import com.foodaholic.items.ItemOfferAndPromotion;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OffersAndPromotionsActivity extends AppCompatActivity {

    Methods methods;
    Toolbar toolbar;

    ProgressDialog pbar;

    RecyclerView rv_offer_and_promotions;

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

        rv_offer_and_promotions = findViewById(R.id.rv_offer_and_promotions);
        rv_offer_and_promotions.setLayoutManager(new LinearLayoutManager(this));

        if (methods.isNetworkAvailable()) {
            loadOffersAndPromotions();
        } else {
            Toast.makeText(this, R.string.net_not_conn, Toast.LENGTH_LONG).show();
        }

    }

    private void loadOffersAndPromotions() {

        LoadOfferAndPromotions loadOfferAndPromotions = new LoadOfferAndPromotions(new OfferAndPromotionListener() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemOfferAndPromotion> offerAndPromotions) {
                pbar.dismiss();
                //Toast.makeText(OffersAndPromotionsActivity.this, offerAndPromotions.get(0).getDescription(), Toast.LENGTH_LONG).show();

                AdapterOffersAndPromotions adapterOffersAndPromotions = new AdapterOffersAndPromotions(OffersAndPromotionsActivity.this, offerAndPromotions);
                rv_offer_and_promotions.setAdapter(adapterOffersAndPromotions);
            }
        });

        loadOfferAndPromotions.execute(Constant.URL_OFFER_AND_PROMOTIONS);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
