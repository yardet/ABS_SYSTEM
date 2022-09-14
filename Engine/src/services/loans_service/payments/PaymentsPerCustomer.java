package services.loans_service.payments;

import services.loans_service.Loan_status;

import java.util.ArrayList;
import java.util.List;

public class PaymentsPerCustomer {
    private String loanId;
    private List<Payment> activePayments;
    private List<Payment> riskPayments;//without risk payments
    float totalAmount;
    float interest;
    int capital;
    int nextPaymentIndex;
    float singlePayment;
    int numOfPayments;

    public PaymentsPerCustomer(String loanId, int capital, float interest, int numOfPayments, float singlePayment)
    {
        this.loanId = loanId;
        this.capital=capital;
        this.interest=interest;
        this.nextPaymentIndex=0;
        this.activePayments =new ArrayList<>();
        this.riskPayments=new ArrayList<>();
        this.singlePayment=singlePayment;
        this.totalAmount=capital+capital*interest/100;
        this.numOfPayments=numOfPayments;
    }

    public PaymentsPerCustomer(PaymentsPerCustomer ownerPayments) {
        this.loanId = ownerPayments.loanId;
        this.capital=ownerPayments.capital;
        this.interest=ownerPayments.interest;
        this.nextPaymentIndex=ownerPayments.nextPaymentIndex;
        this.activePayments = new ArrayList<>();
        ownerPayments.activePayments.forEach(payment ->this.activePayments.add(new Payment(payment)));
        this.riskPayments=new ArrayList<>();
        ownerPayments.riskPayments.forEach(payment ->this.riskPayments.add(new Payment(payment)));
        this.singlePayment=ownerPayments.singlePayment;
        this.totalAmount=ownerPayments.totalAmount;
        this.numOfPayments=ownerPayments.numOfPayments;
    }

    public int getCapital(){return this.capital;}

    public int getNumOfPayments(){return this.numOfPayments;}

    public int getNextPaymentIndex(){return this.nextPaymentIndex;}

    public List<Payment> getActivePaymentsList(){return this.activePayments;}//without Risk payments

    public List<Payment> getRiskPaymentsList(){return this.riskPayments;}

    public Payment getNextPayment(){
        return this.activePayments.stream().filter(
            payment -> !payment.isItDone()).findFirst().get();
    }

    public int getNextPaymentYaz(){return this.getNextPayment().getYaz();}



    public void addPaymentToRiskList(Payment payment,int index) {
        this.riskPayments.add(payment);
        this.activePayments.remove(index);
    }

    public void updateNextPaymentIndex(){
            this.nextPaymentIndex++;}


    public void addToTheNextPayment(float amount){this.getNextPayment().updateAmount(amount+this.getNextPayment().getAmount());}


    public void splitPayments(int yaz, int paymentsRate)
    {
        for(int i=0; i<this.getNumOfPayments(); i++)
        {
            this.activePayments.add(i,new Payment(loanId, (int)capital/this.numOfPayments, (interest/100)*capital/numOfPayments, singlePayment,yaz+(i+1)*paymentsRate));
        }
    }

    //left to pay:
    public float totalLeftToPayAmount(Loan_status status){//including interest

        //float payments_len=this.activePayments.size();
        final float[] sum = {0};
        if(status==Loan_status.active||status==Loan_status.risk){
            this.getActivePaymentsList().subList(nextPaymentIndex,activePayments.size())
                    .forEach(payment ->
                            sum[0] +=payment.getAmount());
            return sum[0];
        }
            //return  (this.totalAmount/this.numOfPayments)*(payments_len-nextPaymentIndex);
        else
            return (this.totalAmount);
    }

    public float leftToPayCapitalAmount(Loan_status status){//only capital
        //float payments_len=this.activePayments.size();
        final float[] sum = {0};
        if(status==Loan_status.active||status==Loan_status.risk){
            this.getActivePaymentsList().subList(nextPaymentIndex,activePayments.size())
                    .forEach(payment ->
                            sum[0] +=payment.getCapital());
            return sum[0];
        }
               // return  (this.capital/this.numOfPayments)*(payments_len-nextPaymentIndex);
        else
            return (this.capital);


    }
    public float leftToPayInterestAmount(Loan_status status){//only interest
        final float[] sum = {0};

        if(status==Loan_status.active||status==Loan_status.risk)
            {
            this.getActivePaymentsList().subList(nextPaymentIndex,activePayments.size())
                    .forEach(payment ->
                            sum[0] +=payment.getInterest());
            return sum[0];
        }
            //return  (this.interest/100*this.capital) * (payments_len-nextPaymentIndex) / this.numOfPayments;
        else
            return (this.interest/100*this.capital);
    }

    //paid:
    public float totalPaidPaymentsAmount(Loan_status status){//including interest
        return this.totalAmount-this.totalLeftToPayAmount(status);
    }

    public float capitalPaidPaymentsAmount(Loan_status status){//only capital
        return this.capital-this.leftToPayCapitalAmount(status);
    }
    public float interestPaidPaymentsAmount(Loan_status status){//only interest
        return (this.capital*this.interest/100)-this.leftToPayInterestAmount(status);
    }
}
