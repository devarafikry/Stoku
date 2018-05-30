package ttc.project.stoku.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

import ttc.project.stoku.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(updateBaseContextLocale(base));
    }

    private Context updateBaseContextLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.getString(context.getString(R.string.lang_key),null)==null){
            Locale current = context.getResources().getConfiguration().locale;
            if (current.equals("ID")){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getString(R.string.lang_key), context.getString(R.string.val_ID));
                editor.apply();
            } else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(context.getString(R.string.lang_key), context.getString(R.string.val_EN));
                editor.apply();
            }
        }

        String language = sharedPreferences.getString(context.getString(R.string.lang_key),context.getString(R.string.val_ID)); // Helper method to get saved language from SharedPreferences


        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(context, locale);
        }

        return updateResourcesLocaleLegacy(context, locale);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResourcesLocale(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}
