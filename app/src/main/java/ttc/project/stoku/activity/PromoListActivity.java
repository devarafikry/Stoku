package ttc.project.stoku.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import ttc.project.stoku.adapter.PromoAdapter;
import ttc.project.stoku.model.PromoItem;

public class PromoListActivity extends BaseActivity {

    public static final String EXTRA_PROMO_ENDPOINT = "endPoint";
    public static final String EXTRA_MART_IMAGE_LINK = "martImageLink";
    public static final String EXTRA_MART_NAME = "martName";

    private String endPoint = "";
    private String martImageLink = "";
    private String martName = "";

    @BindView(R.id.rv_promo_items)
    RecyclerView rv_promo_items;
    @BindView(R.id.img_mart)
    ImageView img_mart;
    @BindView(R.id.tv_mart_name)
    TextView tv_mart_name;
    private PromoAdapter promoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_list);

        ButterKnife.bind(this);

        endPoint = getIntent().getStringExtra(EXTRA_PROMO_ENDPOINT);
        martImageLink = getIntent().getStringExtra(EXTRA_MART_IMAGE_LINK);
        martName = getIntent().getStringExtra(EXTRA_MART_NAME);

        Picasso.get().load(martImageLink).fit().into(img_mart);
        tv_mart_name.setText(martName);
        getSupportActionBar().setTitle(martName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        promoAdapter = new PromoAdapter(this, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv_promo_items.setAdapter(promoAdapter);
        rv_promo_items.setLayoutManager(linearLayoutManager);

        new getPromoItemAsyncTask().execute(endPoint);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class getPromoItemAsyncTask extends AsyncTask<String, ArrayList<PromoItem>, ArrayList<PromoItem>> {

        @Override
        protected ArrayList<PromoItem> doInBackground(String... endpoint) {
            try {
                String endp = endpoint[0];
                Document doc = Jsoup.connect(endp).get();

                Elements elements = doc.select("div.product-wrapper");
                ArrayList<String> titles = new ArrayList<>();
                for (Element element : elements){
                    Elements titleElements = element.select("div.title-prod");
                    titles.add(titleElements.text());
                }

                ArrayList<String> descriptions = new ArrayList<>();
                for (Element element : elements){
                    Elements descElements = element.select("div.desc-prod");
                    descriptions.add(descElements.text());
                }

                ArrayList<String> images = new ArrayList<>();
                for (Element element : elements){
                    Elements imageElements = element.select("div.img-prod");
                    Element image = element.select("img").first();
                    String url = image.absUrl("src");
                    if(url.equals(endp)){
                        url = image.absUrl("data-defer-src");
                        if (TextUtils.isEmpty(url)){
                            url = image.absUrl("data-src");
                        }
                    }
                    images.add(url);
                }

                ArrayList<String> expired = new ArrayList<>();
                for (Element element : elements){
                    Elements expiredElements = element.select("div.tgl-promo");
                    expired.add(expiredElements.text());
                }

                ArrayList<PromoItem> promoItems = new ArrayList<>();
                for (int i =0;i<elements.size();i++){
                    PromoItem mart = new PromoItem(
                            titles.get(i),
                            descriptions.get(i),
                            expired.get(i),
                            images.get(i));
                    promoItems.add(mart);
                }

                return promoItems;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PromoItem> promoItems) {
            super.onPostExecute(promoItems);
            promoAdapter.swapData(promoItems);
        }
    }
}
