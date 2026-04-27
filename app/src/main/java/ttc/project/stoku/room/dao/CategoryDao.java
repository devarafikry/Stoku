package ttc.project.stoku.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
