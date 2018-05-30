package ttc.project.stoku.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import java.util.Locale;

import butterknife.BindView;
import ttc.project.stoku.AppRater;
import ttc.project.stoku.R;
import ttc.project.stoku.fragment.CatalogFragment;
import ttc.project.stoku.fragment.PromoFragment;
import ttc.project.stoku.fragment.ReportFragment;
import ttc.project.stoku.fragment.StockFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Snackbar snackbar;

    @BindView(R.id.content_frame)
    FrameLayout content_frame;
    private DrawerLayout drawer;
    private int REQUEST_SETTING_LANG = 88;
    private String FRAGMENT_ID = "fragmentId";
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PicassoTools.clearCache(Picasso.get());

        AppRater.app_launched(this);
        MobileAds.initialize(this, getString(R.string.mobile_app_id));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString(getString(R.string.lang_key),getString(R.string.val_ID)); // Helper method to get saved language from SharedPreferences
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.d("FIKZ", String.valueOf(ContextCompat.getColor(this, android.R.color.darker_gray)));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        Fragment fragmentA = getSupportFragmentManager().findFragmentByTag(FRAGMENT_ID);
        if (fragmentA == null) {
            //not exist
            swapFragment(new StockFragment(), getString(R.string.stock_title));
        }
        else{
            //fragment exist
        }
    }

//    private void setLocaleFromPref(){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        String val = sharedPreferences.getString(getString(R.string.lang_key),getString(R.string.val_ID));
//        Locale locale = new Locale(val);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config, null);
//    }



    private void swapFragment(Fragment fragment, String title){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragment, FRAGMENT_ID).commit();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBackToExitPressedOnce){
                finish();
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getString(R.string.back_pressed), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stock) {
            swapFragment(new StockFragment(), getString(R.string.stock_title));
        } else if (id == R.id.nav_report) {
            swapFragment(new ReportFragment(), getString(R.string.report_title));
        } else if (id == R.id.nav_promo) {
            swapFragment(new CatalogFragment(), getString(R.string.catalog_title));
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, REQUEST_SETTING_LANG);
        } else if (id == R.id.nav_rate) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            AppRater.showRateDialog(this, sharedPreferences.edit());
        }
        else if (id == R.id.nav_sync){
            showSettingOptionDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSettingOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.activate_sync));
        builder.setMessage(getString(R.string.activate_sync_message));
//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.activate), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (Build.VERSION.SDK_INT >=23){
                    Intent backupIntent = new Intent(Settings.ACTION_PRIVACY_SETTINGS);
                    startActivity(backupIntent);
                } else{
                    drawer.closeDrawer(GravityCompat.START);
                    Toast.makeText(MainActivity.this, getString(R.string.not_supported_sync), Toast.LENGTH_LONG).show();
                }
//        }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SETTING_LANG){
            finish();
            startActivity(getIntent());
        }
    }

}
