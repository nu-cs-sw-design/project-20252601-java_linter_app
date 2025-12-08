package test;

public class Email {

    private Notification notificationService;

    public Email(Notification notificationService) {
        this.notificationService = notificationService;
    }

    public void sendEmail(String address) {
        notificationService.recordDelivery(address);
    }
}