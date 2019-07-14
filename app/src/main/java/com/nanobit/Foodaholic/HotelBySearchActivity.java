package com.nanobit.Foodaholic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanobit.adapter.AdapterHotelList;
import com.nanobit.asyncTask.LoadHotel;
import com.nanobit.interfaces.ClickListener;
import com.nanobit.interfaces.HomeListener;
import com.nanobit.interfaces.InterAdListener;
import com.nanobit.items.ItemRestaurant;
import com.nanobit.utils.Constant;
import com.nanobit.utils.Methods;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class HotelBySearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    private Methods methods;
    private AdapterHotelList adapterHotelList;
    private RecyclerView recyclerView;
    private ArrayList<ItemRestaurant> arrayList_hotel;
    private Menu menu;
    private CircularProgressBar progressBar;

    TextView textView_empty;
    LinearLayout ll_empty;
    String errr_msg;
    AppCompatButton button_try;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_by_cat);

        methods = new Methods(this, new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                Intent intent = new Intent(HotelBySearchActivity.this, HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_hotel.get(getPosition(Integer.parseInt(adapterHotelList.getID(position))));
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar_bycat);
        toolbar.setTitle(Constant.search_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_adView = findViewById(R.id.ll_adView_bycat);
        methods.showBannerAd(ll_adView);

        arrayList_hotel = new ArrayList<>();

        ll_empty = findViewById(R.id.ll_empty);
        textView_empty = findViewById(R.id.textView_empty_msg);
        button_try = findViewById(R.id.button_empty_try);

        progressBar = findViewById(R.id.pb_bycat);
        recyclerView = findViewById(R.id.rv_hotel_bycat);
        recyclerView.setLayoutManager(new GridLayoutManager(HotelBySearchActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        loadHotelApi();

        button_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHotelApi();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        methods.changeCart(menu);

        MenuItem item = menu.findItem(R.id.menu_search);

        MenuItem item_search = menu.findItem(R.id.menu_search);

        final MenuItem item_cart  = menu.findItem(R.id.menu_cart_search);

        final MenuItem item_filter = menu.findItem(R.id.menu_filter).setIcon(R.mipmap.filter);

        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);

        item_search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                item_filter.setVisible(false);
                if(Constant.isLogged) {
                    item_cart.setVisible(true);
                }
                return true;
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                item_filter.setVisible(true);
                if(Constant.isLogged) {
                    item_cart.setVisible(false);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Constant.search_text = s;
            getSupportActionBar().setTitle(Constant.search_text);
            loadHotelApi();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart_search:
                Intent intent = new Intent(HotelBySearchActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_filter:
                methods.openSearchFilter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadHotelApi() {
        if (methods.isNetworkAvailable()) {
            LoadHotel loadHotel = new LoadHotel(new HomeListener() {
                @Override
                public void onStart() {
                    arrayList_hotel.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    ll_empty.setVisibility(View.GONE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemRestaurant> arrayList_latest, ArrayList<ItemRestaurant> arrayList_featured) {
                    if (success.equals("true")) {
                        arrayList_hotel.addAll(arrayList_latest);
                        errr_msg = getString(R.string.no_data_found);
                    } else {
                        errr_msg = getString(R.string.error_server);
                    }
                    setAdapter();
                }
            });

            loadHotel.execute(Constant.URL_SEARCH_HOTEL_LIST + Constant.search_type + "&search_text=" + Constant.search_text.replace(" ", "%20"));
        } else {
            errr_msg = getString(R.string.net_not_conn);
            setAdapter();
        }
    }

    private void setAdapter() {
        adapterHotelList = new AdapterHotelList(HotelBySearchActivity.this, arrayList_hotel, new ClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInterAd(position, "");
            }
        });
        recyclerView.setAdapter(adapterHotelList);
        progressBar.setVisibility(View.GONE);
        setEmpty();
    }

    public void setEmpty() {
        if (arrayList_hotel.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        } else {
            textView_empty.setText(errr_msg);
            recyclerView.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    public int getPosition(int id) {
        int count = 0;
        for (int i = 0; i < arrayList_hotel.size(); i++) {
            if (id == Integer.parseInt(arrayList_hotel.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }

    @Override
    protected void onResume() {
        if (toolbar != null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCart(menu);
        }
        super.onResume();
    }
}
