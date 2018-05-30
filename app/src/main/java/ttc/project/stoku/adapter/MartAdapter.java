package ttc.project.stoku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ttc.project.stoku.R;
import ttc.project.stoku.activity.PromoListActivity;
import ttc.project.stoku.model.Mart;
import ttc.project.stoku.viewholder.MartViewHolder;

public class MartAdapter extends RecyclerView.Adapter<MartViewHolder> {
    ArrayList<Mart> mMarts = new ArrayList<>();
    Context mContext;

    public MartAdapter(Context mContext, ArrayList<Mart> marts) {
        this.mContext = mContext;
        this.mMarts = marts;
    }

    public void swapData(ArrayList<Mart> marts){
        this.mMarts = marts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_mart, parent, false);
        return new MartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MartViewHolder holder, final int position) {
        final Mart mart = mMarts.get(position);
        holder.tv_mart_name.setText(mart.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PromoListActivity.class);
                intent.putExtra(PromoListActivity.EXTRA_MART_NAME, mart.getName());
                intent.putExtra(PromoListActivity.EXTRA_PROMO_ENDPOINT, mart.getEndpoint());
                intent.putExtra(PromoListActivity.EXTRA_MART_IMAGE_LINK, mart.getLink());
                mContext.startActivity(intent);
            }
        });

        Picasso.get().load(mart.getLink()).fit().into(holder.img_mart_image);
    }

    @Override
    public int getItemCount() {
        if(mMarts != null){
            return mMarts.size();
        } else{
            return 0;
        }
    }
}
