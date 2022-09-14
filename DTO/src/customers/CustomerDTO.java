package customers;

import customers.notifications.NotificationDTO;
import customers.transactions.TransactionDTO;
import loans.LoanDTO;
import services.loans_service.Loan_status;

import java.util.List;

public class CustomerDTO {
    private String name;
    private float balance;
    //private int currYaz;
    private List<TransactionDTO> transactions;
    private List<LoanDTO> lenderLoans;
    private List<LoanDTO> ownerLoans;
    private List<NotificationDTO> notifications;

    private int numOfOwnerLoansNew;
    private int numOfOwnerLoansPending;
    private int numOfOwnerLoansActive;
    private int numOfOwnerLoansRisk;
    private int numOfOwnerLoansFinished;

    private int numOfOLenderLoansNew;
    private int numOfLenderLoansPending;
    private int numOfLenderLoansActive;
    private int numOfLenderLoansRisk;
    private int numOfLenderLoansFinished;

    CustomerDTO(String name, float balance, List<TransactionDTO> transactions, List<LoanDTO> ownerLoans
            , List<LoanDTO> lenderLoans, List<NotificationDTO> notifications)
    {
        this.name=name;
        this.balance=balance;
        this.transactions=transactions;
        this.lenderLoans=lenderLoans;
        this.ownerLoans=ownerLoans;
        this.notifications=notifications;
    }

    public String getName() {
        return this.name;
    }

    public float getBalance() {
        return this.balance;
    }

    public List<TransactionDTO> getTransactions(){return this.transactions;}
    public List<NotificationDTO> getNotifications(){return this.notifications;}


    public List<LoanDTO> getLenderList(){return this.lenderLoans;}

    public List<LoanDTO> getOwnerList(){return this.ownerLoans;}

    public int getNumOfOwnerLoansNew() {
        return (int) ownerLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.New).count();
    }

    public int getNumOfOwnerLoansPending() {
        return (int) ownerLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.pending).count();
    }

    public int getNumOfOwnerLoansActive() {
        return (int) ownerLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.active).count();
    }

    public int getNumOfOwnerLoansRisk() {
        return (int) ownerLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.risk).count();
    }

    public int getNumOfOwnerLoansFinished() {
        return (int) ownerLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.finished).count();
    }

    public int getNumOfOLenderLoansNew() {
        return (int) lenderLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.New).count();
    }

    public int getNumOfLenderLoansPending() {
        return (int) lenderLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.pending).count();
    }

    public int getNumOfLenderLoansActive() {
        return (int) lenderLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.active).count();
    }

    public int getNumOfLenderLoansRisk() {
        return (int) lenderLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.risk).count();
    }

    public int getNumOfLenderLoansFinished() {
        return (int) lenderLoans.stream().filter(loanDTO -> loanDTO.getStatus()== Loan_status.finished).count();
    }
}

