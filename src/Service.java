public class Service extends GeneralObject {
    // Service Variables
    private String servCode;
    private String description;
    private float price;

    public Service(
        String servCode,
        String description,
        float price,
        char delIndicator,
        String delReason
    ) {
        super(delIndicator, delReason);
        this.servCode = servCode;
        this.description = description;
        this.price = price;
    }

    public String getServCode() {
        return servCode;
    }

    public void setServCode(String servCode) {
        this.servCode = servCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    
}
