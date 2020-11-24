import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;


public class Product {


    private int id;
    private static final AtomicInteger count = new AtomicInteger(100);
    private String name;
    private String description;
    private double price;
    private LocalDate offerDate;
    private LocalDate sellDate;
    private Boolean available;

    public User getSeller() {
        return seller;
    }

    public User setSeller(User seller) {
        return this.seller = seller;
    }

    private User seller;
    private User buyer;

    public Product(int pid, String name, String description, double price,User user) {

//        this.id = setId(count.incrementAndGet());
        this.id = pid;
        this.name = name;
        this.description = description;
        this.price = price;
        LocalDateTime ldt = LocalDateTime.now();
        this.offerDate = ldt.toLocalDate();
        this.sellDate = null;
        this.available = true;
        this.buyer = null;
        this.seller = setSeller(user);

    }

    public int setId(int id) {
        return this.id = id;
    }


    public final int getId() {
        return id;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }


    public LocalDate getSellDate() {
        return sellDate;
    }

    public void setSellDate(LocalDate sellDate) {
        this.sellDate = sellDate;
    }


    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }




}
