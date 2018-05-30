package ttc.project.stoku.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.callback.CategoryInterface;
import ttc.project.stoku.model.Category;
import ttc.project.stoku.R;
import ttc.project.stoku.adapter.CategoryAdapter;
import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemEntity;
import ttc.project.stoku.util.SnackbarUtil;

public class ManageCategoryActivity extends BaseActivity implements CategoryInterface{

    private static final int INSERT_CATEGORY_REQUEST_CODE = 88;
    private static final int EDIT_CATEGORY_REQUEST_CODE = 99;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;
    @BindView(R.id.rootView)
    View rootView;
    Snackbar snackbar;

    ArrayList<Category> categories = new ArrayList<>();

    private StokuViewModel mStokuViewModel;
    private ArrayList<UserItem> userItems;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(getString(R.string.manage_category));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categories = getDummyCategories();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        categoryAdapter = new CategoryAdapter(this, this);
//        categoryAdapter.swapData(categories);
        rv_category.setAdapter(categoryAdapter);
        rv_category.setLayoutManager(linearLayoutManager);

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);

        fetchAllCategories();

    }



    private ArrayList<Category> getCategoryData(List<CategoryEntity> categoryEntities) {
        categories.clear();
        for (CategoryEntity category : categoryEntities){
            Category data = new Category(
                    category.getId(),
                    category.getCategory_name(),
                    category.getColorId()
            );
            categories.add(data);
        }
        return categories;
    }

    public ArrayList<Category> getDummyCategories() {
        Category category1 = new Category(1l,"Kategori 1", ContextCompat.getColor(this, R.color.amber));
        Category category2 = new Category(1l,"Kategori 2", ContextCompat.getColor(this, R.color.blue_grey));
        Category category3 = new Category(1l,"Kategori 3", ContextCompat.getColor(this, R.color.green));

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);

        return categories;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(this, AddEditCategoryActivity.class);
            intent.putExtra(AddEditCategoryActivity.EXTRA_TYPE, getString(R.string.key_add));
            startActivityForResult(intent, INSERT_CATEGORY_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INSERT_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String command = data.getStringExtra(AddEditCategoryActivity.EXTRA_COMMAND);
            if(command.equals(getString(R.string.key_add))){
                String categoryName = data.getStringExtra(AddEditCategoryActivity.EXTRA_CATEGORY_NAME);
                int colorId = data.getIntExtra(AddEditCategoryActivity.EXTRA_COLOR_ID, ContextCompat.getColor(this, R.color.red));
                mStokuViewModel.insertCategory(new CategoryEntity(0, categoryName, colorId));

                fetchAllCategoriesForAdd();
                rv_category.smoothScrollToPosition(categories.size());
//                categoryAdapter.notifyItemRangeChanged(deletedId, categories.size());
                SnackbarUtil.showSnackbar(this, rootView, getString(R.string.success_create_category),snackbar, Snackbar.LENGTH_LONG);
            }
        } else if(requestCode == EDIT_CATEGORY_REQUEST_CODE && resultCode == RESULT_OK){
            String command = data.getStringExtra(AddEditCategoryActivity.EXTRA_COMMAND);
            if(command.equals(getString(R.string.key_edit))){
                String categoryName = data.getStringExtra(AddEditCategoryActivity.EXTRA_CATEGORY_NAME);
                long categoryId = data.getLongExtra(AddEditCategoryActivity.EXTRA_CATEGORY_ID, 0);
                int colorId = data.getIntExtra(AddEditCategoryActivity.EXTRA_COLOR_ID, ContextCompat.getColor(this, R.color.red));
                mStokuViewModel.updateCategory(new CategoryEntity(categoryId, categoryName, colorId));
                int position = data.getIntExtra(AddEditCategoryActivity.EXTRA_CATEGORY_POSITION, -1);

                Category category = new Category(categoryId, categoryName, colorId);
                categories.set(position, category);
                categoryAdapter.setData(categories);
                categoryAdapter.notifyItemChanged(position);
                SnackbarUtil.showSnackbar(ManageCategoryActivity.this, rootView, getString(R.string.success_update_category),snackbar, Snackbar.LENGTH_LONG);
            } else if(command.equals(getString(R.string.key_delete))){
                String categoryName = data.getStringExtra(AddEditCategoryActivity.EXTRA_CATEGORY_NAME);
                long categoryId = data.getLongExtra(AddEditCategoryActivity.EXTRA_CATEGORY_ID, 0);
                int colorId = data.getIntExtra(AddEditCategoryActivity.EXTRA_COLOR_ID, ContextCompat.getColor(this, R.color.red));
                saveDataFromCategory(categoryId, new CategoryEntity(categoryId, categoryName, colorId));
                int deletedId = data.getIntExtra(AddEditCategoryActivity.EXTRA_CATEGORY_POSITION, -1);
                categoryAdapter.notifyItemRemoved(deletedId);
                categories.remove(deletedId);
                categoryAdapter.notifyItemRangeChanged(deletedId, categories.size());

//                categories.remove(deletedId);
//                categoryAdapter.notifyItemRemoved(deletedId);
//                categoryAdapter.notifyItemRangeChanged(deletedId, categories.size());
                SnackbarUtil.showSnackbar(ManageCategoryActivity.this, rootView, getString(R.string.success_delete_category),snackbar, Snackbar.LENGTH_LONG);
            }
        }
    }

    private void deleteCategoryData(final CategoryEntity categoryEntity, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_category_title));
        String deleteMessage = String.format(getString(R.string.delete_category_message), categoryEntity.getCategory_name());
        builder.setMessage(deleteMessage);

