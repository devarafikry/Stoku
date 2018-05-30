package ttc.project.stoku.model;

public class Report {
    long timemillis, id;
    double price;

    public Report(long timemillis, long id, double price) {
        this.timemillis = timemillis;
        this.id = id;
        this.price = price;
    }

    public long getTimemillis() {
        return timemillis;
    }

    public void setTimemillis(long timemillis) {
        this.timemillis = timemillis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
