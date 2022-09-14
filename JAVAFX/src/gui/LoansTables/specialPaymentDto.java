package gui.LoansTables;


import javafx.scene.control.Button;
import loans.payments.PaymentDto;

public class specialPaymentDto extends PaymentDto {

    private Button pay;

    public specialPaymentDto(PaymentDto payment){
        super(payment.getLoanId(), payment.getCapital(), payment.getInterest(), payment.getYaz(), payment.isItDone());
        pay=new Button();
    }

    public Button getPay() {
        return pay;
    }
}
