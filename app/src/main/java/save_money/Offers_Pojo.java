package save_money;

public class Offers_Pojo {

    String id;
    String ShopName;
    String area;
    String offers;

    public Offers_Pojo(String id, String shopName, String area, String offers) {
        this.id = id;
        ShopName = shopName;
        this.area = area;
        this.offers = offers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }
}
