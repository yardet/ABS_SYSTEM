package services.customers_service;

import services.customers_service.notification.Notification;
import services.customers_service.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Customer implements CustomerInteface {

    private List<Transaction> transactionsList;
    private final String name;
    private float balance;
    private List<Notification> notifications;

    public Customer(String name, float balance)
    {
        this.name=name;
        this.balance=balance;
        this.transactionsList=new ArrayList<>();
        this.notifications=new ArrayList<>();
    }

    public Customer(Customer customer) {
        this.name=customer.name;
        this.balance=customer.balance;
        this.transactionsList=new ArrayList<>();
        customer.getAllTransactionsInfo().forEach(trans -> this.transactionsList.add(new Transaction(trans)));
        this.notifications=new ArrayList<>();
        customer.getAllNotifications().forEach(notification -> this.notifications.add(new Notification(notification)));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getBalance() {
        return this.balance;
    }

    @Override
    public List<Transaction> getAllTransactionsInfo() {
        return this.transactionsList;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return this.notifications;
    }


    @Override
    public void cashLoading(int amount,int yaz) {
        this.addTranscation(yaz,(float)amount,'+');
        //this.balance+=amount;
    }

    @Override
    public void cashWithdrawal(int amount,int yaz) {
        this.addTranscation(yaz,(float)amount,'-');
    }

    @Override
    public void addTranscation(int currYaz, float amount, char type)
    {

        float oldAccountBalance=this.balance;
        if(type=='+')
            this.balance+=amount;
        else
        {
            if(!thereIsEnoughInPassing(amount))
            {
                amount=oldAccountBalance;
            }
            this.balance-=amount;
        }
        this.transactionsList.add(new Transaction(currYaz,amount,type,oldAccountBalance, this.balance));

    }
    @Override
    public boolean thereIsEnoughInPassing(float amount) {return this.balance>=amount;}


    public void addNotification(String loanId, int yaz, float totalAmount) {
        this.notifications.add(new Notification(loanId, yaz, totalAmount));
    }
}
