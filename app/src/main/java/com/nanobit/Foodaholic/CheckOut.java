package com.nanobit.Foodaholic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nanobit.adapter.AdapterCheckOut;
import com.nanobit.asyncTask.LoadCheckOut;
import com.nanobit.interfaces.LoginListener;
import com.nanobit.utils.Constant;
import com.nanobit.utils.Methods;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckOut extends AppCompatActivity {

    Toolbar toolbar;
    LoadCheckOut loadCheckOut;
    Methods methods;
    ProgressDialog progressDialog;
    private AppCompatButton button_checkout;
    private EditText editText_address, editText_comment;
    private TextView textView_total, textView_hotel_name, textView_currency, textView_serviceCharge;
    private String comment, address, cart_ids, total, rest_name = "", from = "";
    CardView cardView_edit;
    RecyclerView recyclerView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        methods = new Methods(this);
        progressDialog = new ProgressDialog(CheckOut.this);
        progressDialog.setMessage(getString(R.string.loading));

        toolbar = findViewById(R.id.toolbar_checkout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        methods.setStatusColor(getWindow(), toolbar);

        from = getIntent().getStringExtra("from");
        rest_name = getIntent().getStringExtra("rest_name");
        cart_ids = getIntent().getStringExtra("cart_ids");
        total = getIntent().getStringExtra("total");

        cardView_edit = findViewById(R.id.cv_checkout_edit);
        editText_address = findViewById(R.id.et_checkout_address);
        editText_comment = findViewById(R.id.et_checkout_comment);
        textView_hotel_name = findViewById(R.id.tv_checkout_hotel_name);
        textView_total = findViewById(R.id.tv_checkout_total);
        textView_currency = findViewById(R.id.tv);
        textView_serviceCharge = findViewById(R.id.tvServiceCharge);
        button_checkout = findViewById(R.id.button_checkout);

        recyclerView = findViewById(R.id.rv_checkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckOut.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        AdapterCheckOut adapterCart = new AdapterCheckOut(CheckOut.this, Constant.arrayList_cart);
        recyclerView.setAdapter(adapterCart);

        textView_currency.setTypeface(null, Typeface.BOLD);
        textView_hotel_name.setText(rest_name);
        textView_hotel_name.setTypeface(textView_hotel_name.getTypeface(), Typeface.BOLD);
        textView_total.setTypeface(textView_hotel_name.getTypeface(), Typeface.BOLD);
        textView_total.setText(total);
        editText_address.setText(Constant.itemUser.getAddress());

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    address = URLEncoder.encode(editText_address.getText().toString());
                    comment = URLEncoder.encode(editText_comment.getText().toString());
                    loadCheckOutApi();
                }
            }
        });

        cardView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("home")) {
                    Intent intent = new Intent(CheckOut.this, CartActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });


        if(Constant.is_service_charge_applicable)
        {
            textView_serviceCharge.setText(Constant.SERVICE_CHARGE_APPLICABLE);
        }
        else
        {
            textView_serviceCharge.setText(Constant.SERVICE_CHARGE_NOT_APPLICABLE);
        }
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

    private Boolean validate() {
        if (editText_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(CheckOut.this, getResources().getString(R.string.address_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadCheckOutApi() {
        if (methods.isNetworkAvailable()) {
            loadCheckOut = new LoadCheckOut(CheckOut.this, new LoginListener() {
                @Override
                public void onStart() {
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String message) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Toast.makeText(CheckOut.this, message, Toast.LENGTH_SHORT).show();
                    if (success.equals("0")) {
                        openErrorDialog(getString(R.string.error_order));
//                    Toast.makeText(CheckOut.this, getString(R.string.error_order), Toast.LENGTH_SHORT).show();
                    } else {
                        Constant.isCartRefresh = true;
                        Constant.menuCount = 0;
                        Constant.arrayList_cart.clear();
                        Constant.isFromCheckOut = true;
                        openOrderSuccessDialog();
                    }
                }
            });
            loadCheckOut.execute(Constant.URL_CHECKOUT_1 + Constant.itemUser.getId() + Constant.URL_CHECKOUT_2 + address + Constant.URL_CHECKOUT_3 + comment + Constant.URL_CHECKOUT_4 + cart_ids);
        } else {
            openErrorDialog(getString(R.string.net_not_conn));
        }
    }

    private void openOrderSuccessDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_pay_suc, null);
        dialogBuilder.setView(dialogView);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_up);
        anim.setInterpolator(new OvershootInterpolator());

        ImageView imageView = dialogView.findViewById(R.id.iv_pay_suc);
        Button button_close = dialogView.findViewById(R.id.button_close);
        imageView.startAnimation(anim);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().getFragments().clear();
                Intent intent = new Intent(CheckOut.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void openErrorDialog(String message) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialog_pay_suc, null);
        dialogBuilder.setView(dialogView);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.scale_up);
        anim.setInterpolator(new OvershootInterpolator());

        ImageView imageView = dialogView.findViewById(R.id.iv_pay_suc);
        TextView textView = dialogView.findViewById(R.id.tv_dialog_suc);
        textView.setText(message);
        imageView.setImageResource(R.drawable.close);
        Button button_close = dialogView.findViewById(R.id.button_close);
        imageView.startAnimation(anim);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}