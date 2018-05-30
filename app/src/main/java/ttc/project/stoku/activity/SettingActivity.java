package ttc.project.stoku.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ttc.project.stoku.R;
import ttc.project.stoku.model.Category;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.CategoryEntity;

public class SettingActivity extends BaseActivity {

    private String language;
    private StokuViewModel mStokuViewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setTitle(getString(R.string.setting_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        language = sharedPreferences.getString(this.getString(R.string.lang_key),this.getString(R.string.val_ID));

//        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void setLanguageSettingActivity(){
        String newLang= sharedPreferences.getString(SettingActivity.this.getString(R.string.lang_key),SettingActivity.this.getString(R.string.val_ID));

        if (newLang.equals(getString(R.string.val_ID))){
            CategoryEntity categoryEntity = new CategoryEntity(
                    1,
                    getString(R.string.no_category_id),
                    getResources().getColor(android.R.color.darker_gray));
            mStokuViewModel.updateCategory(categoryEntity);
        } else{
            CategoryEntity categoryEntity = new CategoryEntity(
                    1,
                    getString(R.string.no_category_en),
                    getResources().getColor(android.R.color.darker_gray));
            mStokuViewModel.updateCategory(categoryEntity);
        }

        recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                if (key.equals(getString(R.string.lang_key))){
//
//                }
//            }
//        };
//    }
}
