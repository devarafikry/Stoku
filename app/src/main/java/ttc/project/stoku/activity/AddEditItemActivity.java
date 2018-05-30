package ttc.project.stoku.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.room.entity.CategoryEntity;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.UserItemEntity;
import ttc.project.stoku.util.SnackbarUtil;

public class AddEditItemActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_ITEM_ID = "itemId";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_COMMAND = "command";
    public static final String EXTRA_ITEM_NAME = "itemName";
    public static final String EXTRA_CATEGORY_ID = "categoryId";
    public static final String EXTRA_NOTES = "notes";
    public static final String EXTRA_AVAILABLE = "available";
    public static final String EXTRA_ITEM_POSITION = "position";

    String itemName, notes, command;
    long categoryId = 1 ,itemId;

    @BindView(R.id.btn_delete)
    TextView btn_delete;
    @BindView(R.id.edt_item_name)
    EditText edt_item_name;
    @BindView(R.id.spinner_category)
    NiceSpinner spinner_category;
    @BindView(R.id.edt_notes) EditText edt_notes;
    @BindView(R.id.btn_add_category)
    ImageView btn_add_category;
    @BindView(R.id.view_theme) View view_theme;
    @BindView(R.id.rootView) View rootView;

    private Snackbar snackbar;
    private StokuViewModel stokuViewModel;
    boolean initialPopulation = true;
    boolean isAvailable = true;
    private List<CategoryEntity> mCategoryEntities = new ArrayList<>();

    boolean isAdd = true;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);
        populateSpinnerWithCategories();
        spinner_category.setOnItemSelectedListener(this);
        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });
        if(getIntent().getStringExtra(EXTRA_ITEM_NAME) != null){
            itemName = getIntent().getStringExtra(EXTRA_ITEM_NAME);
            edt_item_name.setText(itemName);
        }

        if(getIntent() != null && getIntent().getStringExtra(EXTRA_TYPE) != null){
            if(getIntent().getStringExtra(EXTRA_TYPE).equals(
                    getString(R.string.key_add)
            )){
                isAdd = true;
                getSupportActionBar().setTitle(getString(R.string.add_item));
                command = getString(R.string.key_add);
            } else{
                isAdd = false;
                getSupportActionBar().setTitle(getString(R.string.edit_item));
                itemName = getIntent().getStringExtra(EXTRA_ITEM_NAME);
                categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, 1);
                notes = getIntent().getStringExtra(EXTRA_NOTES);
                command = getString(R.string.key_edit);
                itemId = getIntent().getLongExtra(EXTRA_ITEM_ID,1);
                isAvailable = getIntent().getBooleanExtra(EXTRA_AVAILABLE, true);
                position = getIntent().getIntExtra(EXTRA_ITEM_POSITION, -1);
            }
        } else{
            isAdd = true;
            getSupportActionBar().setTitle(getString(R.string.add_item));
            command = getString(R.string.key_add_from_shortcut);
        }
        if (isAdd){
            btn_delete.setVisibility(View.INVISIBLE);
        } else{
            btn_delete.setVisibility(View.VISIBLE);
        }

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
        getSupportActionBar().setElevation(0f);
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_new_category));
        // I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.text_input_category,(ViewGroup) findViewById(android.R.id.content), false);
// Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        builder.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCategoryName = input.getText().toString().trim();
                if(!TextUtils.isEmpty(newCategoryName)){
                    createNewCategory(newCategoryName);
                }
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

    private void createNewCategory(String newCategoryName) {
        stokuViewModel.insertCategory(new CategoryEntity(0, newCategoryName, ContextCompat.getColor(this, R.color.red)));
    }

    private void populateSpinnerWithCategories() {
        stokuViewModel.getAllCategoriesLiveData().observe(this, new Observer<List<CategoryEntity>>() {
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
                spinner_category.attachDataSource(categoryNames);

                if (!initialPopulation){
                    currentCategory = mCategoryEntities.get(categoryNames.size()-1);
                    categoryId = currentCategory.getId();
                    spinner_category.setSelectedIndex(categoryNames.size()-1);
                    changeColor(currentCategory.getColorId());
                } else{
                    spinner_category.setSelectedIndex(position);
                    changeColor(currentCategory.getColorId());
                }
                initialPopulation = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.action_add){
            itemName = edt_item_name.getText().toString().trim();
            notes = edt_notes.getText().toString().trim();
            if (TextUtils.isEmpty(itemName)){
                SnackbarUtil.showSnackbar(this, rootView, getString(R.string.error_empty_item),snackbar, Snackbar.LENGTH_LONG);
            } else{
                if(isAdd){
                    addItem();
                } else{
                    editItem();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_item_title));
        String deleteMessage = String.format(getString(R.string.delete_item_message), itemName);
        builder.setMessage(deleteMessage);
//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                command = getString(R.string.key_delete);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ITEM_ID, itemId);
                intent.putExtra(EXTRA_ITEM_NAME, itemName);
                intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
                intent.putExtra(EXTRA_NOTES, notes);
                intent.putExtra(EXTRA_COMMAND, command);
                intent.putExtra(EXTRA_ITEM_POSITION, position);
                setResult(RESULT_OK, intent);
                finish();
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

    private void editItem() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ITEM_NAME, itemName);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(EXTRA_NOTES, notes);
        intent.putExtra(EXTRA_COMMAND, command);
        intent.putExtra(EXTRA_AVAILABLE, isAvailable);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addItem() {
        if(command.equals(getString(R.string.key_add_from_shortcut))){
            UserItemEntity userItemEntity = new UserItemEntity(0, categoryId, itemName, notes, true);
            stokuViewModel.insertItem(userItemEntity);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else{
            Intent intent = new Intent();
            intent.putExtra(EXTRA_ITEM_NAME, itemName);
            intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
            intent.putExtra(EXTRA_NOTES, notes);
            intent.putExtra(EXTRA_COMMAND, command);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void changeColor(int color) {
        view_theme.setBackgroundColor(color);
        btn_delete.setBackgroundColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        changeColor(mCategoryEntities.get(position).getColorId());
        categoryId = mCategoryEntities.get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
