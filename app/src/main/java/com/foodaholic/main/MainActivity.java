package com.foodaholic.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.irfaan008.irbottomnavigation.SpaceItem;
import com.irfaan008.irbottomnavigation.SpaceNavigationView;
import com.irfaan008.irbottomnavigation.SpaceOnClickListener;
import com.foodaholic.asyncTask.LoadAbout;
import com.foodaholic.interfaces.AboutListener;

import com.foodaholic.utils.Constant;
import com.foodaholic.utils.DBHelper;
import com.foodaholic.utils.Methods;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    DBHelper dbHelper;
    LoadAbout loadAbout;
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

    int lastClick = 0;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //Crashlytics.getInstance().crash();

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


        spaceNavigationView = findViewById(R.id.space);
        //spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.home), R.mipmap.home));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.hotel_list), R.mipmap.cat));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.orderlist), R.mipmap.list));
        spaceNavigationView.addSpaceItem(new SpaceItem(getString(R.string.offers), R.mipmap.restaurant));

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
                        lastClick = 0;
                        break;
                    case 1:

                        Intent intent_hotel = new Intent(MainActivity.this, HotelByLatestActivity.class);
                        intent_hotel.putExtra("type", getString(R.string.hotel_list));
                        startActivity(intent_hotel);
                        lastClick = 0;
                        break;
                    case 2:
                        FragmentOrderList forder = new FragmentOrderList();
                        loadFrag(forder, getString(R.string.orderlist), fm);
                        toolbar.setTitle(getString(R.string.orderlist));
                        lastClick = 2;
                        break;
                    case 3:

                        Intent offer_intent = new Intent(MainActivity.this, OffersAndPromotionsActivity.class);
                        startActivity(offer_intent);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });

        if (methods.isNetworkAvailable()) {
            //loadAboutTask();
        } else {

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

            case R.id.nav_profile:
                FragmentProfile profileFragment = new FragmentProfile();
                loadFrag(profileFragment, getString(R.string.profile), fm);
                spaceNavigationView.changeCurrentItem(-1);
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
            case R.id.facebook:
                openFacebookPage();
                break;
            case R.id.call_us:
                call_us();
                break;


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFacebookPage() {

        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/690429794442099")));
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.itemAbout.getFacebook_link())));
        }
    }

    private void call_us() {
        Intent intent = new Intent(Intent.ACTION_DIAL);

        intent.setData(Uri.parse("tel:" + Constant.itemAbout.getContact()));
        startActivity(intent);
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pbar != null && pbar.isShowing()) {
            pbar.dismiss();
        }
    }

    public void checkPer() {
//        if ((ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) || ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.CALL_PHONE") != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CALL_PHONE"}, 1);
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        spaceNavigationView.changeCurrentItem(lastClick);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        boolean canUseExternalStorage = false;
//
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    canUseExternalStorage = true;
//                }
//
//                if (!canUseExternalStorage) {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

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

        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Are you sure to exit?")

                .addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {

                    finish();
                })
                .addButton("CANCEL", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {

                    dialog.dismiss();
                })
                .addButton("RATE US", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
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
                    dialog.dismiss();
                });

// Show the alert
        builder.show();


        /**
         final AlertDialog.Builder alert;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         alert = new AlertDialog.Builder(MainActivity.this, R.style.ThemeDialog);
         } else {
         alert = new AlertDialog.Builder(MainActivity.this);
         }


         alert.setTitle(getString(R.string.sure_exit));
         alert.setItems(new CharSequence[]
         {"Exit", "Cancel", "Rate Us"},
         new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
         // The 'which' argument contains the index position
         // of the selected item
         switch (which) {
         case 0:
         finish();
         break;
         case 1:
         break;
         case 2:
         Toast.makeText(MainActivity.this, "clicked 3", Toast.LENGTH_SHORT).show();
         break;

         }
         }
         });

         alert.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {
        finish();
        }
        });
         alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {

        }
        });

         alert.show();
         ***/
    }
}