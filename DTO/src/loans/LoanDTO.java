package loans;

import loans.payments.PaymentsPerCustomerDto;
import services.loans_service.Loan_status;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LoanDTO {
    private String id;
    private String owner;
    private String category;
    private int capital;//The amount of the loan
    private int totalYaz;//duration of the loan
    private int paymentRate; //payment rate
    private Loan_status status;
    private float interestPerPayment;//interest



    private int initalYaz;//when became active
    private int nextPaymentYaz;
    private int finalYaz;
    private Map<String, PaymentsPerCustomerDto> lendersMap;
    private PaymentsPerCustomerDto ownerPayments;
    private List<String> lendersNames;
    private float accumulatedByLenders;
    private float leftToPayByLenders;

    private float totalPaidPaymentsAmount;
    private float totalAmountLeftToPay;
    private float capitalPaidPaymentAmount;
    private float capitalLeftToPay;
    private float interestPaidPaymentAmount;
    private float interestAmountLeftToPay;
    private float nextPaymentAmountSum;



    //private Button lenderButton=new Button();
    //private static Button activeButton=new Button();
    //private static Button riskButton=new Button();



    public LoanDTO(String id, String owner, String category, int capital, int totalYazTime, int initalYaz, int finalYaz, Loan_status status, int paymentRate
            , int nextPaymentYaz, float interestPerPayment, Map<String, PaymentsPerCustomerDto> lenders, PaymentsPerCustomerDto ownerPayments, List<String> lendersNames, float accumulatedByLenders, float leftToPayByLenders, float totalPaidPaymentsAmount, float totalAmountLeftToPay
            , float capitalPaidPaymentAmount, float capitalLeftToPay, float interestPaidPaymentAmount, float interestAmountLeftToPay, float nextPaymentAmountSum)
    {
        this.id=id;
        this.owner=owner;
        this.category=category;
        this.capital=capital;
        this.totalYaz=totalYazTime;
        this.initalYaz=initalYaz;
        this.finalYaz=finalYaz;
        this.status=status;
        this.paymentRate=paymentRate;
        this.ownerPayments = ownerPayments;
        this.lendersNames = lendersNames;

        this.accumulatedByLenders = accumulatedByLenders;
        this.interestPerPayment=interestPerPayment;
        this.nextPaymentYaz=nextPaymentYaz;
        this.lendersMap=lenders;
        this.leftToPayByLenders = leftToPayByLenders;
        this.totalPaidPaymentsAmount=totalPaidPaymentsAmount;
        this.totalAmountLeftToPay=totalAmountLeftToPay;
        this.capitalPaidPaymentAmount=capitalPaidPaymentAmount;
        this.capitalLeftToPay=capitalLeftToPay;
        this.interestPaidPaymentAmount=interestPaidPaymentAmount;
        this.interestAmountLeftToPay=interestAmountLeftToPay;
        this.nextPaymentAmountSum=nextPaymentAmountSum;
    }


    public Set<String> getLendersKeys(){return this.lendersMap.keySet();}//List of all lenders already registered in it-1

    public String getId() {
        return this.id;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getCategory() {
        return this.category;
    }

    public int getCapital() {
        return this.capital;
    }

    public int getTotalYaz() {
        return this.totalYaz;
    }

    public int getPaymentRate() {
        return this.paymentRate;
    }

    public float getInterestPerPayment() {
        return this.interestPerPayment;
    }

    public Loan_status getStatus() {
        return this.status;
    }

    public int getInitalYaz() {
        return this.initalYaz;
    }

    public int getFinalYaz(){return this.finalYaz;}

    public int getNextPaymentYaz(){return this.nextPaymentYaz;}

    public PaymentsPerCustomerDto getPaymentsByName(String name){return this.lendersMap.get(name);}

    public Map<String, PaymentsPerCustomerDto> getLendersMap(){return this.lendersMap;}

    public float getTotalPaidPaymentsAmount() {
        return totalPaidPaymentsAmount;
    }

    public float getCapitalPaidPaymentAmount() {
        return capitalPaidPaymentAmount;
    }

    public float getCapitalLeftToPay() {
        return capitalLeftToPay;
    }

    public float getInterestPaidPaymentAmount() {
        return interestPaidPaymentAmount;
    }

    public float getInterestAmountLeftToPay() {
        return interestAmountLeftToPay;
    }

    public float getNextPaymentAmountSum() {
        return nextPaymentAmountSum;
    }

    public float getAccumulatedByLenders() {
        return accumulatedByLenders;
    }



    //maybe will use later
    public float getTotalAmountLeftToPay() {
        return totalAmountLeftToPay;
    }

    public List<PaymentsPerCustomerDto> getAllPaymentsInfo() {
        return this.lendersMap.values().stream().collect(Collectors.toList());
    }

    public float getRiskPaymentsAmount()
    {
        final float[] amount = {0};
        lendersMap.forEach((customer, paymentsPerCustomer) ->{
            amount[0] +=paymentsPerCustomer.riskPaymentsAmount();
        });
        return amount[0];
    }

    public float getLeftToPayByLenders() {
        return leftToPayByLenders;
    }

    public List<String> getLendersNames() {
        return lendersNames;
    }

    public PaymentsPerCustomerDto getOwnerPayments() {
        return ownerPayments;
    }

  /*  public Button getLenderButton() {
        return lenderButton;
    }

    public Button getActiveButton() {
        return activeButton;
    }

    public Button getRiskButton() {
        return riskButton;
    }*/
}

