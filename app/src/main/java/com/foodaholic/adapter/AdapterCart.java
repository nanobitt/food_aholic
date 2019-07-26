package com.foodaholic.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodaholic.asyncTask.LoadAddMenu;
import com.foodaholic.asyncTask.LoadDeleteMenu;
import com.foodaholic.main.CartActivity;
import com.foodaholic.main.R;
import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.items.ItemCart;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;

import java.util.ArrayList;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private Context context;
    private ArrayList<ItemCart> arrayList;
    private ProgressDialog progressDialog;
    private Methods method;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title, textView_quantity, textView_price, textView_minus, textView_plus, tv_currency;

        MyViewHolder(View view) {
            super(view);
            textView_title = view.findViewById(R.id.tv_cart_menu_name);
            textView_quantity = view.findViewById(R.id.tv_menu_qty);
            textView_price = view.findViewById(R.id.tv_cart_price);
            textView_minus = view.findViewById(R.id.tv_cart_minus);
            textView_plus = view.findViewById(R.id.tv_cart_plus);
            tv_currency = view.findViewById(R.id.tv);

            tv_currency.setTypeface(null, Typeface.BOLD);
        }
    }

    public AdapterCart(Context context, ArrayList<ItemCart> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        method = new Methods(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        float tot = Float.parseFloat(arrayList.get(position).getMenuPrice()) * Integer.parseInt(arrayList.get(position).getMenuQty());

        holder.textView_title.setText(arrayList.get(position).getMenuName());
        holder.textView_quantity.setText(arrayList.get(position).getMemuTempQty());
        holder.textView_price.setText(String.valueOf(tot));

        holder.textView_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.textView_quantity.getText().toString());
                if (count > 1) {
                    count = count - 1;
                    holder.textView_quantity.setText(String.valueOf(count));
                    arrayList.get(holder.getAdapterPosition()).setMemuTempQty(String.valueOf(count));
                    loadAddMenu(holder.getAdapterPosition());
                } else if (count == 1) {
                    openDeleteDialog(holder.getAdapterPosition());
                }
            }
        });

        holder.textView_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(holder.textView_quantity.getText().toString());
                if (count < 30) {
                    count = count + 1;
                    holder.textView_quantity.setText(String.valueOf(count));
                    arrayList.get(holder.getAdapterPosition()).setMemuTempQty(String.valueOf(count));
                    loadAddMenu(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, context.getString(R.string.max_quantity_reached), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDeleteDialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(arrayList.get(pos).getMenuName());
        builder.setMessage(context.getString(R.string.remove_menu_from_cart));
        builder.setPositiveButton(context.getString(R.string.remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMenu(pos);
            }
        });
        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void loadAddMenu(final int pos) {
        if (method.isNetworkAvailable()) {
            LoadAddMenu loadAddMenu = new LoadAddMenu(context, new LoginListener() {
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }

                    arrayList.get(pos).setMenuQty(arrayList.get(pos).getMemuTempQty());
                    notifyItemChanged(pos);

                    changeTotal();
                }
            });
            loadAddMenu.execute(Constant.URL_ADD_MENU_1 + Constant.itemUser.getId() + Constant.URL_ADD_MENU_2 + arrayList.get(pos).getRestId() + Constant.URL_ADD_MENU_3 + arrayList.get(pos).getMenuId() + Constant.URL_ADD_MENU_4 + arrayList.get(pos).getMenuName() + Constant.URL_ADD_MENU_5 + arrayList.get(pos).getMemuTempQty() + Constant.URL_ADD_MENU_6 + arrayList.get(pos).getMenuPrice());
        } else {
            Toast.makeText(context, context.getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteMenu(final int pos) {
        if (method.isNetworkAvailable()) {
            LoadDeleteMenu loadDeleteMenu = new LoadDeleteMenu(context, new LoginListener() {
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
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        arrayList.remove(pos);
                        notifyDataSetChanged();

                        Constant.menuCount = Constant.menuCount - 1;
                        changeTotal();
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_server), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loadDeleteMenu.execute(Constant.URL_DELETE_ITEM_CART + arrayList.get(pos).getId());
        } else {
            Toast.makeText(context, context.getString(R.string.net_not_conn), Toast.LENGTH_SHORT).show();
        }
    }

    private void changeTotal() {
        float total = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            total = total + (Float.parseFloat(arrayList.get(i).getMenuPrice()) * Float.parseFloat(arrayList.get(i).getMenuQty()));
        }
        CartActivity.textView_total.setText(String.valueOf(total));
        if (arrayList.size() == 0) {
            ((CartActivity) context).hideView();
        }
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}