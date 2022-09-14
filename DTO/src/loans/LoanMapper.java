package loans;


import loans.payments.PaymentDto;
import loans.payments.PaymentMapper;
import loans.payments.PaymentsPerCustomerDto;
import loans.payments.PaymentsPerCustomerMapper;
import services.loans_service.Loan;
import services.loans_service.payments.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoanMapper {
    Loan loan;
    LoanDTO loanDTO;

    public LoanMapper(LoanDTO loanDTO) {
        this.loan = null;
        this.loanDTO = loanDTO;
    }

    public LoanMapper(Loan loan) {
        this.loan = loan;
        this.loanDTO = null;
    }

    public LoanDTO mapToDTO() {

        Map<String, PaymentsPerCustomerDto> paymentsPerCustomerDTOsMap=new HashMap<>();
        List<String> lendersNames = null;
        this.loan.getLendersKeys().stream().forEach(lenderName -> {
            PaymentsPerCustomer paymentsPerCustomer = this.loan.getPaymentsByName(lenderName);

            List<PaymentDto> paymentDtoList;
            List<PaymentDto> riskPaymentDtoList;
            paymentDtoList = paymentsPerCustomer.getActivePaymentsList().stream().map(payment ->
                    new PaymentMapper(payment).mapToDTO(paymentsPerCustomer.getCapital()/paymentsPerCustomer.getNumOfPayments(), loan.getInterestPerPayment())).collect(Collectors.toList());

            riskPaymentDtoList = paymentsPerCustomer.getRiskPaymentsList().stream().map(payment ->
                    new PaymentMapper(payment).mapToDTO(paymentsPerCustomer.getCapital()/paymentsPerCustomer.getNumOfPayments(), loan.getInterestPerPayment())).collect(Collectors.toList());

            paymentsPerCustomerDTOsMap.put(lenderName, new PaymentsPerCustomerMapper(paymentsPerCustomer).mapToDTO(paymentDtoList,riskPaymentDtoList));
        });

        List<PaymentDto> paymentDtoListOwner;
        List<PaymentDto> riskPaymentDtoListOwner;
        paymentDtoListOwner = this.loan.getOwnerPayments().getActivePaymentsList().stream().map(payment ->
                new PaymentMapper(payment).mapToDTO(this.loan.getOwnerPayments().getCapital()/this.loan.getOwnerPayments().getNumOfPayments(), loan.getInterestPerPayment())).collect(Collectors.toList());

        riskPaymentDtoListOwner = this.loan.getOwnerPayments().getRiskPaymentsList().stream().map(payment ->
                new PaymentMapper(payment).mapToDTO(this.loan.getOwnerPayments().getCapital()/this.loan.getOwnerPayments().getNumOfPayments(), loan.getInterestPerPayment())).collect(Collectors.toList());

        PaymentsPerCustomerDto ownerPayments=new PaymentsPerCustomerMapper((this.loan.getOwnerPayments())).mapToDTO(paymentDtoListOwner,riskPaymentDtoListOwner);

        this.loanDTO=new LoanDTO(this.loan.getId(),this.loan.getOwnerName(), this.loan.getCategory().getName()
                , this.loan.getCapital(),this.loan.getTotalYaz(), this.loan.getInitalYaz(), this.loan.getFinalYaz(), this.loan.getStatus()
                , this.loan.getPaymentRate(), this.loan.getNextPaymentYaz(), this.loan.getInterestPerPayment(), paymentsPerCustomerDTOsMap
                , ownerPayments, lendersNames, loan.getAccumulatedByLenders(), loan.getCapital()-loan.getAccumulatedByLenders(), this.loan.totalPaidPaymentsAmount(), this.loan.totalLeftToPayAmount(), this.loan.capitalPaidPaymentsAmount(), this.loan.capitalLeftToPayAmount(), this.loan.interestPaidPaymentsAmount(), this.loan.interestLeftToPayAmount(),this.loan.nextPaymentsAmountSum());

        return this.loanDTO;
    }
}
