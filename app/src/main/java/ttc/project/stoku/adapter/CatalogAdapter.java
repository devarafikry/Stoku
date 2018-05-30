package ttc.project.stoku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ttc.project.stoku.R;
import ttc.project.stoku.activity.CatalogDetailActivity;
import ttc.project.stoku.callback.CatalogInterface;
import ttc.project.stoku.model.Catalog;
import ttc.project.stoku.model.PromoItem;
import ttc.project.stoku.viewholder.CatalogViewHolder;
import ttc.project.stoku.viewholder.ProgressBarViewHolder;
import ttc.project.stoku.viewholder.PromoViewHolder;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogViewHolder> {
    ArrayList<Catalog> mCatalogues = new ArrayList<>();
    Context mContext;

    public static final int VIEW_TYPE_ITEM = 12;
    public static final int VIEW_TYPE_LOADING = 65;
    private boolean pageAvailable = true;
    CatalogInterface mCatalogInterface;

    public CatalogAdapter(Context mContext, ArrayList<Catalog> catalogues, CatalogInterface catalogInterface) {
        this.mContext = mContext;
        this.mCatalogues = catalogues;
        this.mCatalogInterface = catalogInterface;
    }

    public void swapData(ArrayList<Catalog> catalogues){
        this.mCatalogues = catalogues;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
       if(position == mCatalogues.size()){
           return VIEW_TYPE_LOADING;
       } else{
           return VIEW_TYPE_ITEM;
       }
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if(viewType == VIEW_TYPE_ITEM){
            view = inflater.inflate(R.layout.item_catalog_option, parent, false);
        } else{
            view = inflater.inflate(R.layout.item_progress_bar, parent, false);
            return new CatalogViewHolder(view);

        }
        return new CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, final int position) {
        if(holder.getItemViewType() == VIEW_TYPE_ITEM){
            final Catalog catalog= mCatalogues.get(position);
            holder.tv_date.setText(catalog.getDate());
            holder.tv_catalog_title.setText(catalog.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(mContext, catalog.getEndpoint(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(mContext, CatalogDetailActivity.class);
                    intent.putExtra(CatalogDetailActivity.EXTRA_TITLE, catalog.getTitle());
                    intent.putExtra(CatalogDetailActivity.EXTRA_ENDPOINT, catalog.getEndpoint());

                    mContext.startActivity(intent);
                }
            });
            Picasso.get().load(catalog.getImage_link()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE.NO_STORE).fit().into(holder.img_catalog_thumb);
        } else{
            if (!pageAvailable){
                holder.progressBar.setVisibility(View.INVISIBLE);
//                holder.btn_back_to_top.setVisibility(View.VISIBLE);
//                holder.btn_back_to_top.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCatalogInterface.scrollToTop();
//                    }
//                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mCatalogues != null){
            return mCatalogues.size()+1;
        } else{
            return 0;
        }
    }

    public void setPageAvailable(boolean noPageAvailable) {
        this.pageAvailable = noPageAvailable;
        if(mCatalogues != null){
            notifyItemChanged(mCatalogues.size());
        }
    }
}
