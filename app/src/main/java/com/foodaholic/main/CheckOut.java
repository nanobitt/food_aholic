package com.foodaholic.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodaholic.adapter.AdapterCheckOut;
import com.foodaholic.asyncTask.LoadCheckOut;
import com.foodaholic.asyncTask.LoadPromoCode;
import com.foodaholic.asyncTask.LoadRestServiceCharge;
import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.interfaces.PromoCodeListener;
import com.foodaholic.interfaces.RestServiceChargeListener;
import com.foodaholic.items.ItemCart;
import com.foodaholic.items.ItemPromo;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckOut extends AppCompatActivity {

    Toolbar toolbar;
    LoadCheckOut loadCheckOut;
    Methods methods;
    ProgressDialog progressDialog;
    private AppCompatButton button_checkout;
    private EditText editText_address, editText_comment, editText_promoCode;
    private TextView textView_total, textView_hotel_name, textView_currency, textView_serviceCharge, textView_discountAmount, textView_PromoName, textView_SubTotalAmount;
    private String comment, address, cart_ids, total, rest_name = "", from = "";

    private LinearLayout ll_discount, ll_promo_code, ll_subtotal;

    CardView cardView_edit;
    RecyclerView recyclerView;
    boolean isOpen = false;

    String promo_id;

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


        ll_discount = findViewById(R.id.ll_discount);
        ll_promo_code = findViewById(R.id.ll_promo_code);
        ll_subtotal = findViewById(R.id.ll_subtotal);
        editText_promoCode = findViewById(R.id.et_promo_code);
        textView_PromoName = findViewById(R.id.tv_promo_name);
        textView_discountAmount = findViewById(R.id.tv_discount_amount);
        textView_SubTotalAmount = findViewById(R.id.tv_subtotal_amount);

        ll_discount.setVisibility(View.GONE);
        ll_subtotal.setVisibility(View.GONE);
        promo_id = "0";

        recyclerView = findViewById(R.id.rv_checkout);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckOut.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        AdapterCheckOut adapterCart = new AdapterCheckOut(CheckOut.this, Constant.arrayList_cart);
        recyclerView.setAdapter(adapterCart);

        disablePromoIfOfferExists();

        textView_currency.setTypeface(null, Typeface.BOLD);
        textView_hotel_name.setText(rest_name);
        textView_hotel_name.setTypeface(textView_hotel_name.getTypeface(), Typeface.BOLD);
        textView_total.setTypeface(textView_hotel_name.getTypeface(), Typeface.BOLD);


        editText_address.setText(Constant.itemUser.getAddress());

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    address = URLEncoder.encode(editText_address.getText().toString());
                    comment = URLEncoder.encode(editText_comment.getText().toString());
                    if (isOpen) {
                        loadCheckOutApi();
                    } else {
                        openErrorDialog("Restaurant is closed now. So can't take order right now. But your cart is saved. Try again later.");
                    }

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

        //Toast.makeText(this, Constant.is_service_charge_applicable + "", Toast.LENGTH_LONG).show();
        loadRestServiceChargeApi();


    }

    private void disablePromoIfOfferExists()
    {
        for (ItemCart menu: Constant.arrayList_cart)
        {
            if(menu.getMenuName().toLowerCase().contains("offer"))
            {
                ll_promo_code.setVisibility(View.GONE);

            }

        }
    }

    public void applyPromo(View view)
    {
        final String promoCode = editText_promoCode.getText().toString().trim();
        String userId = Constant.itemUser.getId();

        LoadPromoCode loadPromoCode = new LoadPromoCode(new PromoCodeListener() {
            @Override
            public void onStart() {
                progressDialog.show();

            }

            @Override
            public void onEnd(String success, String result, String msg, ItemPromo promo) {

                progressDialog.dismiss();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);


                if(result.equals("0"))
                {
                    openErrorDialog(msg);

                }
                else if(promo.getMinimum_order() > Double.parseDouble(total))
                {
                    openErrorDialog("Minimum order amount must be " + promo.getMinimum_order() + " to avail this promo.");

                }
                else
                {
                    textView_discountAmount.setText("-" + promo.getDiscount(total));
                    textView_SubTotalAmount.setText(promo.amountAfterDiscount(total));
                    textView_PromoName.setText("Promo(" + promo.getCode() + "):");

                    ll_subtotal.setVisibility(View.VISIBLE);
                    ll_discount.setVisibility(View.VISIBLE);
                    ll_promo_code.setVisibility(View.GONE);

                    promo_id = promo.getId();



                }



            }
        });
        loadPromoCode.execute(Constant.URL_PROMO_CODE + "promo_code=" + promoCode + "&user_id=" + userId + "&rest_id=" + Constant.arrayList_cart.get(0).getRestId() + "&cat_ids=" + cart_ids);

    }

    private void loadRestServiceChargeApi() {
        LoadRestServiceCharge loadRestServiceCharge = new LoadRestServiceCharge(new RestServiceChargeListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String[] resp) {
                progressDialog.dismiss();

                    textView_serviceCharge.setText(resp[2]);
                    double totalBill = Double.parseDouble(total) + Double.parseDouble(resp[2]);
                    textView_total.setText(String.valueOf(totalBill));

                if (resp[1].equals("1")) {
                    isOpen = true;
                } else {
                    isOpen = false;
                }

            }
        });
        loadRestServiceCharge.execute(Constant.URL_REST_SERVICE_CHARGE + Constant.arrayList_cart.get(0).getRestId());
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
            loadCheckOut.execute(Constant.URL_CHECKOUT_1 + Constant.itemUser.getId() + Constant.URL_CHECKOUT_2 + address + Constant.URL_CHECKOUT_3 + comment + Constant.URL_CHECKOUT_4 + cart_ids + Constant.URL_CHECKOUT_5 + promo_id);
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