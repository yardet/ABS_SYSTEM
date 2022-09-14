package services.loans_service;


import services.categories_service.Category;
import services.loans_service.payments.Payment;
import services.loans_service.payments.PaymentsPerCustomer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Loan_interface {

    String getId();
    String getOwnerName();
    Category getCategory();
    int getCapital();
    int getTotalYaz();
    int getPaymentRate();
    float getInterestPerPayment();
    Loan_status getStatus();
    float getTotalPayment();
    int getInitalYaz();
    int getFinalYaz();
    List<PaymentsPerCustomer> getAllPayments();
    int getNextPaymentYaz();
    float getAccumulatedByLenders();
    Set<String> getLendersKeys();
   PaymentsPerCustomer getPaymentsByName(String name);
   PaymentsPerCustomer getOwnerPayments();

    void updateStatus(int currYaz);
    void setStatus(Loan_status status);
    void updateFinalYaz();
    void updateTotalYazTime();
    void updateNextPamentYaz();

    void addLender(String name, int capital);
    void RiskTreatment(List<Payment> paymentsList);
    float totalLeftToPayAmount();
    float capitalLeftToPayAmount();
    float interestLeftToPayAmount();
    float totalPaidPaymentsAmount();
    float capitalPaidPaymentsAmount();
    float interestPaidPaymentsAmount();
    float nextPaymentsAmountSum();


    //maybe use later
    Map<String, PaymentsPerCustomer> getLendersMap();
    List<Payment> getPaymentsListByName(String name);
    float getTotalPaymentsAmount();
    void UpdateNextPaymentYaz();
}
