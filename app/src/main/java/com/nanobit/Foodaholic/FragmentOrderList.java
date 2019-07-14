package com.nanobit.Foodaholic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanobit.adapter.AdapterOrderList;
import com.nanobit.asyncTask.LoadOderList;
import com.nanobit.interfaces.OrderListListener;
import com.nanobit.items.ItemOrderList;
import com.nanobit.utils.Constant;
import com.nanobit.utils.Methods;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;


public class FragmentOrderList extends Fragment {

    LoadOderList loadOderList;
    RecyclerView recyclerView;
    AdapterOrderList adapterOrderList;
    Methods methods;
    ArrayList<ItemOrderList> arrayList;
    View view;
    CircularProgressBar progressBar;
    Menu menu;
    SearchView searchView;

    TextView textView_empty;
    LinearLayout ll_empty;
    String errr_msg;
    AppCompatButton button_try;

    //For Refreshing
    Handler handler;
    Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_orderlist, container, false);

        methods = new Methods(getActivity());

        arrayList = new ArrayList<>();
        LinearLayoutManager llm_latest = new LinearLayoutManager(getActivity());

        ll_empty = v.findViewById(R.id.ll_empty);
        textView_empty = v.findViewById(R.id.textView_empty_msg);
        button_try = v.findViewById(R.id.button_empty_try);

        progressBar = v.findViewById(R.id.pb_orderlist);

        view = v.findViewById(R.id.view_orderlist);
        recyclerView = v.findViewById(R.id.rv_orderlist);
        recyclerView.setLayoutManager(llm_latest);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        button_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrderListApi();
            }
        });

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                int real_pos = getPosition(adapterOrderList.getID(position));
//                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
//                Constant.itemOrderList = arrayList.get(real_pos);
//                startActivity(intent);
//            }
//        }));

        final int delay = 15000;
        runnable = new Runnable() {
            @Override
            public void run() {
                loadOrderListApi();
                handler.postDelayed(this, delay);

            }
        };
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);

        loadOrderListApi();

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        methods.changeCart(menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapterOrderList != null) {
                if (!searchView.isIconified()) {
                    adapterOrderList.getFilter().filter(s);
                    adapterOrderList.notifyDataSetChanged();
                }
            }
            return true;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart_search:
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadOrderListApi() {
        if (Constant.isLogged) {
            if (methods.isNetworkAvailable()) {
                loadOderList = new LoadOderList(new OrderListListener() {
                    @Override
                    public void onStart() {
                        arrayList.clear();
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        ll_empty.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEnd(String success, ArrayList<ItemOrderList> arrayListorderLists) {
                        if (getActivity() != null) {
                            if (success.equals("true")) {
                                arrayList.addAll(arrayListorderLists);
                                errr_msg = getString(R.string.no_data_found);
                            } else {
                                errr_msg = getString(R.string.error_server);
                            }
                            setAdapter();
                        }
                    }
                });

                loadOderList.execute(Constant.URL_ORDER_LIST + Constant.itemUser.getId());
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
        adapterOrderList = new AdapterOrderList(getActivity(), arrayList);
        recyclerView.setAdapter(adapterOrderList);
        progressBar.setVisibility(View.GONE);
        setEmpty();
    }

    public void setEmpty() {
        if (arrayList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        } else {
            textView_empty.setText(errr_msg);
            recyclerView.setVisibility(View.GONE);
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        if (((MainActivity) getActivity()).toolbar != null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCart(menu);
        }

        if(Constant.isCancelOrder) {
            if (adapterOrderList != null && adapterOrderList.getItemCount() > 0) {
                adapterOrderList.notifyDataSetChanged();
            }
            Constant.isCancelOrder = false;
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
    }
}