package ttc.project.stoku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ttc.project.stoku.R;
import ttc.project.stoku.activity.ReportDetailActivity;
import ttc.project.stoku.callback.ReportCallback;
import ttc.project.stoku.model.Report;
import ttc.project.stoku.viewholder.ReportViewHolder;

public class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder> {

    private ArrayList<Report> mReports;
    private Context mContext;
    private String mCurrency;
    private ReportCallback mReportCallback;

    public ReportAdapter(Context context, ArrayList<Report> reports, String mCurrency, ReportCallback reportCallback){
        this.mContext = context;
        this.mReports = reports;
        this.mCurrency = mCurrency;
        this.mReportCallback = reportCallback;
    }

    public void swapData(ArrayList<Report> reports, String currency){
        this.mReports = reports;
        this.mCurrency = currency;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        final Report report = mReports.get(position);
        if(position%2==0){
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
        } else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        if(report.getPrice() == 0){
            holder.tv_price.setText(mContext.getString(R.string.undefined_cost));
        } else{
//            NumberFormat df = NumberFormat.getInstance();
//            df.setMaximumFractionDigits(0);
//
//            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
////                dfs.setCurrencySymbol("Rp ");
//            dfs.setGroupingSeparator('.');
//            dfs.setMonetaryDecimalSeparator('.');
//            dfs.setDecimalSeparator('.');
//            ((DecimalFormat) df).setDecimalFormatSymbols(dfs);

            holder.tv_price.setText(String.format(
                    mContext.getString(R.string.cost), mCurrency, String.valueOf(report.getPrice())
            ));
        }

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(report.getTimemillis());
        final String date = format.format(calendar.getTime());
        holder.tv_date.setText(date);

        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");
        final String time = formatTime.format(calendar.getTime());
        holder.tv_date_time.setText(time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReportDetailActivity.class);
                intent.putExtra(ReportDetailActivity.EXTRA_REPORT_ID, report.getId());
                intent.putExtra(ReportDetailActivity.EXTRA_TITLE,
                        String.format(mContext.getString(R.string.shopping), date));
                mContext.startActivity(intent);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReportCallback.deleteReport(report);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mReports == null) return 0;
        else return mReports.size();
    }
}
