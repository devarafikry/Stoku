package ttc.project.stoku.model;

public class Catalog {
    String title, image_link, endpoint, date;

    public Catalog(String title, String image_link, String endpoint, String date) {
        this.title = title;
        this.image_link = image_link;
        this.endpoint = endpoint;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
