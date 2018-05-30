package ttc.project.stoku.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.angads25.toggle.LabeledSwitch;

import ttc.project.stoku.R;

public class ItemStockViewHolder extends RecyclerView.ViewHolder {

    @Nullable public View viewForeground, viewBackground;
    public View view_category_type;
    @Nullable public TextView tv_item_name, tv_notes;
    @Nullable public LabeledSwitch switch_available;
    @Nullable public CheckBox cb_item;
    @Nullable public ImageView btn_drag;
    @Nullable public View view_category;
    public TextView tv_category;

    public ItemStockViewHolder(View itemView) {
        super(itemView);
        view_category_type = itemView.findViewById(R.id.view_category_type);
        tv_item_name = itemView.findViewById(R.id.tv_item_name);
        switch_available = itemView.findViewById(R.id.switch_available);
        view_category = itemView.findViewById(R.id.view_category);
        tv_category = itemView.findViewById(R.id.tv_category);
        viewForeground = itemView.findViewById(R.id.view_foreground);
        viewBackground = itemView.findViewById(R.id.view_background);
        cb_item = itemView.findViewById(R.id.cb_item);
        btn_drag = itemView.findViewById(R.id.btn_drag);
        tv_notes = itemView.findViewById(R.id.tv_notes);
    }
}
