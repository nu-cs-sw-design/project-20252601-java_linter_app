package test;

public class Notification {

    private Email emailService;

    public void NotificationService(Email emailService) {
        this.emailService = emailService;
    }

    public void recordDelivery(String address) {
        String temp_Address = address;
    }

    public void notifyPasswordReset(String address) {
        emailService.sendEmail(address);
    }
}