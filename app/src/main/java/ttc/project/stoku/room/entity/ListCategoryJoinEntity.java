package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "list_category_join_table")
public class ListCategoryJoinEntity {
    String category_name;
    @PrimaryKey
    long id;
    long category_id;
    String item_name;
    String notes;
    boolean done;
    int colorId;

    @Ignore
    public ListCategoryJoinEntity(String category_name, long id, long category_id, String item_name, String notes, boolean done, int colorId) {
        this.category_name = category_name;
        this.id = id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.notes = notes;
        this.done = done;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public ListCategoryJoinEntity(){

    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setItem_name(String name) {
        this.item_name = name;
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
