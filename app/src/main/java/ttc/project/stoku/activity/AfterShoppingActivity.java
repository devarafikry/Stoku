package ttc.project.stoku.activity;

import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;

public class AfterShoppingActivity extends BaseActivity {
    @BindView(R.id.lottieAnimationView)
    LottieAnimationView animationView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.btn_close)
    ImageView btn_close;
    private boolean adLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_shopping);

        ButterKnife.bind(this);
        startCheckAnimation();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                btn_close.setVisibility(View.VISIBLE);
                adLoaded = true;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                btn_close.setVisibility(View.VISIBLE);
                adLoaded = true;
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(adLoaded){
            finish();
        }
    }

    private void startCheckAnimation(){
//        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(5000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                animationView.setProgress((Float) valueAnimator.getAnimatedValue());
//            }
//        });
//
//        if (animationView.getProgress() == 0f) {
//            animator.start();
//        } else {
//            animationView.setProgress(0f);
//        }
    }
}
