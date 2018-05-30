package ttc.project.stoku.model;

public class PromoItem {
    String title, description, expired;
    String promo_image_link;

    public PromoItem(String title, String description, String expired, String promo_image_link) {
        this.title = title;
        this.description = description;
        this.expired = expired;
        this.promo_image_link = promo_image_link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getPromo_image_link() {
        return promo_image_link;
    }

    public void setPromo_image_link(String promo_image_link) {
        this.promo_image_link = promo_image_link;
    }
}
