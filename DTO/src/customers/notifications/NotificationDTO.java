package customers.notifications;

public class NotificationDTO {
     private String loanName;
     private int yaz;
     private float finalAmount;

    public NotificationDTO(String loanName, int yaz, float finalAmount) {
        this.loanName = loanName;
        this.yaz = yaz;
        this.finalAmount = finalAmount;
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
