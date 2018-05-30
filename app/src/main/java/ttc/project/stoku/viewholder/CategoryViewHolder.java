package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ttc.project.stoku.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_category_name;
    public View view_category_type;
    public ImageButton btn_edit, btn_delete;
    public CategoryViewHolder(View itemView) {
        super(itemView);
        tv_category_name = itemView.findViewById(R.id.tv_category_name);
        view_category_type = itemView.findViewById(R.id.view_category_type);
        btn_edit = itemView.findViewById(R.id.btn_edit);
        btn_delete = itemView.findViewById(R.id.btn_delete);
    }
}
