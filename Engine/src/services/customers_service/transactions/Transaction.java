package services.customers_service.transactions;

public class Transaction implements TransactionInterface {

    private int yaz;
    private float amount;
    private char type;//'+' for income, '-' for expense
    private float oldAcountBalance;
    private float newAccountBalance;

    public Transaction(int currYaz, float amount, char type, float oldAcountBalance, float newAccountBalance)
    {
        this.yaz=currYaz;
        this.amount=amount;
        this.type=type;
        this.oldAcountBalance = oldAcountBalance;
        this.newAccountBalance = newAccountBalance;
    }

    public Transaction(Transaction trans) {
        this.yaz=trans.yaz;
        this.amount=trans.amount;
        this.type=trans.type;
        this.oldAcountBalance = trans.oldAcountBalance;
        this.newAccountBalance = trans.newAccountBalance;
    }

    @Override
    public int getYaz() {
        return this.yaz;
    }

    @Override
    public float getAmount() {
        return this.amount;
    }

    @Override
    public char getType() {
        return this.type;
    }

    @Override
    public float getOldAcountBalance() {
        return this.oldAcountBalance;
    }

    @Override
    public float getNewAccountBalance() {
        return this.newAccountBalance;
    }
}
