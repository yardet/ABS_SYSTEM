package loans.payments;

import java.util.ArrayList;
import java.util.List;

public class PaymentsPerCustomerDto {
    private List<PaymentDto> payments;//active
    private List<PaymentDto> riskPaymentsList;
    private int capital;
    private int nextPaymentIndex;

    public PaymentsPerCustomerDto(int capital,int nextPaymentIndex,List<PaymentDto> payments,List<PaymentDto> riskPayments)
    {
        this.payments=payments;
        this.riskPaymentsList=riskPayments;
        this.capital=capital;
        this.nextPaymentIndex=nextPaymentIndex;
    }
    public int getCapital(){return this.capital;}

    public List<PaymentDto> getAllRiskPayments(){return this.riskPaymentsList;}

    public PaymentDto getNextPayment(){return payments.get(nextPaymentIndex);}//maybe will use later

    public List<PaymentDto> paidPaymentDtosList(){
        List<PaymentDto> paidPaymentsList=new ArrayList<>();
        for(int i=0;i<nextPaymentIndex;i++)
        {paidPaymentsList.add(payments.get(i));}
        return paidPaymentsList;
    }

    public List<PaymentDto> futurePaymentDtosList(){
        //List<PaymentDto> riskPayments = this.riskPaymentsList.subList(this.nextPaymentIndex,this.riskPaymentsList.size());
        List<PaymentDto> activePayments = this.payments.subList(this.nextPaymentIndex,this.payments.size());
        List<PaymentDto> allPayments= new ArrayList<>();
        //allPayments.addAll(riskPayments);
        allPayments.addAll(activePayments);
        return allPayments;
    }

    public float riskPaymentsAmount(){
        float amount=0;
        for(int i=0;i<riskPaymentsList.size();i++)
            amount+=payments.get(i).getTotal();
        return amount;
    }
}
