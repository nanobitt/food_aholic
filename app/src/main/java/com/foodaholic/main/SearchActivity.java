package com.foodaholic.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodaholic.adapter.AdapterHotelList;
import com.foodaholic.adapter.AdapterMenubysearch;
import com.foodaholic.asyncTask.LoadHotel;
import com.foodaholic.asyncTask.LoadMenuBySearch;
import com.foodaholic.interfaces.ClickListener;
import com.foodaholic.interfaces.HomeListener;
import com.foodaholic.interfaces.InterAdListener;
import com.foodaholic.interfaces.MenubysearchListener;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.items.ItemRestaurant;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class SearchActivity extends AppCompatActivity {

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
                Intent intent = new Intent(SearchActivity.this, HotelDetailsActivity.class);
                Constant.itemRestaurant = arrayList_hotel.get(getPosition(Integer.parseInt(adapterHotelList.getID(position))));
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar_bycat);
        toolbar.setTitle(Constant.search_text);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout ll_adView = findViewById(R.id.ll_adView_bycat);

        arrayList_hotel = new ArrayList<>();

        ll_empty = findViewById(R.id.ll_empty);
        textView_empty = findViewById(R.id.textView_empty_msg);
        button_try = findViewById(R.id.button_empty_try);

        progressBar = findViewById(R.id.pb_bycat);
        recyclerView = findViewById(R.id.rv_hotel_bycat);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       

        loadSearchQuery();

       

        button_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSearchQuery();
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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setOnQueryTextListener(queryTextListener);

        MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
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
            loadSearchQuery();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    public void changeMenu() {
        if (toolbar != null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCart(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart_search:
                Intent intent = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_filter:
                methods.openSearchFilter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSearchQuery()
    {
        if(Constant.search_type.equals("Restaurant"))
        {
            loadHotelApi();
        }
        else
        {
            loadMenuApi();
        }

    }

    private void loadMenuApi()
    {
        if (methods.isNetworkAvailable()) {
            LoadMenuBySearch loadHotel = new LoadMenuBySearch(new MenubysearchListener() {
                @Override
                public void onStart() {
                    arrayList_hotel.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    ll_empty.setVisibility(View.GONE);
                }

                @Override
                public void onEnd(String success, ArrayList<ItemMenu> menuList) {

                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setHasFixedSize(true);

                    AdapterMenubysearch adapterMenubysearch = new AdapterMenubysearch(SearchActivity.this, menuList);
                    recyclerView.setAdapter(adapterMenubysearch);
                    progressBar.setVisibility(View.GONE);
                    setMenuLayoutEmpty(menuList.size());


                }
            });

            loadHotel.execute(Constant.URL_SEARCH_HOTEL_LIST + Constant.search_type + "&search_text=" + Constant.search_text.replace(" ", "%20"));
        } else {
            errr_msg = getString(R.string.net_not_conn);
            progressBar.setVisibility(View.GONE);
            setMenuLayoutEmpty(0);
        }
    }


    private void loadHotelApi()
    {
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
                    setRestaurantBySearchAdapter();
                }
            });

            loadHotel.execute(Constant.URL_SEARCH_HOTEL_LIST + Constant.search_type + "&search_text=" + Constant.search_text.replace(" ", "%20"));
        } else {
            errr_msg = getString(R.string.net_not_conn);
            setRestaurantBySearchAdapter();
        }
    }

    private void setRestaurantBySearchAdapter()
    {

        adapterHotelList = new AdapterHotelList(SearchActivity.this, arrayList_hotel, new ClickListener() {
            @Override
            public void onClick(int position) {
                methods.showInterAd(position, "");
            }
        });
        recyclerView.setAdapter(adapterHotelList);
        progressBar.setVisibility(View.GONE);
        setHotelLayoutEmpty();
    }

    public void setHotelLayoutEmpty() {
        if (arrayList_hotel.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        } else {
            textView_empty.setText(errr_msg);
            recyclerView.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        }
    }
    public void setMenuLayoutEmpty(int size) {
        if (size > 0) {
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
