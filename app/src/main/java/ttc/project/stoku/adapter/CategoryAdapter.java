package ttc.project.stoku.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import ttc.project.stoku.callback.CategoryInterface;
import ttc.project.stoku.model.Category;
import ttc.project.stoku.R;
import ttc.project.stoku.activity.AddEditCategoryActivity;
import ttc.project.stoku.viewholder.CategoryViewHolder;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    CategoryInterface mCategoryInterface;
    ArrayList<Category> mCategories = new ArrayList<>();
    Context mContext;

    public CategoryAdapter(Context mContext, CategoryInterface categoryInterface) {
        this.mContext = mContext;
        this.mCategoryInterface = categoryInterface;
    }

    public void swapData(ArrayList<Category> categories){
        this.mCategories = categories;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Category> categories){
        this.mCategories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_category_edit, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final Category category = mCategories.get(position);
        if(category.getId() == 1){
            disableButton(holder.btn_delete, holder.btn_edit, true);
        } else{
            disableButton(holder.btn_delete, holder.btn_edit, false);
        }
        holder.view_category_type.setBackgroundColor(category.getColorId());
        holder.tv_category_name.setText(category.getName());
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryInterface.editCategory(category, position);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryInterface.deleteCategory(category, position);
            }
        });
    }

    private void disableButton(ImageButton btn_delete, ImageButton btn_edit, boolean disable) {
        if(disable){
            btn_delete.setEnabled(false);
            btn_edit.setEnabled(false);
            if(Build.VERSION.SDK_INT >= 21){
                btn_delete.setBackgroundTintList(mContext.getResources().getColorStateList(android.R.color.darker_gray));
                btn_edit.setBackgroundTintList(mContext.getResources().getColorStateList(android.R.color.darker_gray));
            } else{
                btn_delete.setVisibility(View.INVISIBLE);
                btn_edit.setVisibility(View.INVISIBLE);
            }
        } else{
            btn_delete.setEnabled(true);
            btn_edit.setEnabled(true);
            if(Build.VERSION.SDK_INT >= 21){
                btn_delete.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorAccent));
                btn_edit.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorAccent));
            } else{
                btn_delete.setVisibility(View.VISIBLE);
                btn_edit.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
