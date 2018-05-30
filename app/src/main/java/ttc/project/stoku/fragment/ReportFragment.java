package ttc.project.stoku.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.activity.ManageCategoryActivity;
import ttc.project.stoku.adapter.ReportAdapter;
import ttc.project.stoku.callback.ReportCallback;
import ttc.project.stoku.model.Report;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.util.SnackbarUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment implements ReportCallback {

    @BindView(R.id.rv_reports)
    RecyclerView rv_reports;
    @BindView(R.id.view_empty) View view_empty;
    @BindView(R.id.rootView) View rootView;

    Snackbar snackbar;

    ReportAdapter reportAdapter;

    StokuViewModel mStokuViewModel;
    private String currency;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrencyFromSharedPreference();
    }

    private void setCurrencyFromSharedPreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currency = sharedPreferences.getString(getString(R.string.curr_key),getString(R.string.val_IDR));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, view);

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        setCurrencyFromSharedPreference();

        reportAdapter = new ReportAdapter(getActivity(), null, currency, this);
        rv_reports.setLayoutManager(linearLayoutManager);
        rv_reports.setAdapter(reportAdapter);

        mStokuViewModel.getAllReports().observe(this, new Observer<List<ReportEntity>>() {
            @Override
            public void onChanged(@Nullable List<ReportEntity> reportEntities) {
                reportAdapter.swapData(getReportsFromReportEntities(reportEntities),currency);
                if (reportEntities.size() == 0){
                    setEmpty(true);
                } else{
                    setEmpty(false);
                }
            }
        });

        return view;
    }

    private ArrayList<Report> getReportsFromReportEntities(List<ReportEntity> reportEntities) {
        ArrayList<Report> reports = new ArrayList<>();
        for (ReportEntity reportEntity : reportEntities){
            Report report = new Report(reportEntity.getReport_timemillis(),reportEntity.getId(), reportEntity.getReport_total_price());
            reports.add(report);
        }
        return reports;
    }

    public void setEmpty(boolean b){
        if (b){
            view_empty.setVisibility(View.VISIBLE);
        } else{
            view_empty.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void deleteReport(final Report report) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.delete_report_title));

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy - h:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(report.getTimemillis());
        final String date = format.format(calendar.getTime());

        String deleteMessage = String.format(getString(R.string.delete_report_message), date);
        builder.setMessage(deleteMessage);

//        builder.setIcon();
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SnackbarUtil.showSnackbar(getActivity(), rootView, getString(R.string.success_delete_report),snackbar, Snackbar.LENGTH_LONG);
                mStokuViewModel.deleteReport(new ReportEntity(report.getId(), report.getTimemillis(), report.getPrice()));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
