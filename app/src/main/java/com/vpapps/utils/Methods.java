package com.vpapps.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.vpapps.Foodaholic.CartActivity;
import com.vpapps.Foodaholic.LoginActivity;
import com.vpapps.Foodaholic.R;
import com.vpapps.interfaces.InterAdListener;
import com.vpapps.items.ItemRestaurant;
import com.vpapps.items.ItemUser;
import com.vpapps.sharedPref.SharePref;

import java.util.Calendar;


public class Methods {

    private Context _context;
    private InterstitialAd mInterstitial;
    private InterAdListener interAdListener;

    public Methods(Context context) {
        this._context = context;
    }

    public Methods(Context context, InterAdListener interAdListener) {
        this._context = context;
        loadInter();
        this.interAdListener = interAdListener;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) || (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting());
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    private static void openLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("from", "app");
        context.startActivity(intent);
    }

    private void logout(Activity activity) {
        changeRemPass();
        Constant.isLogged = false;
        Constant.itemUser = new ItemUser("", "", "", "", "", "");
        Constant.menuCount = 0;
        Intent intent1 = new Intent(_context, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        _context.startActivity(intent1);
        activity.finish();
    }

    public void clickLogin() {
        if (Constant.isLogged) {
            logout((Activity) _context);
            ((Activity) _context).finish();
            Toast.makeText(_context, _context.getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        } else {
            openLogin(_context);
        }
    }

    public void setStatusColor(Window window, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(_context.getResources().getColor(R.color.status_bar));
            if (toolbar != null) {
                toolbar.setElevation(10);
            }
        }
    }

    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packagename, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void changeRemPass() {
        SharePref sharePref = new SharePref(_context);
        sharePref.setSharedPreferences("", "");
    }

    public void changeCart(Menu menu) {
        View cart = menu.findItem(R.id.menu_cart_search).getActionView();
        if (Constant.isLogged) {
            TextView textView = cart.findViewById(R.id.textView_menu_no);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setText("" + Constant.menuCount);

            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.isLogged) {
                        Intent intent = new Intent(_context, CartActivity.class);
                        _context.startActivity(intent);
                    } else {
                        Intent i = new Intent(_context, LoginActivity.class);
                        _context.startActivity(i);
                    }
                }
            });
        } else {
            MenuItem menuItem = menu.findItem(R.id.menu_cart_search);
            menuItem.setVisible(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void forceRTLIfSupported(Window window) {
        if (_context.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    public String getPathImage(Uri uri) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String filePath = "";
                String wholeID = DocumentsContract.getDocumentId(uri);

                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = _context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;
            } else {

                if (uri == null) {
                    return null;
                }
                // try to retrieve the image from the media store first
                // this will only work for images selected from gallery
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = _context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String retunn = cursor.getString(column_index);
                    cursor.close();
                    return retunn;
                }
                // this is our fallback here
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (uri == null) {
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = _context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String returnn = cursor.getString(column_index);
                cursor.close();
                return returnn;
            }
            // this is our fallback here
            return uri.getPath();
        }
    }

    public String getOpenTime(ItemRestaurant itemRestaurant) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                return itemRestaurant.getSunday();
            case Calendar.MONDAY:
                return itemRestaurant.getMonday();
            case Calendar.TUESDAY:
                return itemRestaurant.getTuesday();
            case Calendar.WEDNESDAY:
                return itemRestaurant.getWednesday();
            case Calendar.THURSDAY:
                return itemRestaurant.getThursday();
            case Calendar.FRIDAY:
                return itemRestaurant.getFriday();
            case Calendar.SATURDAY:
                return itemRestaurant.getSaturday();
            default:
                return "";
        }
    }

    public void showToast(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 0;
    }

    public String getCartIds() {
        String ids = "";

        if (Constant.arrayList_cart.size() > 0) {
            ids = Constant.arrayList_cart.get(0).getId();
            for (int i = 1; i < Constant.arrayList_cart.size(); i++) {
                ids = ids + "," + Constant.arrayList_cart.get(i).getId();
            }
        }
        return ids;
    }

    private void showPersonalizedAds(LinearLayout linearLayout) {
        if (Constant.isBannerAd) {
            AdView adView = new AdView(_context);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.setAdUnitId(Constant.ad_banner_id);
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    private void showNonPersonalizedAds(LinearLayout linearLayout) {
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        if (Constant.isBannerAd) {
            AdView adView = new AdView(_context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(Constant.ad_banner_id);
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        }
    }

    public void showBannerAd(LinearLayout linearLayout) {
        if (isNetworkAvailable()) {
            if (ConsentInformation.getInstance(_context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                showNonPersonalizedAds(linearLayout);
            } else {
                showPersonalizedAds(linearLayout);
            }
        }
    }

    private void loadInter() {
        mInterstitial = new InterstitialAd(_context);
        if (Constant.isInterAd) {
            AdRequest adRequest;
            if (ConsentInformation.getInstance(_context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                adRequest = new AdRequest.Builder().build();
            } else {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                        .build();
            }
            mInterstitial.setAdUnitId(Constant.ad_inter_id);
            mInterstitial.loadAd(adRequest);
        }
    }

    public void showInterAd(final int pos, final String type) {
        Constant.adCount = Constant.adCount + 1;
        if (Constant.adCount % Constant.adShow == 0) {
            mInterstitial.setAdListener(new AdListener() {

                @Override
                public void onAdClosed() {
                    interAdListener.onClick(pos, type);
                    super.onAdClosed();
                }
            });
            if (mInterstitial.isLoaded()) {
                mInterstitial.show();
            } else {
                interAdListener.onClick(pos, type);
            }
            loadInter();
        } else {
            interAdListener.onClick(pos, type);
        }
    }

    public void openSearchFilter() {
        new AlertDialog.Builder(_context)
                .setCancelable(false)
                .setTitle(_context.getString(R.string.filter))
                .setSingleChoiceItems(Constant.search_type_array, Constant.search_type_pos, null)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Constant.search_type_pos = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        if(Constant.search_type_pos == 0) {
                            Constant.search_type = "Restaurant";
                        } else {
                            Constant.search_type = "menu";
                        }
                    }
                })
                .show();
    }
}