package services.main_service;

import loans.LoanDTO;
import categories.CategoryDTO;
import services.customers_service.Customer;
import customers.CustomerDTO;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface EngineInterface {

    int getCurrentYaz();

    List<LoanDTO> getAllLoansInfo();

    List<CustomerDTO> getAllCustomersInfo();

    Set<CategoryDTO> getAllCategoriesInfo();

    Customer findCustomer(String name);

    void initEngine();

    void initEngineFromXmlFile(String path,String nameFromLogin) throws JAXBException;

    void loadXmlFile(InputStream file, String nameFromLogin) throws JAXBException ;

    List<LoanDTO> filteringLoansByParameters(float amount, List<String> categoriesNames
            , float minimumInterest, int minimumTotalYazTime, String customerName, int numOfOpenLoans);

    void scheduling(Set<String> selectedLoansId,String customerName,int amount,int percentLoan) throws Exception;

    void promotingTimeline();

    float loadingWithdrawalMoney(String name, char type, int amount) throws Exception;

    boolean ThereIsEnoughInPassing(String customerName,float amount);

    void payPayments(String loanId, float amount, boolean isPayAllPayments);

    void createNewLoan(String id, String loginName, String category, int capital, float interest, int totalYazTime, int paymentRate);

    List<LoanDTO> getBuyLoans(String loginName);

    void addLoanToSellList(String userName, String loandId);

    Exception buyLoan(String selectedLoans, String loginName, String lenderName);
}