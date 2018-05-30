package ttc.project.stoku.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.model.Category;
import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.room.StokuDatabase;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.UserItemEntity;

public class FirstActivity extends BaseActivity {

    @BindView(R.id.spinner_curr)
    NiceSpinner spinner_curr;
    @BindView(R.id.spinner_lang) NiceSpinner spinner_lang;

    @BindView(R.id.btn_start)
    Button btn_start;

    @BindView(R.id.view_loading) View view_loading;

    boolean isIndonesian = true;

    StokuViewModel mStokuViewModel;
    private UserItemEntity[] mUserItems;
    private CategoryEntity[] mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsFirst()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else{
            setContentView(R.layout.activity_first);

            ButterKnife.bind(this);
            mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);

            final List<String> datasetLang = new LinkedList<>(Arrays.asList(
                    getString(R.string.lang_ID),
                    getString(R.string.lang_EN)
            ));

            List<String> datasetCurr = new LinkedList<>(Arrays.asList(
                    getString(R.string.curr_IDR),
                    getString(R.string.curr_USD)
            ));

            spinner_lang.attachDataSource(datasetLang);
            spinner_curr.attachDataSource(datasetCurr);

            if (isIndonesian){
                spinner_lang.setSelectedIndex(0);
                spinner_curr.setSelectedIndex(0);
            } else{
                spinner_lang.setSelectedIndex(0);
                spinner_curr.setSelectedIndex(0);
            }

            Locale current = getResources().getConfiguration().locale;
            if (current.equals("in_ID")){
                isIndonesian = true;
                spinner_curr.setSelectedIndex(0);
                spinner_lang.setSelectedIndex(0);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.lang_key), getString(R.string.val_ID));
                editor.apply();
            } else{
                isIndonesian = false;
                spinner_curr.setSelectedIndex(1);
                spinner_lang.setSelectedIndex(1);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.lang_key), getString(R.string.val_EN));
                editor.apply();
            }

            spinner_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        Locale locale = new Locale("ID");
                        Locale.setDefault(locale);
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.lang_key), getString(R.string.val_ID));
                        editor.apply();
                    } else{
                        Locale locale = new Locale("EN");
                        Locale.setDefault(locale);
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.lang_key), getString(R.string.val_EN));
                        editor.apply();
                    }
                    FirstActivity.this.recreate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_loading.setVisibility(View.VISIBLE);
                    btn_start.setVisibility(View.GONE);
                    startApp();
                }
            });
        }
    }
    private void startApp() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int langPosition = spinner_lang.getSelectedIndex();
        String langVal;
        if (langPosition == 0){
            langVal = getString(R.string.val_ID);
        } else{
            langVal = getString(R.string.val_EN);
        }

        int currPosition = spinner_curr.getSelectedIndex();
        String langCurr;
        if (currPosition == 0){
            langCurr = getString(R.string.val_IDR);
        } else{
            langCurr = getString(R.string.val_USD);
        }

        editor.putString(getString(R.string.lang_key), langVal);
        editor.putString(getString(R.string.curr_key), langCurr);
        editor.putBoolean(getString(R.string.first_key), false);
        editor.apply();

//        createExampleItem();
        fetchAllCategories();
    }

    private boolean checkIsFirst() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = sharedPreferences.getBoolean(getString(R.string.first_key), true);
        return isFirst;
    }

    private void createExampleItem(){

        CategoryEntity categoryEntity1 = new CategoryEntity(0, getString(R.string.no_category),getResources().getColor(android.R.color.darker_gray));
        CategoryEntity categoryEntity2 = new CategoryEntity(0, getString(R.string.example_category_1),getResources().getColor(R.color.green));
        CategoryEntity categoryEntity3 = new CategoryEntity(0, getString(R.string.example_category_2),getResources().getColor(R.color.blue));

        mCategories = new CategoryEntity[]{
                categoryEntity1,
                categoryEntity2,
                categoryEntity3
        };

        new insertCategories(mCategories).execute();
    }

    public void fetchAllCategories() {
        new getAllCategoriesAsyncTask().execute();
    }
    class getAllCategoriesAsyncTask extends AsyncTask<Void, Void, List<CategoryEntity>>{
        @Override
        protected void onPostExecute(List<CategoryEntity> categoryEntities) {
            super.onPostExecute(categoryEntities);
            if (categoryEntities.size() == 0){
                createExampleItem();
            }

            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected List<CategoryEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllCategories();
        }
    }


    class insertItems extends AsyncTask<Void, Void, Void>{
        UserItemEntity[] mUserItemEntities;

        public insertItems(UserItemEntity[] mUserItemEntities) {
            this.mUserItemEntities = mUserItemEntities;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mStokuViewModel.insertItemWithReturn(mUserItemEntities);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class insertCategories extends AsyncTask<Void, Void, long[]>{
        CategoryEntity[] mCategories;
        insertCategories(CategoryEntity... categoryEntities){
            this.mCategories = categoryEntities;
        }

        @Override
        protected long[] doInBackground(Void... voids) {
            return mStokuViewModel.insertCategoryWithReturn(mCategories);
        }

        @Override
        protected void onPostExecute(long[] insertedIds) {
            super.onPostExecute(insertedIds);
            final UserItemEntity userItem1 = new UserItemEntity(0, insertedIds[1], getString(R.string.example_item_1),getString(R.string.example_notes_1),true);
            final UserItemEntity userItem2 = new UserItemEntity(0, insertedIds[1], getString(R.string.example_item_2),"",true);
            final UserItemEntity userItem3 = new UserItemEntity(0, insertedIds[1], getString(R.string.example_item_3),"",true);

            final UserItemEntity userItem4 = new UserItemEntity(0, insertedIds[2], getString(R.string.example_item_4),"",true);
            final UserItemEntity userItem5 = new UserItemEntity(0, insertedIds[2], getString(R.string.example_item_5),"",true);
            mUserItems = new UserItemEntity[]{
                    userItem1,
                    userItem2,
                    userItem3,
                    userItem4,
                    userItem5
            };
            new insertItems(mUserItems).execute();
        }
    }
}
