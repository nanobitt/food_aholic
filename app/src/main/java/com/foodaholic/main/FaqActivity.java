package com.foodaholic.main;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.foodaholic.adapter.AdapterFaqExpandable;
import com.foodaholic.asyncTask.LoadFaq;
import com.foodaholic.interfaces.FaqListenser;
import com.foodaholic.items.ItemFaq;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;

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
