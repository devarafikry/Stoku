package ttc.project.stoku.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.DragItemTouchHelperCallback;
import ttc.project.stoku.RecyclerItemTouchHelper;
import ttc.project.stoku.activity.ManageCategoryActivity;
import ttc.project.stoku.activity.ShoppingListActivity;
import ttc.project.stoku.callback.AddToCartInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.callback.ItemStockInterface;
import ttc.project.stoku.callback.OnStartDragListener;
import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.activity.AddEditItemActivity;
import ttc.project.stoku.adapter.ItemStockAdapter;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.room.entity.ReportItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemEntity;
import ttc.project.stoku.util.SnackbarUtil;
import ttc.project.stoku.viewholder.ItemStockViewHolder;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment implements AddToCartInterface, ItemStockInterface, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, OnStartDragListener {

    private static final int EDIT_ITEM_REQUEST_CODE = 88;
    private static final int INSERT_ITEM_REQUEST_CODE = 99;

    @BindView(R.id.edt_item_name)
    EditText edt_item_name;
    @BindView(R.id.btn_edit)
    ImageView btn_edit;
    @BindView(R.id.btn_add) ImageView btn_add;
    @BindView(R.id.rv_items)
    RecyclerView rv_items;
    @BindView(R.id.btn_add_to_cart)
    TextView btn_add_to_cart;
    @BindView(R.id.rootView) View rootView;
    @BindView(R.id.btn_shop)
    FloatingActionButton btn_shop;
    @BindView(R.id.btn_add_now)
    Button btn_add_now;
    @BindView(R.id.view_empty) View view_empty;

    private Snackbar snackbar;
    private boolean isButtonShown = false;
    private StokuViewModel mStokuViewModel;

    ArrayList<UserItem> userItems = new ArrayList<>();
    ArrayList<UserItem> cartItems = new ArrayList<>();
    private ItemStockAdapter itemStockAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private boolean firstTime = true;
    private String SHOWCASE_ID = "showCase";

    public StockFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchAllItems();
//        fetchAllListItems();
    }

    @Override
    public void onResume() {
        super.onResume();
//        btn_add_to_cart.setVisibility(View.INVISIBLE);
        if (!firstTime){
            slideDown(btn_add_to_cart, 0);
        } else{
            firstTime = false;
        }
        itemStockAdapter.resetCheckboxState();
        itemStockAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        ButterKnife.bind(this, view);
        btn_add_to_cart.setVisibility(View.INVISIBLE);
//        generateDummyData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        firstTime = true;
        btn_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListActivity();
            }
        });

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);
        itemStockAdapter = new ItemStockAdapter(getActivity(), this, this, mStokuViewModel, this, this);
        itemStockAdapter.swapData(userItems);
        itemStockAdapter.notifyDataSetChanged();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);

        ItemTouchHelper.Callback callback =
                new DragItemTouchHelperCallback(itemStockAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv_items);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_items);
        rv_items.setLayoutManager(linearLayoutManager);
        rv_items.setAdapter(itemStockAdapter);
        fetchAllItems();
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditItemActivity.class);
                intent.putExtra(AddEditItemActivity.EXTRA_TYPE, getString(R.string.key_add));
                String currentItemName = edt_item_name.getText().toString().trim();
                intent.putExtra(AddEditItemActivity.EXTRA_ITEM_NAME, currentItemName);
                startActivityForResult(intent, INSERT_ITEM_REQUEST_CODE);
            }
        });

        btn_add_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditItemActivity.class);
                intent.putExtra(AddEditItemActivity.EXTRA_TYPE, getString(R.string.key_add));
                String currentItemName = edt_item_name.getText().toString().trim();
                intent.putExtra(AddEditItemActivity.EXTRA_ITEM_NAME, currentItemName);
                startActivityForResult(intent, INSERT_ITEM_REQUEST_CODE);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewItem();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListItem();
            }
        });

//        mStokuViewModel.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<ReportEntity> reportEntities) {
//                Toast.makeText(getActivity(), "count reports: "+reportEntities.size(),Toast.LENGTH_LONG).show();
//            }
//        });

