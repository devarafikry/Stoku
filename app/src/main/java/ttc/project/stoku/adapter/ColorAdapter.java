package ttc.project.stoku.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ttc.project.stoku.callback.ColorPickInterface;
import ttc.project.stoku.R;
import ttc.project.stoku.viewholder.ColorViewHolder;

public class ColorAdapter extends RecyclerView.Adapter<ColorViewHolder> {
    ArrayList<Integer> colors = new ArrayList<>();
    Context mContext;
    ColorPickInterface mColorPickInterface;

    private void getColorData(){
        colors.add(ContextCompat.getColor(mContext, R.color.red));
        colors.add(ContextCompat.getColor(mContext, R.color.pink));
        colors.add(ContextCompat.getColor(mContext, R.color.purple));
        colors.add(ContextCompat.getColor(mContext, R.color.deep_purple));
        colors.add(ContextCompat.getColor(mContext, R.color.indigo));
        colors.add(ContextCompat.getColor(mContext, R.color.blue));
        colors.add(ContextCompat.getColor(mContext, R.color.light_blue));
        colors.add(ContextCompat.getColor(mContext, R.color.cyan));
        colors.add(ContextCompat.getColor(mContext, R.color.teal));
        colors.add(ContextCompat.getColor(mContext, R.color.green));
        colors.add(ContextCompat.getColor(mContext, R.color.light_green));
        colors.add(ContextCompat.getColor(mContext, R.color.lime));
        colors.add(ContextCompat.getColor(mContext, R.color.yellow));
        colors.add(ContextCompat.getColor(mContext, R.color.amber));
        colors.add(ContextCompat.getColor(mContext, R.color.orange));
        colors.add(ContextCompat.getColor(mContext, R.color.deep_orange));
        colors.add(ContextCompat.getColor(mContext, R.color.brown));
        colors.add(ContextCompat.getColor(mContext, R.color.grey));
        colors.add(ContextCompat.getColor(mContext, R.color.blue_grey));
    }

    public ColorAdapter(Context mContext, ColorPickInterface colorPickInterface) {
        this.mContext = mContext;
        this.mColorPickInterface = colorPickInterface;
        getColorData();
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, final int position) {
//        holder.view_color.setBackgroundColor(colors.get(position));
        holder.view_color.getBackground().setColorFilter(colors.get(position), PorterDuff.Mode.SRC_ATOP);
        holder.view_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColorPickInterface.colorPicked(colors.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }
}
