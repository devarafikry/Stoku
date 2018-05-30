package ttc.project.stoku.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ttc.project.stoku.R;
import ttc.project.stoku.model.PromoItem;
import ttc.project.stoku.viewholder.PromoViewHolder;

public class PromoAdapter extends RecyclerView.Adapter<PromoViewHolder> {
    ArrayList<PromoItem> mPromos = new ArrayList<>();
    Context mContext;

    public PromoAdapter(Context mContext, ArrayList<PromoItem> marts) {
        this.mContext = mContext;
        this.mPromos = marts;
    }

    public void swapData(ArrayList<PromoItem> marts){
        this.mPromos = marts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_promo, parent, false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, final int position) {
        final PromoItem promoItem = mPromos.get(position);
        holder.tv_promo_title.setText(promoItem.getTitle());
        holder.tv_promo_description.setText(promoItem.getDescription());
        holder.tv_expired.setText(promoItem.getExpired());

        Log.d("Item Viewed",""+position);
        Picasso.get().load(promoItem.getPromo_image_link()).fit().into(holder.img_promo_image);
    }

    @Override
    public int getItemCount() {
        if(mPromos != null){
            return mPromos.size();
        } else{
            return 0;
        }
    }
}
