package com.nanobit.Foodaholic;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.nanobit.adapter.AdapterFaqExpandable;
import com.nanobit.asyncTask.LoadFaq;
import com.nanobit.interfaces.FaqListenser;
import com.nanobit.items.ItemFaq;
import com.nanobit.utils.Constant;
import com.nanobit.utils.Methods;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FaqActivity extends AppCompatActivity {


    Methods methods;
    Toolbar toolbar;

    ProgressDialog pbar;

    ArrayList<ExpandableGroup> faqarrayList;
    RecyclerView rvFaq;

    AdapterFaqExpandable adapterFaqExpandable;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        methods = new Methods(this);
        methods.forceRTLIfSupported(getWindow());
        methods.setStatusColor(getWindow(), toolbar);

        toolbar = this.findViewById(R.id.toolbar_faq);
        toolbar.setTitle(getString(R.string.faq));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbar = new ProgressDialog(this);
        pbar.setMessage(getResources().getString(R.string.loading));
        pbar.setCancelable(false);
        faqarrayList = new ArrayList<>();

        rvFaq = findViewById(R.id.rv_faq);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFaq.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator animator = rvFaq.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(true);
        }

        if (methods.isNetworkAvailable()) {
            loadFaqApi();
        } else {
            Toast.makeText(this, R.string.net_not_conn, Toast.LENGTH_LONG).show();
        }
    }

    private void loadFaqApi()
    {
        LoadFaq loadFaq = new LoadFaq(new FaqListenser() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success, ArrayList<ItemFaq> arrayListFaq) {
                pbar.dismiss();
                for(int i = 0; i<arrayListFaq.size(); i++)
                {
                    List<String> ansList = new ArrayList<>();
                    ansList.add(arrayListFaq.get(i).getFaq_ans());
                    faqarrayList.add(new ExpandableGroup(arrayListFaq.get(i).getFaq_ques(),  ansList));
                }
                adapterFaqExpandable = new AdapterFaqExpandable(faqarrayList);
                rvFaq.setAdapter(adapterFaqExpandable);


            }
        });

        loadFaq.execute(Constant.URL_FAQ);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
