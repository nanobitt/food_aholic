package com.foodaholic.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.foodaholic.asyncTask.LoadAddMenu;
import com.foodaholic.asyncTask.LoadCart;
import com.foodaholic.interfaces.CartListener;
import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.items.ItemCart;
import com.foodaholic.items.ItemMenu;
import com.foodaholic.main.HotelDetailsActivity;
import com.foodaholic.main.R;
import com.foodaholic.main.SearchActivity;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.JsonUtils;
import com.foodaholic.utils.Methods;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterMenubysearch extends BaseQuickAdapter<ItemMenu, BaseViewHolder> {
    Activity activity;
    private ProgressDialog progressDialog;
    private Dialog dialog;
    private Methods methods;

    public AdapterMenubysearch(Activity activity,@Nullable List<ItemMenu> data) {
        super(R.layout.layout_menu, data);
        this.activity = activity;
        methods = new Methods(activity.getApplicationContext());
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ItemMenu itemMenu) {
        ImageView imageView_type = holder.getView(R.id.iv_menu_type);
        TextView textView_title = holder.getView(R.id.tv_menu_name);
        TextView textView_price = holder.getView(R.id.tv_menu_price);
        TextView textView_rest_name = holder.getView(R.id.tv_menu_rest);
        LinearLayout linearLayout =  holder.getView(R.id.ll_menu);
        TextView tv_currency =  holder.getView(R.id.tv);
        ImageView imageView = holder.getView(R.id.iv_menu_image);

        tv_currency.setTypeface(null, Typeface.BOLD);

        if (itemMenu.getType().equals(Constant.TAG_VEG)) {
           imageView_type.setImageResource(R.mipmap.veg);
        } else if (itemMenu.getType().equals(Constant.TAG_NONVEG)) {
            imageView_type.setImageResource(R.mipmap.nonveg);
        }

        textView_title.setText(itemMenu.getName());
        textView_price.setText(itemMenu.getPrice());
        textView_rest_name.setText(itemMenu.getRest_name());
        Picasso.get()
                .load(itemMenu.getImage())
                .placeholder(R.drawable.placeholder_menu)
                .into(imageView);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSelectQuantity(itemMenu);
            }
        });
    }


    /** COPIED FOR ADD TO CART ON CLICK OF MENU
     *
     *
     */

    private void openSelectQuantity(final ItemMenu itemMenu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new Dialog(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            dialog = new Dialog(activity);
        }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_quantity);

        ImageView iv_close = dialog.findViewById(R.id.iv_quantity_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton button_add2cart = dialog.findViewById(R.id.button_quantity_add2cart);
        TextView tv_name = dialog.findViewById(R.id.tv_quantity_menu);
        TextView tv_minus = dialog.findViewById(R.id.tv_quantity_minus);
        TextView tv_plus = dialog.findViewById(R.id.tv_quantity_plus);
        final TextView tv_count = dialog.findViewById(R.id.tv_quantity_count);

        tv_name.setText(itemMenu.getName());
        for (int i = 0; i < Constant.arrayList_cart.size(); i++) {
            if (Constant.arrayList_cart.get(i).getMenuId().equals(itemMenu.getId())) {
                tv_count.setText(Constant.arrayList_cart.get(i).getMenuQty());
                button_add2cart.setText(activity.getString(R.string.update_cart));
                break;
            }
        }

        tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tv_count.getText().toString());
                if (count > 1) {
                    count = count - 1;
                    tv_count.setText(String.valueOf(count));
                }
            }
        });

        tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tv_count.getText().toString());
                count = count + 1;
                tv_count.setText(String.valueOf(count));
            }
        });

        button_add2cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isLogged) {
                    if (methods.isNetworkAvailable()) {
                        if (Constant.arrayList_cart.size() == 0 || Constant.arrayList_cart.get(0).getRestId().equals(itemMenu.getRestId())) {
                            loadAddMenuApi(itemMenu, tv_count.getText().toString());
                        } else {
                            openClearDialg(itemMenu, tv_count.getText().toString());
                        }
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(activity, activity.getString(R.string.not_log), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void openClearDialg(final ItemMenu itemMenu, final String count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.cannot_add_menu_diff_restaurant));
        builder.setPositiveButton(activity.getString(R.string.clear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new LoadClearCart(itemMenu, count).execute(Constant.URL_CLEAR_CART + Constant.itemUser.getId());
            }
        });
        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private class LoadClearCart extends AsyncTask<String, String, String> {

        ItemMenu itemMenu;
        String count;

        ProgressDialog progressDialog = new ProgressDialog(activity);
        String message = "";

        LoadClearCart(ItemMenu itemMenu, String count) {
            this.itemMenu = itemMenu;
            this.count = count;

            progressDialog.setMessage(activity.getString(R.string.clearing_cart));
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String json = JsonUtils.okhttpGET(url);
            try {
                JSONObject jOb = new JSONObject(json);
                JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT);

                String success = "0";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    success = c.getString(Constant.TAG_SUCCESS);
                    message = c.getString(Constant.TAG_MSG);
                }
                return success;
            } catch (Exception ee) {
                ee.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s.equals("1")) {
                Constant.arrayList_cart.clear();
                loadAddMenuApi(itemMenu, count);
            } else {
                Toast.makeText(activity, activity.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    private void loadAddMenuApi(final ItemMenu itemMenu, final String menu_count) {
        LoadAddMenu loadAddMenu = new LoadAddMenu(activity, new LoginListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onEnd(String success, String message) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (success.equals("1")) {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    if (dialog != null) {
                        dialog.dismiss();
                    }

                    if (Constant.arrayList_cart.size() > 0) {
                        for (int i = 0; i < Constant.arrayList_cart.size(); i++) {
                            if (Constant.arrayList_cart.get(i).getMenuId().equals(itemMenu.getId())) {
                                Constant.arrayList_cart.get(i).setMenuQty(menu_count);
                                break;
                            } else if (i == (Constant.arrayList_cart.size() - 1)) {
                                //loadCartApi();
                                Constant.itemRestaurant = itemMenu.getAssociatedRestaurant();
                                Intent intent = new Intent(activity, HotelDetailsActivity.class);
                                activity.startActivity(intent);
                                activity.finish();
//                                Constant.arrayList_cart.add(new ItemCart("", itemMenu.getRestId(), restName, itemMenu.getId(), itemMenu.getName(), itemMenu.getImage(), menu_count, itemMenu.getPrice(), menu_count));
                            }
                        }
                    } else {
                        Constant.itemRestaurant = itemMenu.getAssociatedRestaurant();
                        Intent intent = new Intent(activity, HotelDetailsActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        //loadCartApi();
//                        Constant.arrayList_cart.add(new ItemCart("", itemMenu.getRestId(), restName, itemMenu.getId(), itemMenu.getName(), itemMenu.getImage(), menu_count, itemMenu.getPrice(), menu_count));
                    }
                } else {
                    Toast.makeText(activity, activity.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadAddMenu.execute(Constant.URL_ADD_MENU_1 + Constant.itemUser.getId() + Constant.URL_ADD_MENU_2 + itemMenu.getRestId() + Constant.URL_ADD_MENU_3 + itemMenu.getId() + Constant.URL_ADD_MENU_4 + itemMenu.getName() + Constant.URL_ADD_MENU_5 + menu_count + Constant.URL_ADD_MENU_6 + itemMenu.getPrice());
    }


    private void loadCartApi() {
        if (methods.isNetworkAvailable()) {
            LoadCart loadCart = new LoadCart(new CartListener() {
                @Override
                public void onStart() {
                    Constant.arrayList_cart.clear();
                    progressDialog.show();
                }

                @Override
                public void onEnd(String success, String message, ArrayList<ItemCart> array) {
                    if (success.equals("true")) {
                        Constant.arrayList_cart.addAll(array);


                    } else {
//                        errr_msg = getString(R.string.error_server);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                    ((SearchActivity) activity).changeMenu();

                }
            });
            loadCart.execute(Constant.URL_CART + Constant.itemUser.getId());
        } else {
//                errr_msg = getString(R.string.net_not_conn);
//                setAdapter();
        }
    }





}
