package com.vpapps.Foodaholic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vpapps.adapter.AdapterCart;
import com.vpapps.asyncTask.LoadCart;
import com.vpapps.interfaces.CartListener;
import com.vpapps.items.ItemCart;
import com.vpapps.utils.Constant;
import com.vpapps.utils.Methods;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CartActivity extends AppCompatActivity {

    public static TextView textView_total;
    Toolbar toolbar;
    LoadCart loadCart;
    RecyclerView recyclerView;
    AdapterCart adapterCart;
    Methods methods;
    float total = 0;
    AppCompatButton button_checkout;
    LinearLayout ll_main;
    CircularProgressBar progressBar;

    TextView textView_empty, textView_hotel_name, textView_currency;
    AppCompatButton button_try;
    LinearLayout ll_empty;
    String errr_msg;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        methods = new Methods(this);
        toolbar = findViewById(R.id.toolbar_cart);
        toolbar.setTitle(getString(R.string.cart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.pb_cart);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        ll_empty = findViewById(R.id.ll_empty);
        textView_empty = findViewById(R.id.textView_empty_msg);
        button_try = findViewById(R.id.button_empty_try);
        errr_msg = getString(R.string.cart_is_empty);

        ll_main = findViewById(R.id.ll_main);
        button_checkout = findViewById(R.id.button_confirm_order);
        textView_hotel_name = findViewById(R.id.tv_cart_hotel_name);
        textView_total = findViewById(R.id.tv_cart_total);
        textView_currency = findViewById(R.id.tv);
        textView_currency.setTypeface(null, Typeface.BOLD);

        recyclerView = findViewById(R.id.rv_cart);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        button_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCartApi();
            }
        });

        textView_total.setTypeface(textView_total.getTypeface(), Typeface.BOLD);
        textView_hotel_name.setTypeface(textView_total.getTypeface(), Typeface.BOLD);

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.arrayList_cart.size() > 0) {
                    Intent intent = new Intent(CartActivity.this, CheckOut.class);
                    intent.putExtra("from", "");
                    intent.putExtra("cart_ids", methods.getCartIds());
                    intent.putExtra("total", textView_total.getText().toString());
                    intent.putExtra("rest_name", Constant.arrayList_cart.get(0).getRestName());
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, getString(R.string.no_items_cart), Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadCartApi();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCartApi() {
        if (Constant.isLogged) {
            if (methods.isNetworkAvailable()) {
                loadCart = new LoadCart(new CartListener() {
                    @Override
                    public void onStart() {
                        Constant.arrayList_cart.clear();
                        total = 0;
                        progressBar.setVisibility(View.VISIBLE);
                        ll_main.setVisibility(View.GONE);
                        ll_empty.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEnd(String success, String message, ArrayList<ItemCart> array) {
                        if (success.equals("true")) {
                            Constant.arrayList_cart.addAll(array);

                            if (Constant.arrayList_cart.size() > 0) {
                                for (int i = 0; i < Constant.arrayList_cart.size(); i++) {
                                    total = total + (Float.parseFloat(Constant.arrayList_cart.get(i).getMenuPrice()) * Float.parseFloat(Constant.arrayList_cart.get(i).getMenuQty()));
                                }

                                textView_hotel_name.setText(Constant.arrayList_cart.get(0).getRestName());
                                textView_total.setText(String.valueOf(total));
                            }
                            errr_msg = getString(R.string.cart_is_empty);
                        } else {
                            errr_msg = getString(R.string.error_server);
                            Toast.makeText(CartActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                        setAdapter();
                    }
                });
                loadCart.execute(Constant.URL_CART + Constant.itemUser.getId());
            } else {
                errr_msg = getString(R.string.net_not_conn);
                setAdapter();
            }
        } else {
            errr_msg = getString(R.string.not_log);
            setAdapter();
        }
    }

    private void setAdapter() {
        adapterCart = new AdapterCart(CartActivity.this, Constant.arrayList_cart);
        recyclerView.setAdapter(adapterCart);
        progressBar.setVisibility(View.GONE);
        setEmpty();
    }

    public void setEmpty() {
        if (Constant.arrayList_cart.size() > 0) {
            ll_main.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        } else {
            textView_empty.setText(errr_msg);
            ll_main.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    public void hideView() {
        setEmpty();
    }

    @Override
    protected void onResume() {
        if (Constant.isCartRefresh) {
            Constant.isCartRefresh = false;
            if (methods.isNetworkAvailable()) {
                loadCartApi();
            } else {
                Toast.makeText(this, getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
            }
        }
        super.onResume();
    }
}
