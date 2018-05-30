package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "list_table", foreignKeys = @ForeignKey(entity = CategoryEntity.class,
        parentColumns = "id",
        childColumns = "category_id",
        onDelete = CASCADE),indices = {@Index("category_id")} )
public class ListEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    long id;

    long category_id = 1;
    String item_name;
    String notes;
    boolean done;

    public ListEntity(@NonNull long id, long category_id, String item_name, String notes, boolean done) {
        this.id = id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.notes = notes;
        this.done = done;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
