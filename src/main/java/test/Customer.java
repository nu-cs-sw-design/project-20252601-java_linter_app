package test;

public class Customer {
    private long id;
    private String firstName;
    private String lastName;
    public Customer(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    @Override
    public boolean equals(Object o) {
        Customer customer = (Customer) o;
        return id == customer.id;
    }
}