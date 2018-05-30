package ttc.project.stoku.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ttc.project.stoku.callback.DoneInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.callback.ShoppingListInterface;
import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.model.UserItem;
import ttc.project.stoku.viewholder.ItemCartViewHolder;

public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartViewHolder> {

    Context mContext;
    private List<ListItem> mListItems;
    private long currentCategoryId = 0;
    private static final int VIEW_TYPE_ITEM = 87;
    private static final int VIEW_TYPE_ITEM_WITH_CATEGORY = 90;
    private int count = 0;
    private ShoppingListInterface mShoppingListInterface;
    private Map<Long, Boolean> checkBoxStates = new HashMap<>();
    public void swapData(ArrayList<ListItem> list){
        this.mListItems = list;
        notifyDataSetChanged();
    }

//    public void removeItem(int position){
//
//    }

    public ItemCartAdapter(Context context, ShoppingListInterface shoppingListInterface){
        this.mContext = context;
        this.mShoppingListInterface = shoppingListInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if(mListItems.get(position).getId() == mListItems.get(0).getId()){
            return VIEW_TYPE_ITEM_WITH_CATEGORY;
        } else{
            ListItem currentItem = mListItems.get(position);
            ListItem prevItem = mListItems.get(position-1);
            if (currentItem.getCategory_id() != prevItem.getCategory_id()){
                return VIEW_TYPE_ITEM_WITH_CATEGORY;
            } else{
                return VIEW_TYPE_ITEM;
            }
        }
    }

    @NonNull
    @Override
    public ItemCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if(viewType == VIEW_TYPE_ITEM_WITH_CATEGORY){
            view = inflater.inflate(R.layout.item_list_cart_with_category, parent, false);
        } else{
            view = inflater.inflate(R.layout.item_list_cart, parent, false);
        }
        return new ItemCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemCartViewHolder holder, final int position) {
        final ListItem listItem = mListItems.get(position);

        if(holder.getItemViewType() == VIEW_TYPE_ITEM_WITH_CATEGORY){
            count = 0;
        }

        if (!TextUtils.isEmpty(listItem.getNotes())){
            holder.tv_notes.setText(listItem.getNotes());
            holder.tv_notes.setVisibility(View.VISIBLE);
        } else{
            holder.tv_notes.setText("");
            holder.tv_notes.setVisibility(View.GONE);
        }

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShoppingListInterface.editItem(listItem, position);
            }
        });

        if(position%2==0){
            holder.view_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
        } else{
            holder.view_item.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        count++;
        if(holder.getItemViewType() == VIEW_TYPE_ITEM_WITH_CATEGORY){
            holder.view_category.setBackgroundColor(listItem.getCategory_color());
            holder.tv_category.setText(listItem.getCategory_name());
        }
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListItems.size() > 0){
                    mListItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mListItems.size());
                    mShoppingListInterface.deleteItem(listItem, position);
                }
            }
        });

        holder.view_category_type.setBackgroundColor(listItem.getCategory_color());
        holder.tv_item_name.setText(listItem.getName());
//        holder.cb_item.setChecked(listItem.isDone());
//        if(listItem.isDone()){
//            holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
//        } else{
//            holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//        }

        if(checkBoxStates.containsKey(listItem.getId())){
            holder.cb_item.setChecked(checkBoxStates.get(listItem.getId()));
            if(checkBoxStates.get(listItem.getId())){
                holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                holder.btn_edit.setEnabled(false);
                holder.btn_edit.setAlpha(0.2f);
                holder.btn_remove.setEnabled(false);
                holder.btn_remove.setAlpha(0.2f);
            } else{
                holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

                holder.btn_edit.setEnabled(true);
                holder.btn_edit.setAlpha(1f);
                holder.btn_remove.setEnabled(true);
                holder.btn_remove.setAlpha(1f);
            }
        } else{
            holder.cb_item.setChecked(false);
            holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            holder.btn_edit.setEnabled(true);
            holder.btn_edit.setAlpha(1f);
            holder.btn_remove.setEnabled(true);
            holder.btn_remove.setAlpha(1f);
        }
        holder.cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                currentCategoryId = 0;
                if(buttonView.isPressed()){
                    if(isChecked){
                        holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.btn_edit.setEnabled(false);
                        holder.btn_edit.setAlpha(0.2f);
                        holder.btn_remove.setEnabled(false);
                        holder.btn_remove.setAlpha(0.2f);
                    } else{
                        holder.tv_item_name.setPaintFlags(holder.tv_item_name.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        holder.btn_edit.setEnabled(true);
                        holder.btn_edit.setAlpha(1f);
                        holder.btn_remove.setEnabled(true);
                        holder.btn_remove.setAlpha(1f);
                    }
                    checkBoxStates.put(listItem.getId(), isChecked);
                    listItem.setDone(isChecked);
                    mListItems.set(position, listItem);
                    mShoppingListInterface.updateItem(listItem, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListItems != null){
            return mListItems.size();
        } else{
            return 0;
        }
    }

}
