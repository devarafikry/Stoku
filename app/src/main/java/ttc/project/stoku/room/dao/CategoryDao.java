package ttc.project.stoku.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ttc.project.stoku.room.entity.CategoryEntity;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(CategoryEntity categoryEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertCategoryWithReturn(CategoryEntity... categoryEntity);

    @Query("SELECT * from category_table ORDER BY id ASC")
    LiveData<List<CategoryEntity>> getAllCategoriesLiveData();

    @Query("SELECT * from category_table ORDER BY id ASC")
    List<CategoryEntity> getAllCategories();

    @Delete
    void deleteCategory(CategoryEntity categoryEntity);

    @Update
    void updateCategory(CategoryEntity categoryEntity);
}
