package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ttc.project.stoku.R;

public class ColorViewHolder extends RecyclerView.ViewHolder{
    public View view_color;

    public ColorViewHolder(View itemView) {
        super(itemView);
        view_color = itemView.findViewById(R.id.view_color);
    }
}
