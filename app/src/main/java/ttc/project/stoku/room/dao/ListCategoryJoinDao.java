package ttc.project.stoku.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import ttc.project.stoku.room.entity.ListCategoryJoinEntity;
import ttc.project.stoku.room.entity.UserItemCategoryJoinEntity;

@Dao
public interface ListCategoryJoinDao {
//    @Query("SELECT list_table.id," +
//            "list_table.item_name," +
//            "list_table.category_id, " +
//            "list_table.notes, " +
//            "list_table.done, " +
//            "category_table.category_name," +
//            "category_table.colorId from list_table " +
//            "JOIN category_table ON list_table.category_id = category_table.id ORDER BY category_id ASC")
//    LiveData<List<UserItemCategoryJoinEntity>> getAllListItemsLiveData();

    @Query("SELECT list_table.id," +
            "list_table.item_name," +
            "list_table.category_id, " +
            "list_table.notes, " +
            "list_table.done, " +
            "category_table.category_name," +
            "category_table.colorId from list_table " +
            "JOIN category_table ON list_table.category_id = category_table.id ORDER BY category_id ASC")
    List<ListCategoryJoinEntity> getAllListItems();
}
