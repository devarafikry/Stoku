package ttc.project.stoku.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.ReportEntity;
import ttc.project.stoku.room.entity.UserItemEntity;

@Dao
public interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReport(ReportEntity reportEntity);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertListItems(ListEntity... listEntity);

    @Query("SELECT * from report_table ORDER BY report_timemillis DESC")
    LiveData<List<ReportEntity>> getAllReports();

    @Delete
    void deleteReport(ReportEntity reportEntity);
//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY id ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItems();
//
//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY name ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItemsByName();
}
