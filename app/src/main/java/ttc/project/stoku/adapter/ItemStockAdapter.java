package ttc.project.stoku.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ttc.project.stoku.callback.AddToCartInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.callback.ItemStockInterface;
import ttc.project.stoku.callback.ItemTouchHelperAdapter;
import ttc.project.stoku.callback.OnStartDragListener;
import ttc.project.stoku.model.Category;
import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.viewholder.ItemStockViewHolder;

public class ItemStockAdapter extends RecyclerView.Adapter<ItemStockViewHolder> implements ItemTouchHelperAdapter{

    private final Fragment mOwner;
    Context mContext;
    private ArrayList<UserItem> mUserItems;
    private Set<Long> mCategoriesId = new HashSet<>();
    private long currentCategoryId = 0;
    public static final int VIEW_TYPE_ITEM = 87;
    public static final int VIEW_CATEGORY = 90;
    private int count = 0;
    private AddToCartInterface mAddToCartInterface;
    private ItemStockInterface mItemStockInterface;
    private StokuViewModel mStokuViewModel;
    private OnStartDragListener mDragStartListener;
    private Map<Long, Boolean> checkBoxStates = new HashMap<>();
    private ArrayList<Object> rv_items = new ArrayList<>();

    public void identifyCategories(ArrayList<UserItem> userItems){
        mCategoriesId.clear();

        for (UserItem userItem : userItems){
            if(!mCategoriesId.contains(userItem.getCategory_id())){
                mCategoriesId.add(userItem.getCategory_id());
            }
        }
    }

    public ArrayList<Object> getRv_items() {
        return rv_items;
    }

    public void setRv_items(ArrayList<Object> rv_items) {
        this.rv_items = rv_items;
    }

    public void initiateRvData(){
        rv_items.clear();

        identifyCategories(mUserItems);
        int totalSize = mUserItems.size()+mCategoriesId.size();
        int categoryCounting = 0;
        if(mUserItems.size() > 0){
            for(int pos = 0;pos<totalSize;pos++){
                UserItem userItem = mUserItems.get(pos-categoryCounting);
                if(rv_items.size() == 0){
                    Category category = new Category(userItem.getCategory_id(), userItem.getCategory_name(), userItem.getCategory_color());
                    rv_items.add(category);
                    categoryCounting++;
                } else{
                    if (rv_items.get(pos-1) instanceof Category){
                        Category category = (Category) rv_items.get(pos-1);
                        Category currentCategory = new Category(userItem.getCategory_id(), userItem.getCategory_name(), userItem.getCategory_color());
                        if(userItem.getCategory_id() == category.getId()){
                            rv_items.add(userItem);
                        } else{
                            rv_items.add(currentCategory);
                            categoryCounting++;
                        }
                    } else{
                        UserItem prevUserItem = (UserItem) rv_items.get(pos-1);
                        Category category = new Category(prevUserItem.getCategory_id(), prevUserItem.getCategory_name(), prevUserItem.getCategory_color());
                        Category currentCategory = new Category(userItem.getCategory_id(), userItem.getCategory_name(), userItem.getCategory_color());
                        if(userItem.getCategory_id() == category.getId()){
                            rv_items.add(userItem);
                        } else{
                            rv_items.add(currentCategory);
                            categoryCounting++;
                        }
                    }
                }
            }
        }
    }

    public void swapData(ArrayList<UserItem> userItems){
        currentCategoryId = 0;
        this.mUserItems = userItems;
        initiateRvData();
        notifyDataSetChanged();
    }

    public void statusUpdated(UserItem userItem, int position){
        currentCategoryId = 0;
//        rv_items.set(position, userItem);
        int updateId = rv_items.indexOf(userItem);
        rv_items.set(updateId, userItem);
//        initiateRvData();
        notifyItemChanged(position);
    }

