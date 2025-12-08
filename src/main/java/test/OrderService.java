package test;

public class OrderService {

    // HAS-MANY through array
    public Product[] recentProducts = new Product[0];

    private Customer lastCustomer;

    public void processOrder(Customer customer, Product[] products) {
        this.lastCustomer = customer;
        this.recentProducts = products;
    }
    public Customer getLastCustomer() {
        return lastCustomer;
    }
}
