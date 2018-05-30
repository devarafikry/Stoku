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

import ttc.project.stoku.R;
import ttc.project.stoku.callback.ShoppingListInterface;
import ttc.project.stoku.model.ListItem;
import ttc.project.stoku.model.ReportItem;
import ttc.project.stoku.viewholder.ItemCartViewHolder;
import ttc.project.stoku.viewholder.ItemReportViewHolder;

public class ItemReportAdapter extends RecyclerView.Adapter<ItemReportViewHolder> {

    Context mContext;
    private List<ReportItem> mReportItems;
    private static final int VIEW_TYPE_ITEM = 87;
    private static final int VIEW_TYPE_ITEM_WITH_CATEGORY = 90;

    public void swapData(ArrayList<ReportItem> list){
        this.mReportItems = list;
        notifyDataSetChanged();
    }

//    public void removeItem(int position){
//
//    }

    public ItemReportAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(mReportItems.get(position).getId() == mReportItems.get(0).getId()){
            return VIEW_TYPE_ITEM_WITH_CATEGORY;
        } else{
            ReportItem currentItem = mReportItems.get(position);
            ReportItem prevItem = mReportItems.get(position-1);
            if (currentItem.getCategory_id() != prevItem.getCategory_id()){
                return VIEW_TYPE_ITEM_WITH_CATEGORY;
            } else{
                return VIEW_TYPE_ITEM;
            }
        }
    }

    @NonNull
    @Override
    public ItemReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if(viewType == VIEW_TYPE_ITEM_WITH_CATEGORY){
            view = inflater.inflate(R.layout.item_report_item_with_category, parent, false);
        } else{
            view = inflater.inflate(R.layout.item_report_item, parent, false);
        }
        return new ItemReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemReportViewHolder holder, final int position) {
        final ReportItem reportItem = mReportItems.get(position);

        if(position%2==0){
            holder.view_item.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
        } else{
            holder.view_item.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }

        if (!TextUtils.isEmpty(reportItem.getNotes())){
            holder.tv_notes.setText(reportItem.getNotes());
            holder.tv_notes.setVisibility(View.VISIBLE);
        } else{
            holder.tv_notes.setText("");
            holder.tv_notes.setVisibility(View.GONE);
        }

        if(holder.getItemViewType() == VIEW_TYPE_ITEM_WITH_CATEGORY){
            holder.view_category.setBackgroundColor(reportItem.getColorId());
            holder.tv_category.setText(reportItem.getCategory_name());
        }

        holder.view_category_type.setBackgroundColor(reportItem.getColorId());
        holder.tv_item_name.setText(reportItem.getItem_name());

    }

    @Override
    public int getItemCount() {
        if(mReportItems != null){
            return mReportItems.size();
        } else{
            return 0;
        }
    }
}
