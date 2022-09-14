package customers.transactions;


import services.customers_service.transactions.Transaction;

public class TransactionsMapper {
    Transaction transaction;
    TransactionDTO transactionDTO;

    public TransactionsMapper(TransactionDTO transactionDTO) {
        this.transaction = null;
        this.transactionDTO = transactionDTO;
    }

    public TransactionsMapper(Transaction transaction) {
        this.transaction = transaction;
        this.transactionDTO = null;
    }

    public TransactionDTO mapToDTO() {
        this.transactionDTO = new TransactionDTO(this.transaction.getYaz(), this.transaction.getAmount()
                , this.transaction.getType(), this.transaction.getOldAcountBalance(), this.transaction.getNewAccountBalance());
        return this.transactionDTO;
    }
}
