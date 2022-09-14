package loans.payments;

public class PaymentDto {//CHANGED
    private String loanId;
    private int capital;
    private float interest;
    private float total;
    private int Yaz;
    private boolean isDone;

    //private Button pay=new Button();

    protected PaymentDto(String loanId, int capital, float interest, int Yaz, boolean isDone) {
        this.loanId = loanId;
        this.capital = capital;
        this.interest = interest;
        this.Yaz = Yaz;
        this.isDone = isDone;
        this.total=this.capital + this.interest;
    }
    public int getCapital() {
        return this.capital;
    }

    public float getInterest() {
        return this.interest;
    }

    public int getYaz() {
        return this.Yaz;
    }

    public boolean isItDone() {
        return this.isDone;
    }//maybe will use later

    public float getTotal() {
        return total;
    }

    public String getLoanId() {
        return loanId;
    }

   /* public Button getPay() {
        return pay;
    }*/
}

