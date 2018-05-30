package ttc.project.stoku.model;

public class Category {
    long id;
    String name;
    int colorId;

    public Category(long id, String name, int colorId) {
        this.id = id;
        this.name = name;
        this.colorId = colorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
