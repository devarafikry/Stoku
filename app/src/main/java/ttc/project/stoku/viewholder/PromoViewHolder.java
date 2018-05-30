package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ttc.project.stoku.R;

public class PromoViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_promo_title, tv_promo_description, tv_expired;
    public ImageView img_promo_image;

    public PromoViewHolder(View itemView) {
        super(itemView);
        tv_promo_title = itemView.findViewById(R.id.tv_title);
        tv_promo_description = itemView.findViewById(R.id.tv_description);
        tv_expired = itemView.findViewById(R.id.tv_expired);

        img_promo_image = itemView.findViewById(R.id.img_promo_image);

    }
}
