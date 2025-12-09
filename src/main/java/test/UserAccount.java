package test;

public class UserAccount extends Base implements BaseInterface {

    public String displayName;   // public and mutable
    private String passwordHash;

     UserAccount(long id, String displayName, String passwordHash) {
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }
}