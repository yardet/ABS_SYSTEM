package customers.transactions;

public class TransactionDTO {
    private int yaz;
    private float amount;
    private char type;//'+' for income, '-' for expense
    private float oldAcountBalance;
    private float newAcountBalance;


    public TransactionDTO(int currYaz, float amount, char type, float oldAccountBalance, float newAccountBalance)
    {
        this.yaz=currYaz;
        this.amount=amount;
        this.type=type;
        this.oldAcountBalance =oldAccountBalance;
        this.newAcountBalance = newAccountBalance;
    }

    public int getYaz() {
        return this.yaz;
    }

    public float getAmount() {
        return this.amount;
    }

    public char getType() {
        return this.type;
    }

    public float getOldAcountBalance() {
        return this.oldAcountBalance;
    }

    public float getNewAcountBalance() {
        return this.newAcountBalance;
    }
}
