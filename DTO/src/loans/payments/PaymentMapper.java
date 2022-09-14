package loans.payments;

import services.loans_service.payments.Payment;

public class PaymentMapper {
    Payment payment;
    PaymentDto paymentDto;

    public PaymentMapper(PaymentDto paymentDto) {
        this.payment = null;
        this.paymentDto = paymentDto;
    }

    public PaymentMapper(Payment payment) {
        this.payment = payment;
        this.paymentDto = null;
    }

    public PaymentDto mapToDTO(int capital,float interest) {
        this.paymentDto = new PaymentDto(payment.getLoanId(), payment.getCapital(),payment.getInterest(),payment.getYaz(),payment.isItDone());
        return this.paymentDto;
    }
}
