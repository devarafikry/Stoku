package ttc.project.stoku.room.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "report_item_table", foreignKeys =
        {@ForeignKey(entity = CategoryEntity.class,
        parentColumns = "id",
        childColumns = "category_id",
        onDelete = CASCADE),
                @ForeignKey(entity = ReportEntity.class,
                        parentColumns = "id",
                        childColumns = "report_id",
                        onDelete = CASCADE)},indices = {@Index("category_id"),@Index("report_id")} )
public class ReportItemEntity {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    long id;
    long report_id = 1;
    long category_id = 1;
    String item_name;
    String notes;

    public ReportItemEntity(@NonNull long id, long report_id, long category_id, String item_name, String notes) {
        this.id = id;
        this.report_id = report_id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.notes = notes;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
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
}
