package ttc.project.stoku.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import ttc.project.stoku.room.entity.UserItemEntity;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(UserItemEntity userItemEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertItemWithReturn(UserItemEntity... userItemEntity);

    @Update
    void updateItem(UserItemEntity userItemEntity);

    @Update
    void updateItems(UserItemEntity... userItemEntity);

    @Delete
    void deleteItem(UserItemEntity userItemEntity);

    @Query("SELECT available from item_table WHERE id = :itemId")
    LiveData<Boolean> getStatus(long itemId);

//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY id ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItems();
//
//    @Query("SELECT * from UserItemCategoryJoinEntity ORDER BY name ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItemsByName();
}
