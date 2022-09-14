package services.customers_service.transactions;

public interface TransactionInterface {
    int getYaz();
    float getAmount();
    char getType();
    float getOldAcountBalance();
    float getNewAccountBalance();
}
