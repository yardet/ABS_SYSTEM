package services.loans_service;


import com.sun.xml.internal.bind.v2.TODO;
import loans.LoanDTO;
import loans.LoanMapper;
import services.categories_service.Category;
import services.customers_service.Customer;

import java.util.*;
import java.util.stream.Collectors;

public class LoansService {
    private  Map<String, Loan> loansMap;
    private Map<String, List<Loan>> buyLoans;

    public LoansService() {
        this.loansMap = new HashMap<>();
        this.buyLoans=new HashMap<>();
    }

    public LoansService(LoansService loansService) {
        this.loansMap = new HashMap<>();
        this.buyLoans=new HashMap<>();
        loansService.getAllLoansInfo().forEach(loan -> {
            Loan currLoan = new Loan(loan);
            this.loansMap.put(currLoan.getId(), currLoan);
        });
        loansService.buyLoans.forEach((key,value)->{
            List<Loan> loansToBuy=new ArrayList<>();
            value.forEach(loan->loansToBuy.add(new Loan(loan)));
        });
    }

    public void addLoan(String id, String owner, Category category, int capital, int total_yaz_time, int pays_every_yaz, int interest_per_payment)
    {
        Loan loan=new Loan(id, owner, category, capital, total_yaz_time,pays_every_yaz, interest_per_payment);
        this.loansMap.put(id,loan);
    }
    public List<Loan> getAllLoansInfo() {

        return this.loansMap.values().stream().collect(Collectors.toList());
    }

    public Loan findLoan(String id) {return this.loansMap.get(id);}

    public List<LoanDTO> filterLoansByOwner(String name) {
        return this.loansMap.values().stream().filter(l->l.getOwnerName().startsWith(name)).map(loan->new LoanMapper(loan).mapToDTO()).collect(Collectors.toList());}

    public List<LoanDTO> filterLoansByLenderName(String name){
        return this.loansMap.values().stream().filter(loan -> loan.getLendersKeys().contains(name)).map(loan -> new LoanMapper(loan).mapToDTO()).collect(Collectors.toList());}

    //buy\sell functions:
    public List<LoanDTO> getBuyLoansDto(String loginName) {
        List<LoanDTO> loans = new ArrayList<>();
        this.buyLoans.forEach((key,listLoans)->{
            if(!key.equals(loginName)){
                listLoans.forEach(loan -> loans.add(new LoanMapper(loan).mapToDTO()));
            }
        });
        return loans;
    }

    public Map<String, List<Loan>> getBuyLoans(){return buyLoans;}

    public void addLoanToSellList(String userName,String loandId) {
        Loan loan=findLoan(loandId);
        if(this.buyLoans.get(userName)==null){
            List<Loan> loanList=new ArrayList<>();
            this.buyLoans.put(userName,loanList);
        }
        else
        {
            this.buyLoans.get(userName).add(findLoan(loandId));
        }
    }

}
