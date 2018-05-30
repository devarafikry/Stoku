package ttc.project.stoku.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.model.PromoItem;

public class CatalogDetailActivity extends BaseActivity {

    public static final String EXTRA_ENDPOINT = "endPoint";
    public static final String EXTRA_TITLE = "title";

    String endpoint, title;

    @BindView(R.id.imageView)
    SubsamplingScaleImageView imageView;
    @BindView(R.id.loadingView)
    LottieAnimationView loadingView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.btn_close)
    ImageView btn_close;
    @BindView(R.id.tv_no_connection)
    View view_no_connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_detail);

        ButterKnife.bind(this);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        endpoint = getIntent().getStringExtra(EXTRA_ENDPOINT);
        title = getIntent().getStringExtra(EXTRA_TITLE);

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));
        }
        new getLargeImageAsyncTask().execute(endpoint);
    }

    class getLargeImageAsyncTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... endpoint) {
            try {
                String endp = endpoint[0];
                Document doc = Jsoup.connect(endp).get();

                Element article = doc.select("article").first();
                Element image = article.select("img").first();
                String imgs_1 = image.attr("abs:srcset");
                String[] imgs_2 = imgs_1.split(",");
                String link = imgs_2[0].substring(0, imgs_2[0].length()-5);

                Bitmap bitmap = Picasso.get().load(link).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).get();

                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            Picasso.get().load(imageLink).into(imageView);
            loadingView.setVisibility(View.INVISIBLE);
            if(bitmap != null){
                imageView.setImage(ImageSource.bitmap(bitmap));
            } else{
                view_no_connection.setVisibility(View.VISIBLE);
            }
        }
    }
}