//        mStokuViewModel.getAllReportItemData(3).observe(this, new Observer<List<ReportItemCategoryJoinEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<ReportItemCategoryJoinEntity> reportItemCategoryJoinEntities) {
//                Toast.makeText(getActivity(), "count item reports: "+reportItemCategoryJoinEntities.size(),Toast.LENGTH_LONG).show();
//            }
//        });
        return view;
    }

    private void showTutorial(ItemStockViewHolder holder) {
//        // single example
//        new MaterialShowcaseView.Builder(getActivity())
//                .setTarget(holder.cb_item)
//                .setDismissText("GOT IT")
//                .setContentText("This is some amazing feature you should know about")
//                .setDelay(100) // optional but starting animations immediately in onCreate can make them choppy
////                .singleUse(SHOWCASE_ID) // provide a unique ID used to ensure it is only shown once
//                .show();
//
//
//

        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view
        config.setMaskColor(addAlphaToColor(getResources().getColor(R.color.colorPrimary), 220));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), SHOWCASE_ID);

        sequence.setConfig(config);

        MaterialShowcaseView.Builder rectangleBuilder1 = new MaterialShowcaseView.Builder(getActivity())
                .setTarget(holder.itemView)
                .setContentText(getString(R.string.hint_itemview))
                .setDismissText(getString(R.string.got_it))
                .setMaskColour(addAlphaToColor(getResources().getColor(R.color.colorPrimary), 220))
                .withRectangleShape();

        sequence.addSequenceItem(rectangleBuilder1.build());

        sequence.addSequenceItem(holder.switch_available,
                getString(R.string.hint_switch), getString(R.string.got_it));

        sequence.addSequenceItem(holder.cb_item,
                getString(R.string.hint_checkbox), getString(R.string.got_it));

        sequence.addSequenceItem(holder.btn_drag,
                getString(R.string.hint_drag), getString(R.string.got_it));

        sequence.addSequenceItem(btn_shop,
                getString(R.string.hint_shopping_list), getString(R.string.got_it));
        sequence.start();
    }


    private int addAlphaToColor(int color, int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    private int addAlphaToColor(int colorId, int alpha, Context context) {
        return addAlphaToColor(ContextCompat.getColor(context, colorId), alpha);
    }

    private void createListItem() {
        sortCartItems();
        ListEntity[] listEntities = new ListEntity[cartItems.size()];
        listEntities = getListEntitiesFromCartList().toArray(listEntities);
        mStokuViewModel.insertListItems(listEntities);
        cartItems.clear();
        openListActivity();
    }

    private void openListActivity() {
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class);
        startActivity(intent);
    }

    private void insertNewItem(UserItemEntity item) {
        UserItemEntity itemEntity = new UserItemEntity(
                0,
                item.getCategory_id(),
                item.getItem_name(),
                item.getNotes(),
                true
        );
        mStokuViewModel.insertItem(itemEntity);
        edt_item_name.setText("");
        fetchAllItems();
    }

    private void insertNewItem() {
        String item_name = edt_item_name.getText().toString().trim();
        if(TextUtils.isEmpty(item_name)){
            SnackbarUtil.showSnackbar(getActivity(), rootView, getString(R.string.error_empty_item), snackbar, Snackbar.LENGTH_LONG);
        } else{
            UserItemEntity itemEntity = new UserItemEntity(
                    0,
                    1,
                    item_name,
                    "",
                    true
            );
            mStokuViewModel.insertItem(itemEntity);
            edt_item_name.setText("");
        }
        fetchAllItems();
    }

    private ArrayList<UserItem> getUserItemData(List<UserItemCategoryJoinEntity> userItemCategoryJoinEntities) {
        userItems.clear();
        for (UserItemCategoryJoinEntity item : userItemCategoryJoinEntities){
            UserItem userItem = new UserItem(
                item.getId(),
                item.getCategory_id(),
                item.getColorId(),
                item.getCategory_name(),
                item.getItem_name(),
                item.getNotes(),
                item.isAvailable()
            );
            userItems.add(userItem);
        }
        return userItems;
    }

    private void sortCartItems() {
        Collections.sort(cartItems);
    }


    @Override
    public void addToCart(UserItem userItem, boolean add) {
        userItem.setNotes("");
        if(add){
            cartItems.add(userItem);
        } else{
            cartItems.remove(userItem);
        }
        if(cartItems.size()>0){
//            btn_add_to_cart.setVisibility(View.VISIBLE);
            slideUp(btn_add_to_cart);
//            btn_shop.hide();
        } else {
//            btn_add_to_cart.setVisibility(View.INVISIBLE);
            slideDown(btn_add_to_cart, 250);
//            btn_shop.show();
        }
    }

    // slide the view from below itself to the current position
    public void slideUp(final View view){
        if(btn_shop.isShown()){
            btn_shop.hide();
        }
        if(!isButtonShown){
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(250);
            animate.setFillAfter(false);
            view.startAnimation(animate);
            view.setVisibility(View.VISIBLE);
        }
        isButtonShown = true;
    }

    // slide the view from its current position to below itself
    public void slideDown(final View view, long duration){
        if(!btn_shop.isShown()){
            btn_shop.show();
        }
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(duration);
        animate.setFillAfter(false);
//        animate.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                animation.cancel();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        view.startAnimation(animate);
        btn_add_to_cart.setVisibility(View.INVISIBLE);

        isButtonShown = false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(item.getItemId() == R.id.action_category){
            Intent intent = new Intent(getActivity(), ManageCategoryActivity.class);
            startActivity(intent);
        }
//        else if (item.getItemId() == R.id.action_sync){
//            Intent backupIntent = new Intent(Settings.ACTION_PRIVACY_SETTINGS);
//            startActivity(backupIntent);
//        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INSERT_ITEM_REQUEST_CODE && resultCode == RESULT_OK){
            String itemName = data.getStringExtra(AddEditItemActivity.EXTRA_ITEM_NAME);
            String notes = data.getStringExtra(AddEditItemActivity.EXTRA_NOTES);
            long categoryId = data.getLongExtra(AddEditItemActivity.EXTRA_CATEGORY_ID,1);
            insertNewItem(new UserItemEntity(0, categoryId, itemName, notes, true));
        } else if(requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK){
            String command = data.getStringExtra(AddEditItemActivity.EXTRA_COMMAND);
            if(command.equals(getString(R.string.key_edit))){
                long itemId = data.getLongExtra(AddEditItemActivity.EXTRA_ITEM_ID,0);
                String itemName = data.getStringExtra(AddEditItemActivity.EXTRA_ITEM_NAME);
                String notes = data.getStringExtra(AddEditItemActivity.EXTRA_NOTES);
                long categoryId = data.getLongExtra(AddEditItemActivity.EXTRA_CATEGORY_ID,1);
                boolean available = data.getBooleanExtra(AddEditItemActivity.EXTRA_AVAILABLE, true);

                mStokuViewModel.updateItem(new UserItemEntity(itemId, categoryId, itemName, notes, available));
                SnackbarUtil.showSnackbar(getActivity(), rootView, getString(R.string.success_update_item),snackbar, Snackbar.LENGTH_LONG);
                fetchAllItems();
            } else if(command.equals(getString(R.string.key_delete))){
                int position = data.getIntExtra(AddEditItemActivity.EXTRA_ITEM_POSITION, -1);
                long itemId = data.getLongExtra(AddEditItemActivity.EXTRA_ITEM_ID,0);
                String itemName = data.getStringExtra(AddEditItemActivity.EXTRA_ITEM_NAME);
                String notes = data.getStringExtra(AddEditItemActivity.EXTRA_NOTES);
                long categoryId = data.getLongExtra(AddEditItemActivity.EXTRA_CATEGORY_ID,1);
                boolean available = data.getBooleanExtra(AddEditItemActivity.EXTRA_AVAILABLE, true);

                mStokuViewModel.deleteItem(new UserItemEntity(itemId, categoryId, itemName, notes, available));
                SnackbarUtil.showSnackbar(getActivity(), rootView, getString(R.string.success_delete_item),snackbar, Snackbar.LENGTH_LONG);

                userItems.remove(position);
                if (userItems.size() == 0){
                    setEmpty(true);
                } else{
                    setEmpty(false);
                }

                if(cartItems.size() > 0){
//                    for (UserItem userItem : cartItems){
//                        if (userItem.getId() == itemId){
//                            cartItems.remove(userItem);
//                            if (cartItems.size() == 0){
//                                slideDown(btn_add_to_cart, 250);
//                            }
//                        }
//                    }
                }
                itemStockAdapter.notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void updateItem(UserItem userItem, int position) {
        mStokuViewModel.updateItem(new UserItemEntity(userItem.getId(), userItem.getCategory_id(), userItem.getName(), userItem.getNotes(), userItem.isAvailable()));
//        userItems.get(position).setDone(userItem.isDone());
        if (position == 1){
            rv_items.smoothScrollToPosition(0);
        }
        itemStockAdapter.statusUpdated(userItem, position);
    }

    @Override
    public void editItem(UserItem userItem, int position) {
        Intent intent = new Intent(getActivity(), AddEditItemActivity.class);
        intent.putExtra(AddEditItemActivity.EXTRA_TYPE, getString(R.string.key_edit));
        intent.putExtra(AddEditItemActivity.EXTRA_COMMAND, getString(R.string.key_edit));
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_NAME, userItem.getName());
        intent.putExtra(AddEditItemActivity.EXTRA_CATEGORY_ID, userItem.getCategory_id());
        intent.putExtra(AddEditItemActivity.EXTRA_NOTES, userItem.getNotes());
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_ID, userItem.getId());
        intent.putExtra(AddEditItemActivity.EXTRA_AVAILABLE, userItem.isAvailable());
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_POSITION, position);
        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
    }

    @Override
    public void deleteItem(final UserItem userItem, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_item_title));
        String deleteMessage = String.format(getString(R.string.delete_item_message), userItem.getName());
        builder.setMessage(deleteMessage);
