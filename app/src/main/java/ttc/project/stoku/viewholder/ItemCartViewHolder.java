package ttc.project.stoku.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import ttc.project.stoku.R;

public class ItemCartViewHolder extends RecyclerView.ViewHolder {

    public View view_category_type, view_item;
    public TextView tv_item_name;
    public CheckBox cb_item;
    public ImageView btn_remove, btn_edit;
    @Nullable public View view_category;
    @Nullable public TextView tv_category, tv_notes;

    public ItemCartViewHolder(View itemView) {
        super(itemView);
        view_category_type = itemView.findViewById(R.id.view_category_type);
        tv_item_name = itemView.findViewById(R.id.tv_item_name);
        view_category = itemView.findViewById(R.id.view_category);
        tv_category = itemView.findViewById(R.id.tv_category);
        view_item = itemView.findViewById(R.id.view_foreground);
        cb_item = itemView.findViewById(R.id.cb_item);
        btn_remove = itemView.findViewById(R.id.btn_remove);
        tv_notes = itemView.findViewById(R.id.tv_notes);
        btn_edit = itemView.findViewById(R.id.btn_edit);
    }
}
