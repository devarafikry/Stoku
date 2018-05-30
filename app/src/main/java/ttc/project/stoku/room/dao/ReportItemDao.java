package ttc.project.stoku.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import ttc.project.stoku.room.entity.ReportItemEntity;

@Dao
public interface ReportItemDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertReport(ReportEntity reportEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReportItems(ReportItemEntity... reportItemEntities);

//    @Query("SELECT * from list_table")
//    List<ListEntity> getListItems();

//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY id ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItems();
//
//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY name ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItemsByName();
}
