package loans.payments;

import services.loans_service.payments.PaymentsPerCustomer;

import java.util.List;

public class PaymentsPerCustomerMapper {
    PaymentsPerCustomer paymentsPerCustomer;
    PaymentsPerCustomerDto paymentsPerCustomerDto;

    public PaymentsPerCustomerMapper(PaymentsPerCustomerDto paymentsPerCustomerDto) {
        this.paymentsPerCustomer = null;
        this.paymentsPerCustomerDto = paymentsPerCustomerDto;
    }

    public PaymentsPerCustomerMapper(PaymentsPerCustomer paymentsPerCustomer) {
        this.paymentsPerCustomer=paymentsPerCustomer;
        this.paymentsPerCustomerDto = null;
    }

    public PaymentsPerCustomerDto mapToDTO(List<PaymentDto> activePaymentsList,List<PaymentDto> riskPaymentsList) {
        this.paymentsPerCustomerDto = new PaymentsPerCustomerDto(this.paymentsPerCustomer.getCapital(),this.paymentsPerCustomer.getNextPaymentIndex(),activePaymentsList,riskPaymentsList);
        return this.paymentsPerCustomerDto;
    }
}
