package services.customers_service.notification;

public class Notification {
    final private String loanName;
    final private int yaz;
    final private float finalAmount;

    public Notification(String loanName, int yaz, float finalAmount) {
        this.loanName = loanName;
        this.yaz = yaz;
        this.finalAmount = finalAmount;
    }

    public Notification(Notification notification) {
        this.loanName = notification.loanName;
        this.yaz = notification.yaz;
        this.finalAmount = notification.finalAmount;
    }

    public String getLoanName() {
        return loanName;
    }

    public int getYaz() {
        return yaz;
    }

    public float getFinalAmount() {
        return finalAmount;
    }
}
