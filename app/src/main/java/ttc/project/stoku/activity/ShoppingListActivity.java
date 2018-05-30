package ttc.project.stoku.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.callback.DoneInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.callback.ShoppingListInterface;
import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.adapter.ItemCartAdapter;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.entity.ListCategoryJoinEntity;
import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.room.entity.ReportItemEntity;
import ttc.project.stoku.room.entity.UserItemEntity;
import ttc.project.stoku.util.SnackbarUtil;

public class ShoppingListActivity extends BaseActivity implements DoneInterface, ShoppingListInterface, AdapterView.OnItemSelectedListener, Observer<List<CategoryEntity>> {

    public static final String EXTRA_SHOPPING_LIST = "shoppingList";


    @BindView(R.id.rv_shopping_list)
    RecyclerView rv_shopping_list;
    @BindView(R.id.rootView) View rootView;
    @BindView(R.id.btn_swipe)
    SwipeButton btn_swipe;
    @BindView(R.id.edt_item_name)
    EditText edt_item_name;
    @BindView(R.id.btn_add)
    ImageView btn_add;
    @BindView(R.id.edt_total) EditText edt_total;
    @BindView(R.id.btn_add_from_stock)
    Button btn_add_from_stock;
    @BindView(R.id.view_empty) View view_empty;
    @BindView(R.id.view_finish) View view_finish;
    @BindView(R.id.tv_currency) TextView tv_currency;

    ArrayList<ListItem> shoopingList = new ArrayList<>();
    ArrayList<ListItem> doneList = new ArrayList<>();

    Map<Long, ListItem> shoppingSet = new HashMap<>();
    Map<Long, ListItem> doneSet = new HashMap<>();

    private StokuViewModel mStokuViewModel;
    private ItemCartAdapter itemCartAdapter;
    private Snackbar snackbar;
    private String currency;
    private List<CategoryEntity> mCategoryEntities;
    private long categoryId;
    private NiceSpinner currentSpinner;
    private boolean initialPopulation = true;
    private View view_title;

    @Override
    protected void onResume() {
        super.onResume();
        fetchAllListItems();
        setCurrencyFromSharedPreference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);

        btn_swipe.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                if(getDoneSet().size() > 0){
                    shoppingFinished();
                } else{
                    SnackbarUtil.showSnackbar(ShoppingListActivity.this, rootView, getString(R.string.error_empty_cart),snackbar, Snackbar.LENGTH_SHORT);
                    btn_swipe.toggleState();
                }
            }
        });

        btn_add_from_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getParent() != null){
                    finish();
                } else{
                    Intent intent = new Intent(ShoppingListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);

        itemCartAdapter = new ItemCartAdapter(this, this);
        itemCartAdapter.swapData(shoopingList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv_shopping_list.setAdapter(itemCartAdapter);
        rv_shopping_list.setLayoutManager(linearLayoutManager);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewListItem();
            }
        });

        getSupportActionBar().setTitle(getString(R.string.shopping_list));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_clear_white);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void shoppingFinished(){
        if (!isAllItemsBought()){
            showAlertItemNotBought();
        } else{
            finishShopping();
        }
    }

    private Map<Long, ListItem> getDoneSet(){
        doneSet.clear();
        for (ListItem doneList : doneList){
            doneSet.put(doneList.getId(), doneList);
        }
        return doneSet;
    }

    private boolean isAllItemsBought(){
        shoppingSet.clear();
        doneSet.clear();

        for (ListItem shoppingList : shoopingList){
            shoppingSet.put(shoppingList.getId(), shoppingList);
        }

        for (ListItem doneList : doneList){
            doneSet.put(doneList.getId(), doneList);
        }

        return shoppingSet.size() == doneSet.size();
    }


    private void showAlertItemNotBought(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.finish_shopping_list_confirmation));
        builder.setMessage(getString(R.string.finish_undone_confirmation));
