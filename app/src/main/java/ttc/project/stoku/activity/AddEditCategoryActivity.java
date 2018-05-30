package ttc.project.stoku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.callback.ColorPickInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.adapter.ColorAdapter;
import ttc.project.stoku.util.SnackbarUtil;

public class AddEditCategoryActivity extends BaseActivity implements ColorPickInterface {

    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_COMMAND = "command";
    public static final String EXTRA_CATEGORY_POSITION = "itemPos";
    public static String EXTRA_CATEGORY_NAME = "categoryName";
    public static String EXTRA_COLOR_ID = "colorId";
    public static String EXTRA_CATEGORY_ID = "categoryId";

    boolean isAdd = true;

    @BindView(R.id.rv_colors)
    RecyclerView rv_colors;
    @BindView(R.id.edt_category_name)
    EditText edt_category_name;
    @BindView(R.id.btn_delete)
    TextView btn_delete;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_theme) View view_theme;
    @BindView(R.id.rootView) View rootView;

    String categoryName, command;
    int colorId;
    private Snackbar snackbar;
    long categoryId;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0f);

        colorId = ContextCompat.getColor(this, R.color.red);
        changeColor(colorId);

        if (getIntent().getStringExtra(EXTRA_TYPE).equals(getString(R.string.key_add))){
            isAdd = true;
            command = getString(R.string.key_add);
            getSupportActionBar().setTitle(getString(R.string.add_category));
            btn_delete.setVisibility(View.INVISIBLE);
        } else{
            command = getString(R.string.key_edit);
            isAdd = false;
            getSupportActionBar().setTitle(getString(R.string.edit_category));
            btn_delete.setVisibility(View.VISIBLE);
            categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
            colorId = getIntent().getIntExtra(EXTRA_COLOR_ID, ContextCompat.getColor(this, R.color.red));
            categoryId = getIntent().getLongExtra(EXTRA_CATEGORY_ID, 1);
            changeColor(colorId);
            edt_category_name.setText(categoryName);
            position = getIntent().getIntExtra(EXTRA_CATEGORY_POSITION, -1);
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteCategory();
                }
            });
        }

        ColorAdapter colorAdapter = new ColorAdapter(this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rv_colors.setLayoutManager(gridLayoutManager);
        rv_colors.setAdapter(colorAdapter);
    }

    private void deleteCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete_category_title));
        String deleteMessage = String.format(getString(R.string.delete_category_message), categoryName);
        builder.setMessage(deleteMessage);
//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                command = getString(R.string.key_delete);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
                resultIntent.putExtra(EXTRA_COLOR_ID, colorId);
                resultIntent.putExtra(EXTRA_CATEGORY_ID, categoryId);
                resultIntent.putExtra(EXTRA_COMMAND, command);
                resultIntent.putExtra(EXTRA_CATEGORY_POSITION, position);
                setResult(RESULT_OK, resultIntent);
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
            categoryName = edt_category_name.getText().toString().trim();
            if(TextUtils.isEmpty(categoryName)){
                SnackbarUtil.showSnackbar(this, rootView, getString(R.string.error_empty_category),snackbar, Snackbar.LENGTH_LONG);
            } else{
                if(isAdd){
                    addCategory();
                } else{
                    editCategory();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void editCategory() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        resultIntent.putExtra(EXTRA_COLOR_ID, colorId);
        resultIntent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        resultIntent.putExtra(EXTRA_COMMAND, command);
        resultIntent.putExtra(EXTRA_CATEGORY_POSITION, position);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void addCategory() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        resultIntent.putExtra(EXTRA_COLOR_ID, colorId);
        resultIntent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        resultIntent.putExtra(EXTRA_COMMAND, command);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void colorPicked(int color) {
        colorId = color;
        changeColor(color);
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
}
