package gui.LoansTables;


import javafx.scene.control.Button;
import loans.LoanDTO;

public class SpecialLoanDTO extends LoanDTO {

    private Button lenderButton;
    private Button activeButton;
    private Button riskButton;

   SpecialLoanDTO(LoanDTO loan) {
        super(loan.getId(),loan.getOwner(), loan.getCategory(),loan.getCapital(),loan.getTotalYaz(),loan.getInitalYaz(),loan.getFinalYaz(),loan.getStatus(),loan.getPaymentRate(),loan.getNextPaymentYaz(),loan.getInterestPerPayment(),loan.getLendersMap(),loan.getOwnerPayments(),loan.getLendersNames(),loan.getAccumulatedByLenders(),loan.getLeftToPayByLenders(),loan.getTotalPaidPaymentsAmount(),loan.getTotalAmountLeftToPay(),loan.getCapitalPaidPaymentAmount(),loan.getCapitalLeftToPay(),loan.getInterestPaidPaymentAmount(),loan.getInterestAmountLeftToPay(),loan.getNextPaymentAmountSum());
       lenderButton=new Button();
       activeButton=new Button();
       riskButton=new Button();
   }

    public Button getLenderButton() {
        return lenderButton;
    }

    public Button getActiveButton() {
        return activeButton;
    }

    public Button getRiskButton() {
        return riskButton;
    }
}
