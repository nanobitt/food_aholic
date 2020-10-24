package com.foodaholic.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.foodaholic.asyncTask.LoadAbout;
import com.foodaholic.asyncTask.LoadLoginLocal;
import com.foodaholic.interfaces.AboutListener;
import com.foodaholic.interfaces.LoginListener;
import com.foodaholic.sharedPref.SharePref;
import com.foodaholic.utils.Constant;
import com.foodaholic.utils.Methods;
import com.google.firebase.analytics.FirebaseAnalytics;

public class SplashActivity extends AppCompatActivity {

    SharePref sharePref;
    LoadLoginLocal loadLoginLocal;
    Methods methods;

    LoadAbout loadAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadAboutTask();


    }

    private void loadAboutTask() {
        loadAbout = new LoadAbout(new AboutListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd(String success)
            {
                if(success.equals("1"))
                {
                    if (!Constant.itemAbout.getAppVersion().equals(BuildConfig.VERSION_NAME))
                    {
                        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(SplashActivity.this)
                                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                .setTitle("Update available!")
                                .setMessage("Please update your app to continue")
                                .addButton("UPGRADE", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
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
                                }
                                )
                                .addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
                                finish();
                            }
                    );


                        builder.show();

                    }
                    else
                    {
                        progressToLogin();
                    }

                }

            }
        });
        loadAbout.execute(Constant.URL_ABOUT);
    }

    private void progressToLogin()
    {
        sharePref = new SharePref(this);
        methods = new Methods(SplashActivity.this);

        if (sharePref.getEmail().equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {
            if (methods.isNetworkAvailable()) {
                loadLogin();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void loadLogin() {
        loadLoginLocal = new LoadLoginLocal(this, new LoginListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onEnd(String success, String message) {
                if (success.equals("true")) {
                    if (message.equals("0")) {
                        Toast.makeText(SplashActivity.this, getResources().getString(R.string.email_pass_nomatch), Toast.LENGTH_SHORT).show();
                        openLoginActivity();
                    } else {
                        Constant.isLogged = true;
                        Toast.makeText(SplashActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                    openLoginActivity();
                }
            }
        });

        loadLoginLocal.execute(Constant.URL_LOGIN_1 + sharePref.getEmail() + Constant.URL_LOGIN_2 + sharePref.getPassword());
    }

    private void openLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void openMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
