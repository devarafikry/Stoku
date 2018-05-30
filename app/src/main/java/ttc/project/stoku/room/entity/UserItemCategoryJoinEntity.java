package ttc.project.stoku.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ttc.project.stoku.model.Category;
import ttc.project.stoku.model.UserItem;

@Entity(tableName = "useritem_category_join_table")
public class UserItemCategoryJoinEntity {
    String category_name;
    @PrimaryKey
    long id;
    long category_id;
    String item_name;
    String notes;
    boolean available;
    int colorId;

    @Ignore
    public UserItemCategoryJoinEntity(String category_name, long id, long category_id, String item_name, String notes, boolean available, int colorId) {
        this.category_name = category_name;
        this.id = id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.notes = notes;
        this.available = available;
        this.colorId = colorId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public UserItemCategoryJoinEntity(){

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
