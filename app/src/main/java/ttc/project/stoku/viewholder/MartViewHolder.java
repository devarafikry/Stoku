package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ttc.project.stoku.R;

public class MartViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_mart_name;
    public ImageView img_mart_image;

    public MartViewHolder(View itemView) {
        super(itemView);
        tv_mart_name = itemView.findViewById(R.id.tv_mart_name);
        img_mart_image = itemView.findViewById(R.id.img_mart_image);
    }
}
