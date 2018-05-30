package ttc.project.stoku.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ttc.project.stoku.room.entity.ListEntity;
import ttc.project.stoku.room.entity.UserItemEntity;

@Dao
public interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListItem(ListEntity listEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListItems(ListEntity... listEntity);

    @Update
    void updateListItem(ListEntity listEntity);

    @Update
    void updateListItems(ListEntity... listEntity);

    @Delete
    void deleteListItem(ListEntity listEntity);

    @Query("SELECT * from list_table")
    List<ListEntity> getListItems();

    @Query("DELETE FROM list_table")
    void deleteAllListData();

//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY id ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItems();
//
//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY name ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItemsByName();
}
