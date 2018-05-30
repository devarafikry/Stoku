package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category_table")
public class CategoryEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    long id;

    String category_name;
    int colorId;

    public CategoryEntity(@NonNull long id, String category_name, int colorId) {
        this.id = id;
        this.category_name = category_name;
        this.colorId = colorId;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
