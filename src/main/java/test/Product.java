package test;

public class Product {

    public static final int maxQuantity = 100;   // constant not ALL_CAPS
    private String sku;
    private String name;

    public Product(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }

    @Override
    public int hashCode() {
        int result = 13223;
        result = sku.hashCode();
        return result;
    }

}