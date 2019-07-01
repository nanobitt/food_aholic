package com.vpapps.Foodaholic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.irfaan008.irbottomnavigation.SpaceItem;
import com.irfaan008.irbottomnavigation.SpaceNavigationView;
import com.irfaan008.irbottomnavigation.SpaceOnClickListener;
import com.vpapps.asyncTask.LoadAbout;
import com.vpapps.interfaces.AboutListener;
import com.vpapps.interfaces.AdConsentListener;
import com.vpapps.utils.AdConsent;
import com.vpapps.utils.Constant;
import com.vpapps.utils.DBHelper;
import com.vpapps.utils.Methods;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    DBHelper dbHelper;
    LoadAbout loadAbout;
    AdConsent adConsent;
    Methods methods;
    DrawerLayout drawer;
    TextView textView_header_message;
    MenuItem menuItem_login;
    FragmentManager fm;
    ProgressDialog pbar;
    String selectedFragment = "";
    NavigationView navigationView;
    //    LinearLayout ll_adView_main;
    SpaceNavigationView spaceNavigationView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        pbar = new ProgressDialog(this);
        pbar.setMessage(getString(R.string.loading));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);
        methods = new Methods(this);
        methods.setStatusColor(getWindow(), toolbar);
        methods.forceRTLIfSupported(getWindow());

//        ll_adView_main = findViewById(R.id.ll_adView_main);
        adConsent = new AdConsent(this, new AdConsentListener() {
            @Override
            public void onConsentUpdate() {
//                methods.showBannerAd(ll_adView_main);
            }
        });

        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.home), R.mipmap.home));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.categories), R.mipmap.cat));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.orderlist), R.mipmap.list));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.profile), R.mipmap.profile));

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        FragmentHome f1 = new FragmentHome();
                        loadFrag(f1, getString(R.string.home), fm);
                        toolbar.setTitle(getString(R.string.app_name));
                        break;
                    case 1:
                        FragmentCat fcat = new FragmentCat();
                        loadFrag(fcat, getString(R.string.categories), fm);
                        toolbar.setTitle(getString(R.string.categories));
                        break;
                    case 2:
                        FragmentOrderList forder = new FragmentOrderList();
                        loadFrag(forder, getString(R.string.orderlist), fm);
                        toolbar.setTitle(getString(R.string.orderlist));
                        break;
                    case 3:
                        FragmentProfile fprof = new FragmentProfile();
                        loadFrag(fprof, getString(R.string.profile), fm);
                        toolbar.setTitle(getString(R.string.profile));
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });

        if (methods.isNetworkAvailable()) {
            loadAboutTask();
        } else {
            adConsent.checkForConsent();
            dbHelper.getAbout();
            methods.showToast(getString(R.string.net_not_conn));
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setDrawerIndicatorEnabled(false);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        toggle.setHomeAsUpIndicator(R.mipmap.nav);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuItem_login = navigationView.getMenu().findItem(R.id.nav_login);
        textView_header_message = navigationView.getHeaderView(0).findViewById(R.id.tv_header_msg);

        changeLoginTitle();

        if (!Constant.isFromCheckOut) {
            FragmentHome f1 = new FragmentHome();
            loadFrag(f1, getString(R.string.home), fm);
            getSupportActionBar().setTitle(getResources().getString(R.string.home));
            navigationView.setCheckedItem(R.id.nav_home);
        } else {
            Constant.isFromCheckOut = false;
            spaceNavigationView.changeCurrentItem(2);
        }

        checkPer();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                FragmentHome f1 = new FragmentHome();
                loadFrag(f1, getString(R.string.home), fm);
//                spaceNavigationView.changeCurrentItem(0);
//                toolbar.setTitle(getString(R.string.app_name));
                break;
            case R.id.nav_fav:
                Intent intent_fav = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent_fav);
                break;
            case R.id.nav_hotel_list:
                Intent intent_hotel = new Intent(MainActivity.this, HotelByLatestActivity.class);
                intent_hotel.putExtra("type", getString(R.string.hotel_list));
                startActivity(intent_hotel);
                break;
            case R.id.nav_rate:
                final String appName = getPackageName();//your application package name i.e play store application url
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + appName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id="
                                    + appName)));
                }
                break;
            case R.id.nav_shareapp:
                Intent ishare = new Intent(Intent.ACTION_SEND);
                ishare.setType("text/plain");
                ishare.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + " - http://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(ishare);
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_login:
                methods.clickLogin();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFrag(Fragment f1, String name, FragmentManager fm) {
        selectedFragment = name;
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.frame_nav, f1, name);
        ft.commit();
        getSupportActionBar().setTitle(name);
    }

    private void changeLoginTitle() {
        if (Constant.isLogged) {
            menuItem_login.setTitle(getString(R.string.logout));
            menuItem_login.setIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.logout));
            textView_header_message.setText(getString(R.string.hi) + " " + Constant.itemUser.getName());
        } else {
            menuItem_login.setTitle(getString(R.string.login));
            menuItem_login.setIcon(ContextCompat.getDrawable(MainActivity.this, R.mipmap.login));
            textView_header_message.setText(getString(R.string.hi) + " " + getString(R.string.guest));
        }
    }

    private void loadAboutTask() {
        loadAbout = new LoadAbout(new AboutListener() {
            @Override
            public void onStart() {
                pbar.show();
            }

            @Override
            public void onEnd(String success) {
                if (pbar.isShowing()) {
                    pbar.dismiss();
                }

                adConsent.checkForConsent();
                dbHelper.addtoAbout();
            }
        });
        loadAbout.execute(Constant.URL_ABOUT);
    }

    public void checkPer() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (selectedFragment.equals(getString(R.string.home))) {
                exitDialog();
            } else {
                spaceNavigationView.changeCurrentItem(0);
            }
        }
    }

    private void exitDialog() {
        AlertDialog.Builder alert;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
        } else {
            alert = new AlertDialog.Builder(MainActivity.this);
        }

        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.sure_exit));
        alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }
}