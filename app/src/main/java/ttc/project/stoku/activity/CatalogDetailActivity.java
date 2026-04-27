package ttc.project.stoku.activity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
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
                Document doc = Jsoup
                        .connect(endp)
                        .userAgent("Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36")
                        .timeout(15000)
                        .followRedirects(true)
                        .get();

                Element article = doc.selectFirst("article");
                if (article == null) {
                    return null;
                }

                // The site lazy-loads images with WP-Rocket: real URL is on
                // data-lazy-src, the src attribute holds an inline svg placeholder.
                // Walk the article's <img> tags and return the first one whose
                // resolved URL is an actual http(s) image.
                String link = null;
                for (Element img : article.select("img")) {
                    String url = img.absUrl("data-lazy-src");
                    if (TextUtils.isEmpty(url)) {
                        url = img.absUrl("data-src");
                    }
                    if (TextUtils.isEmpty(url)) {
                        url = img.absUrl("src");
                    }
                    if (!TextUtils.isEmpty(url) && !url.startsWith("data:")) {
                        link = url;
                        break;
                    }
                }
                if (link == null) {
                    return null;
                }

                return Picasso.get()
                        .load(link)
                        .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .get();
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
