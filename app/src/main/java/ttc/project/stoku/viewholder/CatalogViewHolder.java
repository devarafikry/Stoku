package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import javax.annotation.Nullable;

import ttc.project.stoku.R;

public class CatalogViewHolder extends RecyclerView.ViewHolder{

    public TextView tv_catalog_title, tv_date;
    public ImageView img_catalog_thumb;

    @Nullable public LottieAnimationView progressBar;
    @Nullable public Button btn_back_to_top;
    public CatalogViewHolder(View itemView) {
        super(itemView);
        tv_catalog_title = itemView.findViewById(R.id.tv_catalog_title);
        img_catalog_thumb = itemView.findViewById(R.id.img_catalog_thumbnail);
        tv_date = itemView.findViewById(R.id.tv_date);

        progressBar = itemView.findViewById(R.id.loadingView);
        btn_back_to_top = itemView.findViewById(R.id.btn_back_to_top);
    }
}
