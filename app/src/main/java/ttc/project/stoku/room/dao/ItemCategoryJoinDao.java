package ttc.project.stoku.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;

@Dao
public interface ItemCategoryJoinDao {
    @Query("SELECT item_table.id," +
            "item_table.item_name," +
            "item_table.category_id, " +
            "item_table.notes, " +
            "item_table.available, " +
            "category_table.category_name," +
            "category_table.colorId from item_table " +
            "JOIN category_table ON item_table.category_id = category_table.id ORDER BY category_id ASC")
    LiveData<List<UserItemCategoryJoinEntity>> getAllUserItemsLiveData();

    @Query("SELECT item_table.id," +
            "item_table.item_name," +
            "item_table.category_id, " +
            "item_table.notes, " +
            "item_table.available, " +
            "category_table.category_name," +
            "category_table.colorId from item_table " +
            "JOIN category_table ON item_table.category_id = category_table.id ORDER BY category_id ASC")
    List<UserItemCategoryJoinEntity> getAllUserItems();
}
