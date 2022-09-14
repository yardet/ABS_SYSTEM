package services.loans_service.payments;

public class Payment implements PaymentsInterface{
    private String loanId;
    private int capital;
    private float interest;
    private float amount;
    private int Yaz;
    private boolean isDone;

    public Payment(String loanId, int capital, float interest, float amount, int Yaz)
    {
        this.loanId = loanId;
        this.capital = capital;
        this.interest = interest;
        this.amount=amount;
        this.Yaz=Yaz;
        this.isDone=false;
    }

    public Payment(Payment payment) {
        this.loanId = payment.loanId;
        this.capital = payment.capital;
        this.interest = payment.interest;
        this.amount=payment.amount;
        this.Yaz=payment.Yaz;
        this.isDone=payment.isDone;
    }

    @Override
    public int getYaz(){return this.Yaz;}

    @Override
    public float getAmount(){return this.amount;}

    @Override
    public boolean isItDone(){return this.isDone;}

    @Override
    public void updateYaz(int yaz) {this.Yaz+=yaz;}

    @Override
    public void updateAmount(float amount) {
        this.amount=amount;
    }

    @Override
    public void updateIsDone(){this.isDone=true;}

    public String getLoanId() {
        return loanId;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDone(boolean b) {
        this.isDone=true;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public float getInterest() {
        return interest;
    }

    public void setInterest(float interest) {
        this.interest = interest;
    }
}
