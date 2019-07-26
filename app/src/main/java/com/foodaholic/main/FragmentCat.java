package com.foodaholic.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodaholic.adapter.AdapterCat;
import com.foodaholic.asyncTask.LoadCat;
import com.foodaholic.interfaces.CategoryListener;
import com.foodaholic.interfaces.InterAdListener;
import com.foodaholic.items.ItemCat;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;
import com.foodaholic.utils.RecyclerItemClickListener;

import java.util.ArrayList;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class FragmentCat extends Fragment {

    Methods methods;
    LoadCat loadCat;
    RecyclerView recyclerView;
    ArrayList<ItemCat> arrayList;
    AdapterCat adapter;
    LinearLayoutManager llm;
    SearchView searchView;
    Menu menu;
    CircularProgressBar progressBar;

    TextView textView_empty;
    LinearLayout ll_empty;
    String errr_msg;
    AppCompatButton button_try;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cat, container, false);

        methods = new Methods(getActivity(), new InterAdListener() {
            @Override
            public void onClick(int position, String type) {
                int real_pos = getPosition(adapter.getID(position));
                Intent intent = new Intent(getActivity(), HotelByCatActivity.class);
                intent.putExtra("cid", arrayList.get(real_pos).getId());
                intent.putExtra("cname", arrayList.get(real_pos).getName());
                startActivity(intent);
            }
        });

        arrayList = new ArrayList<>();

        ll_empty = rootView.findViewById(R.id.ll_empty);
        textView_empty = rootView.findViewById(R.id.textView_empty_msg);
        button_try = rootView.findViewById(R.id.button_empty_try);

        progressBar = rootView.findViewById(R.id.pb_cat);
        recyclerView = rootView.findViewById(R.id.rv_cat);
        llm = new LinearLayoutManager(getActivity());

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        button_try.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCatApi();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                methods.showInterAd(position, "");
            }
        }));

        loadCatApi();

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        this.menu = menu;
        methods.changeCart(menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setOnQueryTextListener(queryTextListener);
    }

    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (adapter != null) {
                if (!searchView.isIconified()) {
                    adapter.getFilter().filter(s);
                    adapter.notifyDataSetChanged();
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

    private void loadCatApi() {
        if (methods.isNetworkAvailable()) {
            loadCat = new LoadCat(getActivity(), new CategoryListener() {
                @Override
                public void onStart() {
                    arrayList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    ll_empty.setVisibility(View.GONE);
                }

                @Override
                public void onEnd(String success, String message, ArrayList<ItemCat> arrayListCat) {
                    if (getActivity() != null) {
                        if (success.equals("true")) {
                            arrayList.addAll(arrayListCat);
                            errr_msg = getString(R.string.no_cat_found);
                        } else {
                            errr_msg = getString(R.string.error_server);
                        }
                        setAdapter();
                    }
                }
            });

            loadCat.execute(Constant.URL_CAT);
        } else {
            errr_msg = getString(R.string.net_not_conn);
            setAdapter();
        }
    }

    private void setAdapter() {
        adapter = new AdapterCat(arrayList);
        recyclerView.setAdapter(adapter);
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

    private int getPosition(String id) {
        int count = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (id.equals(arrayList.get(i).getId())) {
                count = i;
                break;
            }
        }
        return count;
    }

    @Override
    public void onResume() {
        if (((MainActivity) getActivity()).toolbar != null && menu != null && menu.findItem(R.id.menu_cart_search) != null) {
            methods.changeCart(menu);
        }
        super.onResume();
    }
}