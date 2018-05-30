package ttc.project.stoku.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

import ttc.project.stoku.R;

public class ProgressBarViewHolder extends RecyclerView.ViewHolder {
    public LottieAnimationView progressBar;
    public ProgressBarViewHolder(View itemView) {
        super(itemView);

        this.progressBar = itemView.findViewById(R.id.loadingView);
    }
}
