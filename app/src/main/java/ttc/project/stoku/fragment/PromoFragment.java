package ttc.project.stoku.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.adapter.MartAdapter;
import ttc.project.stoku.adapter.ReportAdapter;
import ttc.project.stoku.model.Mart;
import ttc.project.stoku.model.Report;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.ReportEntity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromoFragment extends Fragment {

    @BindView(R.id.rv_mart)
    RecyclerView rv_mart;
    @BindView(R.id.tv_test)
    TextView tv_test;

    MartAdapter martAdapter;

    public PromoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo, container, false);
        ButterKnife.bind(this, view);

        martAdapter = new MartAdapter(getActivity(), null);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_mart.setAdapter(martAdapter);
        rv_mart.setLayoutManager(gridLayoutManager);

        new getMartAsyncTask().execute();
        return view;
    }

    class getMartAsyncTask extends AsyncTask<Void, ArrayList<Mart>, ArrayList<Mart>>{

        @Override
        protected ArrayList<Mart> doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://www.hemat.id/").get();
                Elements elements = doc.select("div.logo-wrapper");
                ArrayList<String> names = new ArrayList<>();
                for (Element element : elements){
                    names.add(element.text());
                }

                ArrayList<String> images = new ArrayList<>();
                for (Element element : elements){
                    Element image = element.select("img").first();
                    images.add(image.attr("abs:src"));
                }

                ArrayList<String> endpoints = new ArrayList<>();
                for (Element element : elements){
                    Element a = element.select("a").first();
                    endpoints.add(a.attr("abs:href"));
                }

                ArrayList<Mart> marts = new ArrayList<>();
                for (int i =0;i<elements.size();i++){
                    Mart mart = new Mart(names.get(i), images.get(i), endpoints.get(i));
                    marts.add(mart);
                }

                return marts;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Mart> marts) {
            super.onPostExecute(marts);
            martAdapter.swapData(marts);
        }
    }

}
