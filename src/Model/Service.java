package Model;

import Model.GeneralObject;

public class Service extends GeneralObject {
    // Service Variables
    private String servCode;
    private String description;
    private String price;

    public Service(
        String servCode,
        String description,
        String price,
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

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