//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mStokuViewModel.deleteItem(new UserItemEntity(
                        userItem.getId(),
                        userItem.getCategory_id(),
                        userItem.getName(),
                        userItem.getNotes(),
                        userItem.isAvailable()));
                SnackbarUtil.showSnackbar(getActivity(), rootView, getString(R.string.success_delete_item),snackbar, Snackbar.LENGTH_LONG);
                userItems.remove(userItem);
                itemStockAdapter.setData(userItems, userItem);
                if(userItems.size() > 0){
                    setEmpty(false);
                    checkCart(userItem);
                    itemStockAdapter.notifyItemRemoved(position);
//                    itemStockAdapter.notifyItemRangeRemoved(position, 1);
                    itemStockAdapter.notifyItemRangeChanged(position, userItems.size());
                } else{
                    itemStockAdapter.notifyDataSetChanged();
                    setEmpty(true);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                itemStockAdapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                itemStockAdapter.notifyItemChanged(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void firstItemReady(ItemStockViewHolder holder) {
        showTutorial(holder);
    }

    private void checkCart(UserItem userItem) {
        if(cartItems.size() > 0){
            cartItems.remove(userItem);
        }

        if (cartItems.size() == 0){
            slideDown(btn_add_to_cart, 250);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        ArrayList<Object> rv_items = itemStockAdapter.getRv_items();
        deleteItem((UserItem) rv_items.get(position), position);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public ArrayList<ListEntity> getListEntitiesFromCartList() {
        ArrayList<ListEntity> entities = new ArrayList<>();
        for (UserItem item : cartItems){
            ListEntity listEntity = new ListEntity(0, item.getCategory_id(), item.getName(), item.getNotes(), item.isAvailable());
            entities.add(listEntity);
        }
        return entities;
    }

    public void setEmpty(boolean b){
        if (b){
            view_empty.setVisibility(View.VISIBLE);
        } else{
            view_empty.setVisibility(View.INVISIBLE);
        }
    }

    public void fetchAllItems() {
        new getAllItemAsyncTask().execute();
    }
    class getAllItemAsyncTask extends AsyncTask<Void, Void, List<UserItemCategoryJoinEntity>>{
        @Override
        protected void onPostExecute(List<UserItemCategoryJoinEntity> userItemCategoryJoinEntities) {
            super.onPostExecute(userItemCategoryJoinEntities);
            userItems = getUserItemData(userItemCategoryJoinEntities);
            itemStockAdapter.swapData(userItems);


            if(userItems.size() == 0){
                setEmpty(true);
            } else{
                setEmpty(false);
            }
        }

        @Override
        protected List<UserItemCategoryJoinEntity> doInBackground(Void... voids) {
            return mStokuViewModel.getAllItems();
        }
    }

}
