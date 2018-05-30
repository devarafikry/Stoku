package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import ttc.project.stoku.room.dao.ReportItemCategoryJoinDao;

@Entity(tableName = "report_item_category_join_table")
public class ReportItemCategoryJoinEntity {
    String category_name;
    @PrimaryKey
    long id;
    long report_id;
    long category_id;
    String item_name;
    String notes;
    int colorId;

    public ReportItemCategoryJoinEntity(){

    }

    @Ignore
    public ReportItemCategoryJoinEntity(String category_name, long id, long report_id, long category_id, String item_name, String notes, int colorId) {
        this.category_name = category_name;
        this.id = id;
        this.report_id = report_id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.notes = notes;
        this.colorId = colorId;
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

    public long getReport_id() {
        return report_id;
    }

    public void setReport_id(long report_id) {
        this.report_id = report_id;
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

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
