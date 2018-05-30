package ttc.project.stoku.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ttc.project.stoku.room.entity.ReportItemCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;

@Dao
public interface ReportItemCategoryJoinDao {
    @Query("SELECT report_item_table.id," +
            "report_item_table.item_name," +
            "report_item_table.report_id," +
            "report_item_table.category_id, " +
            "report_item_table.notes, " +
            "category_table.category_name," +
            "category_table.colorId from report_item_table " +
            "JOIN category_table ON report_item_table.category_id = category_table.id WHERE report_item_table.report_id=:reportId ORDER BY category_id ASC")
    LiveData<List<ReportItemCategoryJoinEntity>> getAllItemReportLiveData(long reportId);
}
