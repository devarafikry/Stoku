package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "report_table")
public class ReportEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    long id;

    long report_timemillis;
    double report_total_price;

    public ReportEntity(@NonNull long id, long report_timemillis, double report_total_price) {
        this.id = id;
        this.report_timemillis = report_timemillis;
        this.report_total_price = report_total_price;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public long getReport_timemillis() {
        return report_timemillis;
    }

    public void setReport_timemillis(long report_timemillis) {
        this.report_timemillis = report_timemillis;
    }

    public double getReport_total_price() {
        return report_total_price;
    }

    public void setReport_total_price(double report_total_price) {
        this.report_total_price = report_total_price;
    }
}