    public ItemStockAdapter(Context context, AddToCartInterface addToCartInterface, ItemStockInterface itemStockInterface, StokuViewModel stokuViewModel, Fragment owner, OnStartDragListener onStartDragListener){
        this.mContext = context;
        this.mAddToCartInterface = addToCartInterface;
        this.mItemStockInterface = itemStockInterface;
        this.mStokuViewModel = stokuViewModel;
        this.mOwner = owner;
        this.mDragStartListener = onStartDragListener;
    }

    @Override
    public int getItemViewType(int position) {
//        if(position > 0) {
//
//        }
        if (rv_items.get(position) instanceof Category) {
            return VIEW_CATEGORY;
        } else {
            return VIEW_TYPE_ITEM;
        }
//            UserItem prevCurrentUserItem = mUserItems.get(position-1);
//            UserItem currentUserItem = mUserItems.get(position);
//
//            if(prevCurrentUserItem.getCategory_id() != currentUserItem.getCategory_id()){
//                return VIEW_CATEGORY;
//            } else{
//                return VIEW_TYPE_ITEM;
//            }
//        } else{
//            return VIEW_CATEGORY;
//        }
//        if(position == 0){
//            currentCategoryId =0;
//        }
//        long newCategoryId = mUserItems.get(position).getCategory_id();
//        if(currentCategoryId == 0 ){
//            currentCategoryId = newCategoryId;
//            return VIEW_CATEGORY;
//        }
//        if(currentCategoryId != 0 && currentCategoryId != newCategoryId){
//            currentCategoryId = newCategoryId;
//            return VIEW_CATEGORY;
//        }
//        else{
//            return VIEW_TYPE_ITEM;
//        }
    }

    @NonNull
    @Override
    public ItemStockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if(viewType == VIEW_CATEGORY){
            view = inflater.inflate(R.layout.item_category, parent, false);
        } else{
            view = inflater.inflate(R.layout.item_list, parent, false);
        }
        return new ItemStockViewHolder(view);
    }

    public void resetCheckboxState(){
        checkBoxStates.clear();
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemStockViewHolder holder, final int position) {
        if(holder.getItemViewType() == VIEW_TYPE_ITEM){
            final UserItem userItem = (UserItem) rv_items.get(position);
            if (position == 3){
                mItemStockInterface.firstItemReady(holder);
            }
            holder.btn_drag.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
            if (!TextUtils.isEmpty(userItem.getNotes())){
                holder.tv_notes.setText(userItem.getNotes());
                holder.tv_notes.setVisibility(View.VISIBLE);
            } else{
                holder.tv_notes.setText("");
                holder.tv_notes.setVisibility(View.GONE);
            }
            if(holder.getItemViewType() == VIEW_CATEGORY){
                count = 0;
                holder.itemView.setEnabled(false);
            } else{

            }

            if(position%2==0){
                holder.viewForeground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
            } else{
                holder.viewForeground.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
            }
            count++;
            if(holder.getItemViewType() == VIEW_CATEGORY){
                holder.view_category.setBackgroundColor(userItem.getCategory_color());
                holder.tv_category.setText(userItem.getCategory_name());
            }

            holder.switch_available.setLabelOn(mContext.getString(R.string.available));
            holder.switch_available.setLabelOff(mContext.getString(R.string.empty));
            holder.switch_available.setOnToggledListener(null);
            holder.switch_available.setOn(userItem.isAvailable());

            holder.switch_available.setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
                    userItem.setAvailable(isOn);
                    mItemStockInterface.updateItem(userItem, position);
                }
            });
            holder.view_category_type.setBackgroundColor(userItem.getCategory_color());
            holder.tv_item_name.setText(userItem.getName());

