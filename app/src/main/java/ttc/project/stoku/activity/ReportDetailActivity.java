package ttc.project.stoku.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ttc.project.stoku.R;
import ttc.project.stoku.adapter.ItemReportAdapter;
import ttc.project.stoku.model.ReportItem;
import ttc.project.stoku.room.StokuViewModel;
import ttc.project.stoku.room.entity.ReportItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.ReportItemEntity;

public class ReportDetailActivity extends BaseActivity {

    @BindView(R.id.rv_items)
    RecyclerView rv_items;

    private StokuViewModel mStokuViewModel;
    ItemReportAdapter itemReportAdapter;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_REPORT_ID = "reportId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        ButterKnife.bind(this);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        long reportId = getIntent().getLongExtra(EXTRA_REPORT_ID, -1);
        if (reportId == -1){
            finish();
        }

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStokuViewModel = ViewModelProviders.of(this).get(StokuViewModel.class);
        itemReportAdapter = new ItemReportAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv_items.setAdapter(itemReportAdapter);
        rv_items.setLayoutManager(linearLayoutManager);

        mStokuViewModel.getAllReportItemData(reportId).observe(this, new Observer<List<ReportItemCategoryJoinEntity>>() {
            @Override
            public void onChanged(@Nullable List<ReportItemCategoryJoinEntity> reportItemCategoryJoinEntities) {
                itemReportAdapter.swapData(getReportItemFromReportItemEntities(reportItemCategoryJoinEntities));
            }
        });
    }

    private ArrayList<ReportItem> getReportItemFromReportItemEntities(List<ReportItemCategoryJoinEntity> reportEntities) {
        ArrayList<ReportItem> reports = new ArrayList<>();
        for (ReportItemCategoryJoinEntity reportEntity : reportEntities){
            ReportItem reportItem = new ReportItem(
                    reportEntity.getCategory_name(),
                    reportEntity.getId(),
                    reportEntity.getReport_id(),
                    reportEntity.getCategory_id(),
                    reportEntity.getItem_name(),
                    reportEntity.getNotes(),
                    reportEntity.getColorId()
            );
            reports.add(reportItem);
        }
        return reports;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
