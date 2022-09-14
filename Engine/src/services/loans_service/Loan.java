package services.loans_service;

import services.categories_service.Category;
import services.loans_service.payments.Payment;
import services.loans_service.payments.PaymentsPerCustomer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Loan implements Loan_interface {
    private String id;
    private String owner;
    private Category category;
    private int capital;//The amount of the loan
    private int totalYazTime;//duration of the loan
    private int paymentRate; //payment rate
    private float interestPerPayment;//interest
    private Loan_status status;

    //addition
    private float totalPayment;
    private float accumulatedByLenders;
    private int initalYaz;
    private int finalYaz;
    private int nextPaymentYaz;
    private int numOfPayments;
    private final Map<String, PaymentsPerCustomer> lendersMap = new HashMap<>();
    private PaymentsPerCustomer ownerPayments;

    Loan(String id, String owner, Category category, int capital, int totalYazTime, int paymentRate, float interestPerPayment) {
        this.id = id;
        this.owner = owner;
        this.category = category;
        this.capital = capital;
        this.totalYazTime = totalYazTime;
        this.paymentRate = paymentRate;
        this.interestPerPayment = interestPerPayment;
        this.status = Loan_status.New;

        this.numOfPayments = totalYazTime / paymentRate;
        this.totalPayment=capital+capital*interestPerPayment/100;
        this.accumulatedByLenders=0;
        this.ownerPayments=new PaymentsPerCustomer(this.id, capital,interestPerPayment,numOfPayments,totalPayment/numOfPayments);

    }

    public Loan(Loan loan) {

        this.id = loan.id;
        this.owner = loan.owner;
        this.category = loan.category;
        this.capital = loan.capital;
        this.totalYazTime = loan.totalYazTime;
        this.paymentRate = loan.paymentRate;
        this.interestPerPayment = loan.interestPerPayment;
        this.status = loan.status;

        this.totalPayment=loan.totalPayment;
        this.accumulatedByLenders=loan.accumulatedByLenders;
        this.initalYaz = loan.initalYaz;
        this.finalYaz = loan.finalYaz;
        this.nextPaymentYaz = loan.nextPaymentYaz;
        this.numOfPayments = loan.numOfPayments;

        this.ownerPayments = new PaymentsPerCustomer(loan.ownerPayments);

        loan.lendersMap.forEach((key, lender)-> {
            PaymentsPerCustomer newLender = new PaymentsPerCustomer(lender);
            this.lendersMap.put(new String(key), newLender);
        });

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getOwnerName() {
        return this.owner;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public int getCapital() {
        return this.capital;
    }

    @Override
    public int getTotalYaz() {
        return this.totalYazTime;
    }

    @Override
    public int getPaymentRate() {
        return this.paymentRate;
    }

    @Override
    public float getInterestPerPayment() {
        return this.interestPerPayment;
    }

    @Override
    public Loan_status getStatus() {
        return this.status;
    }

    @Override
    public float getTotalPayment() {
        return this.capital + this.capital * (this.interestPerPayment / 100);
    }

    @Override
    public int getInitalYaz() {
        return this.initalYaz;
    }

    @Override
    public int getFinalYaz() {
        return this.finalYaz;
    }

    @Override
    public Map<String, PaymentsPerCustomer> getLendersMap() {
        return this.lendersMap;
    }

    public Set<String> getLendersKeys() {
        return this.lendersMap.keySet();
    }

    @Override
    public List<PaymentsPerCustomer> getAllPayments() {
        return this.lendersMap.values().stream().collect(Collectors.toList());}

    @Override
    public int getNextPaymentYaz() {
        return this.nextPaymentYaz;
    }

    @Override
    public float getAccumulatedByLenders() {
        return this.accumulatedByLenders;
    }

    @Override
    public PaymentsPerCustomer getPaymentsByName(String name){return this.lendersMap.get(name);}

    @Override
    public List<Payment> getPaymentsListByName(String name){return this.getPaymentsByName(name).getActivePaymentsList();

    }
    @Override
    public PaymentsPerCustomer getOwnerPayments(){return this.ownerPayments;}

    @Override
    public float getTotalPaymentsAmount() {
        return this.capital+this.capital*this.interestPerPayment/100;
    }

    @Override
    //left to pay
    public float totalLeftToPayAmount() {
        return this.totalPayment-this.totalPaidPaymentsAmount();
    }

    @Override
    public float capitalLeftToPayAmount() {
        return this.capital - this.capitalPaidPaymentsAmount();
    }

    @Override
    public float interestLeftToPayAmount() {
        return this.interestPerPayment/100*this.capital - interestPaidPaymentsAmount();
    }

    //accumulated:
    @Override
    public float totalPaidPaymentsAmount() {

        final float[] amount = {0};
        lendersMap.forEach((customer, paymentsPerCustomer) ->{
            amount[0] +=paymentsPerCustomer.totalPaidPaymentsAmount(this.status);
        });
        return amount[0];
    }

    @Override
    public float capitalPaidPaymentsAmount() {

        final float[] amount = {0};
        lendersMap.forEach((customer, paymentsPerCustomer) ->{
            amount[0] +=paymentsPerCustomer.capitalPaidPaymentsAmount(this.getStatus());
        });
        return amount[0];
    }

    @Override
    public float interestPaidPaymentsAmount() {

        final float[] amount = {0};
        lendersMap.forEach((customer, paymentsPerCustomer) ->{
            amount[0] +=paymentsPerCustomer.interestPaidPaymentsAmount(this.getStatus());
        });
        return amount[0];
    }

    @Override
    //next payment
    public float nextPaymentsAmountSum(){
        if(this.status==Loan_status.active ||this.status==Loan_status.risk)
        {
            final float[] amount = {0};
            lendersMap.forEach((customer, paymentsPerCustomer) ->{
                amount[0] +=paymentsPerCustomer.getNextPayment().getAmount();
            });
            return amount[0];
        }
        else
            return this.totalPayment/this.numOfPayments;


    }

    @Override
    public void setStatus(Loan_status status) {
        this.status = status;
    }

    @Override
    public void updateFinalYaz(){this.finalYaz+=this.paymentRate;}

    @Override
    public void updateTotalYazTime(){this.totalYazTime+=this.paymentRate;}


    @Override
    public void UpdateNextPaymentYaz(){this.nextPaymentYaz=this.nextPaymentYaz+this.paymentRate;}

    @Override
    public void updateNextPamentYaz(){
        this.nextPaymentYaz+=this.paymentRate;}

    @Override
    public void addLender(String name, int capital) {
        this.accumulatedByLenders+=capital;
        float singlePayment = (capital + capital * interestPerPayment/100)/ numOfPayments;//A fixed amount to be paid according to the rate
        PaymentsPerCustomer payments= new PaymentsPerCustomer(this.id, capital,this.interestPerPayment,this.numOfPayments,singlePayment);//changed
        this.lendersMap.put(name, payments);}

    @Override
    public void RiskTreatment(List<Payment> paymentsList)
    {

        //owner
        PaymentsPerCustomer ownerPayments=this.getOwnerPayments();
        ownerPayments.getRiskPaymentsList().clear();

        if(paymentsList.get(paymentsList.size()-1).getYaz()==this.finalYaz)
        {
            this.updateFinalYaz();//Rejects the loan to: another YAZ number as converted at the payment rate
            this.updateTotalYazTime();}

        ownerPayments.getRiskPaymentsList().addAll(paymentsList);

        List<PaymentsPerCustomer> lenderPayments=this.getAllPayments();

        lenderPayments.forEach(lender->
        {
            lender.getRiskPaymentsList().clear();
            lender.getRiskPaymentsList().addAll((lender.getActivePaymentsList().subList(0,paymentsList.size()-1)));
        });
    }

    @Override
    public void updateStatus(int currYaz)
    {
        if(this.getStatus()==Loan_status.New)
            this.setStatus(Loan_status.pending);

        else if(this.status==Loan_status.active)
        {
            if(!this.ownerPayments.getRiskPaymentsList().isEmpty())
                this.status=Loan_status.risk;

            if(this.totalPaidPaymentsAmount()>=this.getTotalPayment())
                this.setStatus(Loan_status.finished);
        }
        if(this.accumulatedByLenders>=this.capital)
        {
            if(this.status==Loan_status.pending)
            {
                this.setStatus(Loan_status.active);
                this.initalYaz=currYaz;
                this.nextPaymentYaz=currYaz;
                this.finalYaz=this.initalYaz+this.totalYazTime;
                this.updateNextPamentYaz();
                this.getLendersKeys().forEach(name->this.getPaymentsByName(name).splitPayments(currYaz,this.paymentRate));
                this.ownerPayments.splitPayments(currYaz,this.paymentRate);
            }
            if(this.ownerPayments.getRiskPaymentsList().isEmpty() && this.status==Loan_status.risk){
                this.setStatus(Loan_status.active);
                this.updateNextPamentYaz();
            }


        }
    }

}