//            if (checkBoxStates.containsKey(userItem.getId())){
//                holder.cb_item.setChecked(checkBoxStates.get(userItem.getId()));
//            } else{
//                holder.cb_item.setChecked(false);
//            }
            if(checkBoxStates.containsKey(userItem.getId())){
                holder.cb_item.setChecked(checkBoxStates.get(userItem.getId()));
            } else{
                holder.cb_item.setChecked(false);
            }
            holder.cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(buttonView.isPressed()){
                        checkBoxStates.put(userItem.getId(), isChecked);
                        mAddToCartInterface.addToCart(userItem, isChecked);
                    }
                }
            });
            holder.tv_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemStockInterface.editItem(userItem, position);
                }
            });
        } else{
            final Category category= (Category) rv_items.get(position);
            holder.view_category_type.setBackgroundColor(category.getColorId());
            holder.tv_category.setText(category.getName());
        }
    }

    @Override
    public void onItemDismiss(int position) {
        mUserItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Log.v("", "Log position" + fromPosition + " " + toPosition);
        if (fromPosition < rv_items.size() && toPosition < rv_items.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(rv_items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(rv_items, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        return true;
    }

    @Override
    public boolean onItemMoveCompleted(int endPosition) {
        if(endPosition != -1){
            if(endPosition>0){
                if(rv_items.get(endPosition-1) instanceof UserItem){
                    UserItem itemNewPositionPrev = (UserItem) rv_items.get(endPosition-1);
                    UserItem currentItem = (UserItem) rv_items.get(endPosition);

                    currentItem.setCategory_id(itemNewPositionPrev.getCategory_id());
                    currentItem.setCategory_name(itemNewPositionPrev.getCategory_name());
                    currentItem.setCategory_color(itemNewPositionPrev.getCategory_color());
                    mItemStockInterface.updateItem(currentItem, endPosition);
                } else{
                    Category category = (Category) rv_items.get(endPosition-1);

                    UserItem currentItem = (UserItem) rv_items.get(endPosition);

                    currentItem.setCategory_id(category.getId());
                    currentItem.setCategory_name(category.getName());
                    currentItem.setCategory_color(category.getColorId());
                    mItemStockInterface.updateItem(currentItem, endPosition);
                }
            } else{
                Collections.swap(rv_items, 0, 1);
                notifyItemMoved(0,1);
                Category category = (Category) rv_items.get(0);
                UserItem userItem = (UserItem) rv_items.get(1);
                userItem.setCategory_id(category.getId());
                userItem.setCategory_name(category.getName());
                userItem.setCategory_color(category.getColorId());

                mItemStockInterface.updateItem(userItem, 1);
            }
//            else{
//                if(rv_items.get(endPosition) instanceof UserItem){
//                    UserItem itemNewPositionNext = (UserItem) rv_items.get(endPosition);
//                    UserItem currentItem = mUserItems.get(endPosition);
//
//                    currentItem.setCategory_id(itemNewPositionNext.getCategory_id());
//                    currentItem.setCategory_name(itemNewPositionNext.getCategory_name());
//                    currentItem.setCategory_color(itemNewPositionNext.getCategory_color());
//                    mItemStockInterface.updateItem(currentItem, endPosition);
//                } else{
//                    Category category = (Category) rv_items.get(endPosition);
//
//                    UserItem currentItem = mUserItems.get(endPosition);
//
//                    currentItem.setCategory_id(category.getId());
//                    currentItem.setCategory_name(category.getName());
//                    currentItem.setCategory_color(category.getColorId());
//                    mItemStockInterface.updateItem(currentItem, endPosition);
//                }
//            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        if(rv_items.size()>0){ return rv_items.size();}
        else return 0;
    }

    public void setData(ArrayList<UserItem> data, UserItem deletedUserItem) {
        int idDeletedUserItem = rv_items.indexOf(deletedUserItem);
        if (rv_items.get(idDeletedUserItem-1) instanceof Category){
            rv_items.remove(idDeletedUserItem-1);
            notifyItemRemoved(idDeletedUserItem-1);
            notifyItemRangeRemoved(idDeletedUserItem-1, 1);
            notifyItemRangeChanged(idDeletedUserItem-1, rv_items.size());
        }

        this.mUserItems = data;
        initiateRvData();
    }
}
