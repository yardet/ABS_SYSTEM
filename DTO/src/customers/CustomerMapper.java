package customers;

import customers.notifications.NotificationDTO;
import customers.notifications.NotificationMapper;
import services.customers_service.Customer;
import customers.transactions.TransactionDTO;
import customers.transactions.TransactionsMapper;
import loans.LoanDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {
    Customer customer;
    CustomerDTO customerDTO;

    private List<LoanDTO> loansAsLender;
    private List<LoanDTO> loansAsOwner;


    public CustomerMapper(CustomerDTO customerDTO) {
        this.customer = null;
        this.customerDTO = customerDTO;
        this.loansAsLender = null;
        this.loansAsOwner = null;
    }

    public CustomerMapper(Customer customer, List<LoanDTO> loansAsLender, List<LoanDTO> loansAsOwner) {
        this.customer = customer;
        this.customerDTO = null;
        this.loansAsLender=loansAsLender;
        this.loansAsOwner= loansAsOwner;
    }

    public CustomerDTO mapToDTO() {
        List<TransactionDTO> transactionDTOList;
        transactionDTOList = this.customer.getAllTransactionsInfo().stream().map(transaction ->
                new TransactionsMapper(transaction).mapToDTO()).collect(Collectors.toList());

        List<NotificationDTO> notificationsDTOList;
        notificationsDTOList = this.customer.getAllNotifications().stream().map(notification ->
                new NotificationMapper(notification).mapToDTO()).collect(Collectors.toList());


        this.customerDTO = new CustomerDTO(this.customer.getName(), this.customer.getBalance(),
                transactionDTOList,this.loansAsOwner,this.loansAsLender, notificationsDTOList);
        return this.customerDTO;
    }
}
