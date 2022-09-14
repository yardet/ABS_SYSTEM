package services.loans_service.payments;

public interface PaymentsInterface {
    int getYaz();
    float getAmount();
    boolean isItDone();
    void updateYaz(int yaz);
    void updateAmount(float amount);
    void updateIsDone();//maybe will use later
}
