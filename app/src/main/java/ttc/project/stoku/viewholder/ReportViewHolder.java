package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ttc.project.stoku.R;

public class ReportViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_price, tv_date, tv_date_time;
    public ImageView btn_delete;
    public ReportViewHolder(View itemView) {
        super(itemView);
        tv_price = itemView.findViewById(R.id.tv_price);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_date_time = itemView.findViewById(R.id.tv_date_time);
        btn_delete = itemView.findViewById(R.id.btn_delete);
    }
}
