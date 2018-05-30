package ttc.project.stoku.model;

public class Mart {
    String name, link, endpoint;

    public Mart(String name, String link, String endpoint) {
        this.name = name;
        this.link = link;
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
