package ttc.project.stoku.fragment;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.adapter.CatalogAdapter;
import ttc.project.stoku.callback.CatalogInterface;
import ttc.project.stoku.model.Catalog;
import ttc.project.stoku.model.Mart;

/**
 * A simple {@link Fragment} subclass.
 */
public class CatalogFragment extends Fragment implements CatalogInterface, LoaderManager.LoaderCallbacks<ArrayList<Catalog>>{

    @BindView(R.id.rv_catalogues)
    RecyclerView rv_catalogues;
    private CatalogAdapter catalogAdapter;
    @BindView(R.id.loadingView)
    LottieAnimationView loadingView;
    @BindView(R.id.btn_up)
    FloatingActionButton fab_up;
    @BindView(R.id.btn_load)
    Button btn_load;
    @BindView(R.id.view_empty) View view_empty;

    private ArrayList<Catalog> mCatalogs = new ArrayList<>();

    RecyclerView.OnScrollListener onScrollListener;
    int page = 1;
    private int LOADER_GET_CATALOG = 90;
    private String BUNDLE_PAGE = "page";


    public CatalogFragment() {
        // Required empty public constructor
    }

    public void setConnection(boolean b){
        if(b){
            view_empty.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.VISIBLE);
        } else{
            view_empty.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        ButterKnife.bind(this, view);
        catalogAdapter = new CatalogAdapter(getActivity(), null, this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        rv_catalogues.setAdapter(catalogAdapter);
        rv_catalogues.setLayoutManager(linearLayoutManager);

//        new getMartAsyncTask().execute(new Integer[]{page});

        checkInternet();
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == mCatalogs.size()){
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_PAGE, String.valueOf(page));
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_CATALOG, bundle, CatalogFragment.this);
                }
            }
        };

        rv_catalogues.addOnScrollListener(onScrollListener);
        rv_catalogues.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab_up.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                fab_up.hide();
            }
        });

        fab_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv_catalogues.smoothScrollToPosition(0);
            }
        });
        return view;
    }

    private void checkInternet() {
        setConnection(true);

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            setConnection(true);
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_PAGE, String.valueOf(page));
            getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_CATALOG, bundle, this);
        } else{
            setConnection(false);
        }
    }

    @Override
    public void scrollToTop() {
        rv_catalogues.smoothScrollToPosition(0);
    }


    @Override
    public Loader<ArrayList<Catalog>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Catalog>>(getActivity()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Catalog> loadInBackground() {
                try {
                    String pg = args.getString(BUNDLE_PAGE);
                    Document doc = Jsoup.connect("http://katalogpromosi.com/category/pasar-swalayan/page/"+pg).get();
                    Elements elements = doc.select("article.post");
                    ArrayList<String> titles = new ArrayList<>();
                    for (Element element : elements){
                        Element title = element.select("h3.loop-title").first();
                        titles.add(title.text());
                    }

                    ArrayList<String> images = new ArrayList<>();
                    for (Element element : elements){
                        Element thumb = element.select("div.loop-thumb").first();
                        Element image = thumb.select("img").first();
                        if(image == null){
                            return null;
                        }
                        String imgs_1 = image.attr("abs:srcset");
                        String[] imgs_2 = imgs_1.split(",");
                        String link = imgs_2[0].substring(0, imgs_2[0].length()-5);
                        images.add(link);
                    }

                    ArrayList<String> endpoints = new ArrayList<>();
                    for (Element element : elements){
                        Element a = element.select("a").first();
                        endpoints.add(a.attr("abs:href"));
                    }

                    ArrayList<String> dates = new ArrayList<>();
                    for (Element element : elements){
                        Element header = element.select("header.loop-data").first();
                        Element p = header.select("p.meta").first();
                        Element atext = p.select("a").first();

                        String date;
                        if(getActivity() != null){
                            date = String.format(
                                    getResources().getString(R.string.catalog_date),
                                    atext.text()
                            );
                        } else{
                            date = "null";
                        }
                        dates.add(date);
                    }

                    ArrayList<Catalog> catalogs = new ArrayList<>();
                    for (int i =0;i<elements.size();i++){
                        Catalog catalog = new Catalog(titles.get(i), images.get(i), endpoints.get(i), dates.get(i));
                        catalogs.add(catalog);
                    }
                    return catalogs;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Catalog>> loader, ArrayList<Catalog> data) {
        if(data != null){
            if(data.size() != 0){
                ArrayList<Catalog> al= new ArrayList<Catalog>();
                al.addAll(mCatalogs);
                al.addAll(data);

                mCatalogs = al;

                page++;
                catalogAdapter.swapData(al);
                loadingView.setVisibility(View.INVISIBLE);
            }
        } else{
            rv_catalogues.removeOnScrollListener(onScrollListener);
            catalogAdapter.setPageAvailable(false);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Catalog>> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PAGE, String.valueOf(page));
        getActivity().getSupportLoaderManager().restartLoader(LOADER_GET_CATALOG, bundle, this);
    }

    //    class getMartAsyncTask extends AsyncTask<Integer, ArrayList<Catalog>, ArrayList<Catalog>> {
//        @Override
//        protected ArrayList<Catalog> doInBackground(Integer... pg) {
//
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<Catalog> catalogs) {
//            super.onPostExecute(catalogs);
//
//        }
//    }

}
