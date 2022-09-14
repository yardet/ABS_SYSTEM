package services.customers_service;

import services.customers_service.notification.Notification;
import services.customers_service.transactions.Transaction;

import java.util.List;

public interface CustomerInteface {

    String getName();
    float getBalance();
    void cashLoading(int amount,int yaz);
    void cashWithdrawal(int amount,int yaz);
    List<Transaction> getAllTransactionsInfo();
    void addTranscation(int currYaz, float amount, char type);
    boolean thereIsEnoughInPassing(float amount);
    List<Notification> getAllNotifications();

}