//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.continue_option), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                finishShopping();
//                categoryAdapter.notifyDataSetChanged();
//                SnackbarUtil.showSnackbar(ShoppingListActivity.this, rootView, getString(R.string.success_delete_category),snackbar, Snackbar.LENGTH_LONG);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                btn_swipe.toggleState();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                btn_swipe.toggleState();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setCurrencyFromSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currency = sharedPreferences.getString(getString(R.string.curr_key),getString(R.string.val_IDR));
        tv_currency.setText(currency);
    }

    private void finishShopping() {
        mStokuViewModel.deleteAllListData();
        double price = 0;
        if (!TextUtils.isEmpty(edt_total.getText().toString().trim())){
           price = Double.valueOf(edt_total.getText().toString());
        }
        ReportEntity reportEntity = new ReportEntity(0,
                Calendar.getInstance().getTimeInMillis(), price);
        new insertReportAsyncTask(mStokuViewModel).execute(reportEntity);

        Intent intent = new Intent(this, AfterShoppingActivity.class);
        startActivity(intent);
    }

    private void createNewReport(long id) {
        ReportItemEntity[] reportEntities = new ReportItemEntity[doneList.size()];
        reportEntities = getItemReportEntityFromDoneSet(id).toArray(reportEntities);

        mStokuViewModel.insertReportItem(reportEntities);
    }

    public List<ReportItemEntity> getItemReportEntityFromDoneSet(long reportId) {
        List<ReportItemEntity> reportItemEntities = new ArrayList<>();
        for(Map.Entry<Long, ListItem> e : doneSet.entrySet()) {
            ListItem listItem = e.getValue();
            ReportItemEntity reportItemEntity = new ReportItemEntity(
                    0,
                    reportId,
                    listItem.getCategory_id(),
                    listItem.getName(),
                    listItem.getNotes()
            );
            reportItemEntities.add(reportItemEntity);
        }
        return reportItemEntities;
    }

    private void populateSpinnerWithCategories(NiceSpinner spinner, long catId) {
        this.currentSpinner = spinner;
        this.categoryId = catId;
        mStokuViewModel.getAllCategoriesLiveData().removeObserver(this);
        mStokuViewModel.getAllCategoriesLiveData().observe(this, this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        changeColor(mCategoryEntities.get(position).getColorId());
        categoryId = mCategoryEntities.get(position).getId();
    }

    public void changeColor(int color) {
        view_title.setBackgroundColor(color);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onChanged(@Nullable List<CategoryEntity> categoryEntities) {
        ArrayList<String> categoryNames = new ArrayList<>();
        mCategoryEntities = categoryEntities;
        CategoryEntity currentCategory = null;
        int count = 0;
        int position = 0;
        for (CategoryEntity categoryEntity : categoryEntities){
            categoryNames.add(categoryEntity.getCategory_name());
            if(categoryEntity.getId() == categoryId){
                currentCategory = categoryEntity;
                position = count;
            }
            count++;
        }
//                ArrayAdapter<String> adapter =
//                        new ArrayAdapter<String>(AddEditItemActivity.this,  android.R.layout.simple_spinner_dropdown_item, categoryNames);
//                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        currentSpinner.attachDataSource(categoryNames);
        if (!initialPopulation){
            currentCategory = mCategoryEntities.get(categoryNames.size()-1);
            categoryId = currentCategory.getId();
            currentSpinner.setSelectedIndex(categoryNames.size()-1);
//            changeColor(currentCategory.getColorId());
        } else{
            currentSpinner.setSelectedIndex(position);
//            changeColor(currentCategory.getColorId());
        }
        initialPopulation = false;
    }

    private class insertReportAsyncTask extends AsyncTask<ReportEntity, Void, Long>{
        private StokuViewModel stokuViewModel;

        insertReportAsyncTask(StokuViewModel stoku){
            stokuViewModel = stoku;
        }

        @Override
        protected Long doInBackground(ReportEntity... reportEntity) {
            long id = stokuViewModel.insertReport(reportEntity[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            long id = aLong;
            createNewReport(id);
        }
    }

    private void insertNewListItem() {
        String itemName = edt_item_name.getText().toString().trim();
        if(TextUtils.isEmpty(itemName)){
            SnackbarUtil.showSnackbar(this, rootView, getString(R.string.error_empty_item), snackbar, Snackbar.LENGTH_LONG);
        } else{
            ListEntity listEntity = new ListEntity(0,1,itemName,"",false);
            mStokuViewModel.insertListItem(listEntity);
            fetchAllListItems();
            edt_item_name.setText("");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.action_share){
            shareData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareData() {
        StringBuilder sb = new StringBuilder();

        sb.append(getString(R.string.share_data_opening) +"\n");
        long currentCategoryId = 0;
        for (ListItem listItem : shoopingList){
            if(currentCategoryId != listItem.getCategory_id()){
                sb.append("\n"+listItem.getCategory_name()+"\n");
                currentCategoryId = listItem.getCategory_id();
            }
            String item = "- "+listItem.getName();
            if (TextUtils.isEmpty(listItem.getNotes())){
                item = item +"\n";
            } else{
                item = item +" ("+listItem.getNotes()+") "+"\n";
            }
            sb.append(item);
        }

        sb.append(getString(R.string.share_data_closing));

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }

    @Override
    public void done(ListItem listItem, boolean add) {
        if(add){
            doneList.add(listItem);
        } else{
            doneList.remove(listItem);
        }

    }

    @Override
    public void updateItem(ListItem listItem, int position) {
        ListEntity listEntity = new ListEntity(listItem.getId(), listItem.getCategory_id(), listItem.getName(), listItem.getNotes(), listItem.isDone());
        mStokuViewModel.updateListItem(listEntity);
        new getAllListItemJustDataAsyncTask().execute();
        if(listItem.isDone()){
//            SnackbarUtil.showSnackbar(this, rootView, getString(R.string.success_update_list_item), snackbar, Snackbar.LENGTH_LONG);
            doneList.add(listItem);
        } else{
            doneList.remove(listItem);
        }
    }

    @Override
    public void updateNotes(ListItem listItem, int position) {
        ListEntity listEntity = new ListEntity(listItem.getId(), listItem.getCategory_id(), listItem.getName(), listItem.getNotes(), listItem.isDone());
        mStokuViewModel.updateListItem(listEntity);
    }

    @Override
    public void deleteItem(ListItem listItem, int position) {
        ListEntity listEntity = new ListEntity(listItem.getId(), listItem.getCategory_id(), listItem.getName(), listItem.getNotes(), listItem.isDone());
        mStokuViewModel.deleteListItem(listEntity);
        itemCartAdapter.notifyItemChanged(position);

        shoopingList.remove(listItem);
//        for (ListItem item : shoopingList){
//            if (item.getId() == listItem.getId()){
//                shoopingList.remove(item);
//            }
//        }
        removeFromDoneList(listEntity);
//        shoopingList.remove(position);
        new getAllListItemJustDataAsyncTask().execute();

        SnackbarUtil.showSnackbar(this, rootView, getString(R.string.success_remove_list_item), snackbar, Snackbar.LENGTH_LONG);
    }

    @Override
    public void editItem(ListItem listItem, int position) {
        initialPopulation = true;
        showEditDialog(listItem, position);
    }

    private void removeFromDoneList(ListEntity listEntity) {
        for (ListItem listItem : doneList){
            if(listItem.getId() == listEntity.getId()){
                doneList.remove(listItem);
            }
        }
    }

    class getAllListItemAsyncTask extends AsyncTask<Void, Void, List<ListCategoryJoinEntity>> {
        @Override
        protected void onPostExecute(List<ListCategoryJoinEntity> listEntities) {
            super.onPostExecute(listEntities);
            shoopingList = getListItem(listEntities);
            itemCartAdapter.swapData(shoopingList);
            if(shoopingList.size() == 0){
                setEmpty(true);
            } else{
                setEmpty(false);
            }
        }

        @Override
        protected List<ListCategoryJoinEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllListItems();
        }
    }

    class getAllListItemJustDataAsyncTask extends AsyncTask<Void, Void, List<ListCategoryJoinEntity>> {
        @Override
        protected void onPostExecute(List<ListCategoryJoinEntity> listEntities) {
            super.onPostExecute(listEntities);
            shoopingList = getListItem(listEntities);
            if(shoopingList.size() == 0){
                setEmpty(true);
            } else{
                setEmpty(false);
            }
        }

        @Override
        protected List<ListCategoryJoinEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllListItems();
        }
    }

    private ArrayList<ListItem> getListItem(List<ListCategoryJoinEntity> listEntities) {
        ArrayList<ListItem> items = new ArrayList<>();
        for (ListCategoryJoinEntity listEntity : listEntities){
            ListItem item = new ListItem(
                    listEntity.getId(),
                    listEntity.getCategory_id(),
                    listEntity.getColorId(),
                    listEntity.getCategory_name(),
                    listEntity.getItem_name(),
                    listEntity.getNotes(),
                    listEntity.isDone()
            );
            items.add(item);
        }
        return items;
    }

    public void setEmpty(boolean b){
        if (b){
            view_empty.setVisibility(View.VISIBLE);
            view_finish.setVisibility(View.GONE);
        } else{
            view_empty.setVisibility(View.INVISIBLE);
            view_finish.setVisibility(View.VISIBLE);
        }
    }

    public void fetchAllListItems() {
        new getAllListItemAsyncTask().execute();
    }

    private void showEditDialog(final ListItem listItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_list_item_dialog));
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_item_cart,(ViewGroup) findViewById(android.R.id.content), false);

        View customTitleView = getLayoutInflater().inflate(R.layout.custom_title, null);
        TextView title = (TextView) customTitleView.findViewById(R.id.title);
        View view_title = customTitleView.findViewById(R.id.view_title);
        this.view_title = view_title;
//        title.setText("TITLE");

        builder.setCustomTitle(customTitleView);
        // Set up the input
        final EditText input_name = (EditText) viewInflated.findViewById(R.id.input_name);
        final EditText input_notes = (EditText) viewInflated.findViewById(R.id.input_notes);
        final NiceSpinner spinner_category = (NiceSpinner) viewInflated.findViewById(R.id.spinner_category);

        view_title.setBackgroundColor(listItem.getCategory_color());
        input_name.setText(listItem.getName());
        input_notes.setText(listItem.getNotes());

        populateSpinnerWithCategories(spinner_category, listItem.getCategory_id());
        spinner_category.setOnItemSelectedListener(this);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);


        builder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if(!TextUtils.isEmpty()){
//                    createNewCategory(newCategoryName);
//                }
                String item_name = input_name.getText().toString().trim();
                String notes = input_notes.getText().toString().trim();
                ListEntity listEntity = new ListEntity(
                        listItem.getId(),
                        categoryId,
                        item_name,
                        notes,
                        listItem.isDone()
                        );
                mStokuViewModel.updateListItem(listEntity);
                fetchAllListItems();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
