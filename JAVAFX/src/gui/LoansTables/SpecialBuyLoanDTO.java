package gui.LoansTables;


import javafx.scene.control.Button;
import loans.LoanDTO;
import loans.payments.PaymentsPerCustomerDto;
import services.loans_service.Loan_status;

import java.util.List;
import java.util.Map;

public class SpecialBuyLoanDTO extends LoanDTO {

    private String lenderName;

    SpecialBuyLoanDTO(LoanDTO loan,String lenderName) {
        super(loan.getId(),loan.getOwner(), loan.getCategory(),loan.getCapital(),loan.getTotalYaz(),loan.getInitalYaz(),loan.getFinalYaz(),loan.getStatus(),loan.getPaymentRate(),loan.getNextPaymentYaz(),loan.getInterestPerPayment(),loan.getLendersMap(),loan.getOwnerPayments(),loan.getLendersNames(),loan.getAccumulatedByLenders(),loan.getLeftToPayByLenders(),loan.getTotalPaidPaymentsAmount(),loan.getTotalAmountLeftToPay(),loan.getCapitalPaidPaymentAmount(),loan.getCapitalLeftToPay(),loan.getInterestPaidPaymentAmount(),loan.getInterestAmountLeftToPay(),loan.getNextPaymentAmountSum());
        this.lenderName=lenderName;
    }
}
