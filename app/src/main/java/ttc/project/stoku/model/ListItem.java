package ttc.project.stoku.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class ListItem implements Parcelable, Comparable<ListItem> {
    int category_color;
    String category_name;
    long id;
    long category_id;
    String name;
    String notes;
    boolean done;

    public ListItem(long id, long category_id, int category_color, String category_name, String name, String notes, boolean done) {
        this.category_color = category_color;
        this.category_name = category_name;
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.notes = notes;
        this.done = done;
    }

    public int getCategory_color() {
        return category_color;
    }

    public void setCategory_color(int category_color) {
        this.category_color = category_color;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    protected ListItem(Parcel in) {
        category_color = in.readInt();
        category_name = in.readString();
        id = in.readLong();
        category_id = in.readLong();
        name = in.readString();
        notes = in.readString();
        done = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_color);
        dest.writeString(category_name);
        dest.writeLong(id);
        dest.writeLong(category_id);
        dest.writeString(name);
        dest.writeString(notes);
        dest.writeByte((byte) (done ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    @Override
    public int compareTo(@NonNull ListItem userItem) {
        int compareCategoryId = (int)userItem.getCategory_id();
        /* For Ascending order*/
        return (int)this.category_id-compareCategoryId;
    }
}