//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                saveDataFromCategory(categoryEntity.getId(), categoryEntity);
                categoryAdapter.notifyItemRemoved(position);
                categories.remove(position);
                categoryAdapter.notifyItemRangeChanged(position, categories.size());
//                categoryAdapter.notifyDataSetChanged();
                SnackbarUtil.showSnackbar(ManageCategoryActivity.this, rootView, getString(R.string.success_delete_category),snackbar, Snackbar.LENGTH_LONG);
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

    public void saveDataFromCategory(long categoryId, CategoryEntity categoryEntity) {
        new getAllItemAsyncTask(categoryId, categoryEntity).execute();
    }

    class getAllItemAsyncTask extends AsyncTask<Void, Void, List<UserItemCategoryJoinEntity>> {
        long mCategoryId;
        CategoryEntity mCategoryEntity;

        getAllItemAsyncTask(long categoryId, CategoryEntity categoryEntity){
            this.mCategoryId = categoryId;
            this.mCategoryEntity = categoryEntity;
        }

        @Override
        protected void onPostExecute(List<UserItemCategoryJoinEntity> userItemCategoryJoinEntities) {
            super.onPostExecute(userItemCategoryJoinEntities);
            List<UserItemEntity> userItemEntities = new ArrayList<>();
            for (UserItemCategoryJoinEntity item : userItemCategoryJoinEntities){
                if(item.getCategory_id() == mCategoryId){
                    item.setCategory_id(1);
                }
                userItemEntities.add(new UserItemEntity(item.getId(), item.getCategory_id(), item.getItem_name(), item.getNotes(), item.isAvailable()));
            }
            UserItemEntity[] entities = new UserItemEntity[userItemEntities.size()];
            entities = userItemEntities.toArray(entities);
            mStokuViewModel.updateItems(entities);

            mStokuViewModel.deleteCategory(mCategoryEntity);
        }

        @Override
        protected List<UserItemCategoryJoinEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllItems();
        }
    }

    @Override
    public void editCategory(Category category, int position) {
        Intent intent = new Intent(this, AddEditCategoryActivity.class);
        intent.putExtra(AddEditCategoryActivity.EXTRA_TYPE, this.getString(R.string.key_edit));
        intent.putExtra(AddEditCategoryActivity.EXTRA_CATEGORY_NAME, category.getName());
        intent.putExtra(AddEditCategoryActivity.EXTRA_COLOR_ID, category.getColorId());
        intent.putExtra(AddEditCategoryActivity.EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(AddEditCategoryActivity.EXTRA_CATEGORY_POSITION, position);
        startActivityForResult(intent, EDIT_CATEGORY_REQUEST_CODE);
    }

    @Override
    public void deleteCategory(Category category, int position) {
        deleteCategoryData(new CategoryEntity(category.getId(), category.getName(), category.getColorId()), position);
    }

    public void fetchAllCategories() {
        new getAllCategoriesAsyncTask().execute();
    }
    class getAllCategoriesAsyncTask extends AsyncTask<Void, Void, List<CategoryEntity>>{
        @Override
        protected void onPostExecute(List<CategoryEntity> CategoryEntity) {
            super.onPostExecute(CategoryEntity);
            categories = getCategoriesData(CategoryEntity);
            categoryAdapter.swapData(categories);
        }

        @Override
        protected List<CategoryEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllCategories();
        }
    }

    public void fetchAllCategoriesForAdd() {
        new getAllCategoriesForAddAsyncTask().execute();
    }
    class getAllCategoriesForAddAsyncTask extends AsyncTask<Void, Void, List<CategoryEntity>>{
        @Override
        protected void onPostExecute(List<CategoryEntity> CategoryEntity) {
            super.onPostExecute(CategoryEntity);
            categories = getCategoriesData(CategoryEntity);
            categoryAdapter.setData(categories);
            categoryAdapter.notifyItemInserted(categories.size());
        }

        @Override
        protected List<CategoryEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllCategories();
        }
    }

    private ArrayList<Category> getCategoriesData(List<CategoryEntity> categoryEntity) {
        ArrayList<Category> categories = new ArrayList<>();
        for (CategoryEntity entity :categoryEntity){
            Category category = new Category(entity.getId(), entity.getCategory_name(), entity.getColorId());
            categories.add(category);
        }
        return categories;
    }
}
