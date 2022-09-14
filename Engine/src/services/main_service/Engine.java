package services.main_service;

import loans.LoanDTO;
import loans.LoanMapper;
import services.categories_service.CategoriesService;
import services.categories_service.Category;
import categories.CategoryDTO;
import services.customers_service.Customer;
import customers.CustomerDTO;
import customers.CustomerMapper;
import services.customers_service.CustomersService;
import services.loans_service.*;
import services.loans_service.payments.Payment;
import services.loans_service.payments.PaymentsPerCustomer;
import services.xml_parser_service.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Engine implements EngineInterface {
    private CustomersService customersService;
    private LoansService loansService;
    private CategoriesService categoriesService;
    private int YAZ;



    public Engine() {
        this.initEngine();
    }

    public Engine(Engine engine) {
        this.customersService = new CustomersService(engine.customersService);
        this.loansService = new LoansService(engine.loansService);
        this.categoriesService = new CategoriesService(engine.categoriesService);;
        this.YAZ=engine.YAZ;
    }

    //1
    @Override
    public void initEngine() {
        this.loansService = new LoansService();
        this.customersService = new CustomersService();
        this.categoriesService = new CategoriesService();
        this.YAZ = 1;
    }

    @Override
    public void initEngineFromXmlFile(String path,String nameFromLogin) throws JAXBException {
       // this.initEngine();
        XMLParser xmlParser = new XMLParser(path,nameFromLogin, this);
        xmlParser.fromXmlFileToObject();
    }

    @Override
    public void loadXmlFile(InputStream file, String nameFromLogin) throws JAXBException {
        //this.initEngine();
        XMLParser xmlParser = new XMLParser(file,nameFromLogin, this);
        xmlParser.fromXmlFileToObject();
    }

    public void addCustomer(String name, int balance) throws Exception{
        if(balance<0)
            throw new Exception("Balance is not positive - "+balance+" - "+name);
        if(this.customersService.getAllCustomersInfo().stream().filter(i->i.getName().equals(name)).collect(Collectors.toList()).isEmpty())
            this.customersService.addCustomer(name, balance);
        else
            throw new Exception("A customer already exists in the system- "+name);

    }

    public void createNewLoan(String id, String loginName, String category, int capital, float interest, int totalYazTime, int paymentRate){
        try {
            addLoan(id,loginName,category,capital,totalYazTime,paymentRate,(int)interest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLoan(String id, String ownerName, String category, int capital, int total_yaz_time, int pays_every_yaz, int interest_per_payment) throws Exception {
        if(this.customersService.getAllCustomersInfo().stream().filter(i->i.getName().equals(ownerName)).collect(Collectors.toList()).isEmpty())
        {
            //throw new Exception("The customer does not exist in the system- "+owner+" - "+id);//TODO- MAYBE DO SOME FUNCTIONALLY IN SERVER
            addCustomer(ownerName,0);
        }


        //valdation in the server,if all ok- return messge to the curr custoemr-TODO
        if(capital<= 0)
            throw new Exception("capital is not valid- "+capital+" - "+id);
        if(total_yaz_time<= 0)
            throw new Exception("total yaz time is not valid- "+total_yaz_time+" - "+id);
        if(pays_every_yaz<= 0)
            throw new Exception("Pyaments rate is not valid- "+pays_every_yaz+" - "+id);
        if(interest_per_payment<= 0)
            throw new Exception("interest is not valid- "+interest_per_payment+" - "+id);

        if(!this.loansService.getAllLoansInfo().stream().filter(i->i.getId().equals(id)).collect(Collectors.toList()).isEmpty())
            throw new Exception("A loan already exists in the system- "+id);

        if(this.getAllCategoriesInfo().stream().filter(i->i.getName().equals(category)).collect(Collectors.toList()).isEmpty()) {
            {
                addCategory(category);
                //throw new Exception("The loan's category not exists in the system- "+category+" - "+id);

            }
        }
        if(total_yaz_time%pays_every_yaz!=0)
            throw new Exception("The total yaz is not divided by the payments rate- "+id);
        Category category1 =  new Category(category);

        this.loansService.addLoan(id,ownerName, category1,capital, total_yaz_time,pays_every_yaz,interest_per_payment);

    }

    public void addCategory(String name) throws Exception {
        this.categoriesService.addCategories(name);
    }

    @Override
    public Set<CategoryDTO> getAllCategoriesInfo() {return this.categoriesService.getAllCategoriesInfo();}

    @Override
    public List<LoanDTO> getAllLoansInfo() {//2
        List<LoanDTO> loansDtoList=new ArrayList<>();
        this.loansService.getAllLoansInfo().forEach(loan -> loansDtoList.add(new LoanMapper(loan).mapToDTO()));
        return loansDtoList;
    }

    @Override
    public List<CustomerDTO> getAllCustomersInfo() {//3
        return this.customersService.getAllCustomersInfo().stream().map(customer ->
                new CustomerMapper(customer, this.loansService.filterLoansByLenderName(customer.getName())
                        , this.loansService.filterLoansByOwner(customer.getName())).mapToDTO()).collect(Collectors.toList());
    }

    @Override
    public float loadingWithdrawalMoney(String name, char type, int amount) throws Exception {//4-5
        if (type == '+')
            this.findCustomer(name).cashLoading(amount,this.getCurrentYaz());
        else//''-'
        {
            if (!this.findCustomer(name).thereIsEnoughInPassing(amount))
                throw new Exception("there is not enogh money");
            this.findCustomer(name).cashWithdrawal(amount, this.getCurrentYaz());
        }
        return this.findCustomer(name).getBalance();
    }

    @Override
    public void scheduling(Set<String> selectedLoansId,String customerName
            ,int amount,int percentLoan) throws Exception//6
    {
        if (!this.findCustomer(customerName).thereIsEnoughInPassing(amount))
            throw new Exception("there is not enogh money");

        //split-
        final int[] numOfLoans = {selectedLoansId.size()};
        final List<Integer>[] splitAmountList = new List[]{this.makePaymentsList(amount, numOfLoans[0])};
        final int[] raised = {0};
        selectedLoansId.forEach(id->{
            Loan loan=this.loansService.findLoan(id);
            Customer currCustomer= this.customersService.findCustomer(customerName);
            int loanCapital=loan.getCapital();
            int loanInvestmentLimit=Math.round(loan.getCapital()*(percentLoan/100));
            numOfLoans[0] -=1;
            if(splitAmountList[0].get(0)>loanInvestmentLimit) {
                loan.addLender(customerName, loanInvestmentLimit);
                currCustomer.addTranscation(this.YAZ, loan.getCapital(), '-');
                raised[0] +=loanInvestmentLimit;

                splitAmountList[0] =makePaymentsList((amount-raised[0]),numOfLoans[0]);
            }
            else {
                loan.addLender(customerName, splitAmountList[0].get(0));
                currCustomer.addTranscation(this.YAZ, splitAmountList[0].get(0), '-');
                raised[0] +=splitAmountList[0].get(0);
                splitAmountList[0].remove(0);
            }

            loan.updateStatus(this.YAZ);
            if(loan.getStatus()==Loan_status.active) {
                Customer owner = this.customersService.findCustomer(this.loansService.findLoan(id).getOwnerName());
                owner.addTranscation(YAZ, loan.getCapital(), '+');
            }

        });
    }
    @Override
    public void payPayments(String loanId, float amountFromGui, boolean isPayAllPayments) {
        Loan loan = this.loansService.findLoan(loanId);

        if (isPayAllPayments) {

            loan.getOwnerPayments().getActivePaymentsList().stream().filter(pay ->pay.isItDone()==false)
                    .forEach(payment->payPayments(payment.getLoanId(), payment.getAmount(),false));
        } else {
            //get owner data
            PaymentsPerCustomer ownerPayments = loan.getOwnerPayments();
            List<Payment> ownerPaymentsList = ownerPayments.getActivePaymentsList();

            //next payment
            Payment nextOwnerPayment=ownerPayments.getNextPayment();
            float nextOwnerPaymentAmount = nextOwnerPayment.getAmount();

            if (amountFromGui >= nextOwnerPaymentAmount)//full single amount
            {
                //add transaction to owner
                this.customersService.findCustomer(loan.getOwnerName()).addTranscation(this.YAZ, nextOwnerPaymentAmount, '-');
                //get lender data
                loan.getLendersKeys().forEach(name -> {
                    PaymentsPerCustomer lenderPaymentsPerCustomer = loan.getPaymentsByName(name);
                    Payment nextPaymentToLender = lenderPaymentsPerCustomer.getNextPayment();

                    //add transaction
                    this.customersService.findCustomer(name).addTranscation(this.YAZ, nextPaymentToLender.getAmount(), '+');

                    lenderPaymentsPerCustomer.updateNextPaymentIndex();
                    nextPaymentToLender.setDone(true);
                    nextPaymentToLender.updateYaz(this.YAZ);
                });
            } else if (amountFromGui < nextOwnerPaymentAmount)//part
            {
                //old paymemt:
                nextOwnerPayment.setAmount(amountFromGui);//old

                //add transaction to old
                this.customersService.findCustomer(loan.getOwnerName()).addTranscation(this.YAZ, nextOwnerPaymentAmount, '-');
                int oldPaymentCapital= (int) ((int)amountFromGui/((100+loan.getInterestPerPayment())/100));
                float oldPaymentInterest=amountFromGui-oldPaymentCapital;

                //add the new payment
                Payment newOwnerPayment=new Payment(loanId
                        ,nextOwnerPayment.getCapital()-oldPaymentCapital
                        ,nextOwnerPayment.getInterest()-oldPaymentInterest
                        ,nextOwnerPayment.getAmount()-amountFromGui,this.YAZ);
                ownerPaymentsList.add(ownerPayments.getNextPaymentIndex()+1,newOwnerPayment);//add to the next index

                //update the old
                nextOwnerPayment.setCapital(oldPaymentCapital);
                nextOwnerPayment.setInterest(oldPaymentInterest);
                nextOwnerPayment.setAmount(amountFromGui);

                //LENDERS:
                float splitForLender=amountFromGui/nextOwnerPaymentAmount;
                loan.getLendersKeys().forEach(name -> {
                    PaymentsPerCustomer lenderPaymentsPerCustomer = loan.getPaymentsByName(name);
                    Payment nextPaymentToLender = lenderPaymentsPerCustomer.getNextPayment();

                    //add transaction
                    int lenderOldPaymentAmount = (int) ((int) amountFromGui * splitForLender);
                    this.customersService.findCustomer(name).addTranscation(this.YAZ, lenderOldPaymentAmount, '+');

                    int lenderOldPaymentCapital = (int) ((int) lenderOldPaymentAmount / ((100 + loan.getInterestPerPayment()) / 100));
                    float lenderoldPaymentInterest = lenderOldPaymentAmount - oldPaymentCapital;

                    //add the new payment
                    Payment newlenderPayment=new Payment(loanId
                            ,nextPaymentToLender.getCapital()-lenderOldPaymentCapital
                            ,nextPaymentToLender.getInterest()-lenderoldPaymentInterest
                            ,nextPaymentToLender.getAmount()-lenderOldPaymentAmount,this.YAZ);
                    lenderPaymentsPerCustomer.getActivePaymentsList().add(ownerPayments.getNextPaymentIndex()+1,newOwnerPayment);

                    //update the old
                    nextPaymentToLender.setCapital(lenderOldPaymentCapital);
                    nextPaymentToLender.setInterest(lenderoldPaymentInterest);
                    nextPaymentToLender.setAmount(lenderOldPaymentAmount);

                    lenderPaymentsPerCustomer.updateNextPaymentIndex();
                    nextPaymentToLender.setDone(true);
                    nextPaymentToLender.updateYaz(this.YAZ);
                });
            }
            nextOwnerPayment.setDone(true);
            nextOwnerPayment.updateYaz(this.YAZ);
            ownerPayments.updateNextPaymentIndex();

            loan.updateStatus(this.YAZ);

        }
    }



    @Override
    public void promotingTimeline()
    {

        this.YAZ += 1;

        List<Loan> sortedLoans = this.filteringLoansforTimeLine();
        if (sortedLoans.isEmpty()) {
            return;
        }

        sortedLoans.stream().forEach(loan -> {

            //owner
            PaymentsPerCustomer ownerPayments = loan.getOwnerPayments();
            List<Payment> ownerPaymentsList = ownerPayments.getActivePaymentsList();


            //notifications
            List<Payment> paymentsForNotification=ownerPayments.getActivePaymentsList()
                    .stream().filter(payment -> payment.isItDone()==false)
                    .filter(payment -> payment.getYaz()==this.YAZ)
                    .collect(Collectors.toList());

            paymentsForNotification.stream().forEach(payment->{
                this.customersService.sendNotificationToCustomer(loan.getOwnerName(), payment.getLoanId(), payment.getYaz(), payment.getAmount());
                       // .addNotification(, loan);
                    });



            List<Payment> oldPayments=ownerPayments.getActivePaymentsList()
                    .stream().filter(payment -> payment.isItDone()==false)
                    .filter(payment -> payment.getYaz()<=this.YAZ)
                    .collect(Collectors.toList());
            if(oldPayments.isEmpty())
            {
                ownerPayments.getRiskPaymentsList().clear();
                //lender
                loan.getLendersKeys().forEach(name -> {
                    PaymentsPerCustomer lenderPaymentsPerCustomer = loan.getPaymentsByName(name);
                    lenderPaymentsPerCustomer.getRiskPaymentsList().clear();
                });
            }
            else
            {
                loan.RiskTreatment(oldPayments);
            }
            //next payment
            int nextPaymentIndex = ownerPayments.getNextPaymentIndex();
            Payment nextPayment = ownerPaymentsList.get(nextPaymentIndex);
            if(nextPayment.getYaz()==this.YAZ){/* notification*/}

            loan.getLendersKeys().forEach(name->{
                PaymentsPerCustomer lenderPaymentsPerCustomer=loan.getPaymentsByName(name);
                Payment lenderNextPayment=lenderPaymentsPerCustomer.getNextPayment();
                if(nextPayment.getYaz()==this.YAZ){/* notification*/}
            });
        loan.updateStatus(this.YAZ);
        });
    }
    @Override
    public List<LoanDTO> filteringLoansByParameters(float amount, List<String> categoriesNames, float minimumInterest
            , int minimumTotalYazTime, String customerName, int numOfOpenLoans)
    {//6

        List<LoanDTO> loanDtos=this.loansService.getAllLoansInfo().stream().filter(loan ->
                (loan.getStatus()==Loan_status.pending || loan.getStatus()==Loan_status.New)).filter(loan ->
                loan.getInterestPerPayment()>=minimumInterest).filter(loan ->loan.getTotalYaz()>=minimumTotalYazTime).filter(loan ->
                categoriesNames.contains(loan.getCategory().getName())).filter(loan ->
                !(loan.getOwnerName().equals(customerName))).filter(loan->getNumOfOpenLoansPerOwner(loan.getOwnerName())<=numOfOpenLoans).map(loan->
                new LoanMapper(loan).mapToDTO()).collect(Collectors.toList());
        return loanDtos;
    }

    private int getNumOfOpenLoansPerOwner(String name){
        return (int) this.loansService.filterLoansByOwner(name).stream().filter(loan-> loan.getStatus()!=Loan_status.finished).count();
    }

    @Override
    public int getCurrentYaz() {
        return this.YAZ;
    }

    @Override
    public boolean ThereIsEnoughInPassing(String customerName,float amount) {return this.findCustomer(customerName).thereIsEnoughInPassing(amount);}


    @Override
    public Customer findCustomer(String name) {
        return this.customersService.findCustomer(name);
    }

    private List<Loan> filteringLoansforTimeLine()//7
    {
        return this.loansService.getAllLoansInfo().stream().filter(loan ->
                        (loan.getStatus()==Loan_status.active) || loan.getStatus()==Loan_status.risk).sorted(Comparator.comparing(Loan::getInitalYaz)).sorted(Comparator.comparing(Loan::getCapital))
                .filter(loan ->

                        (loan.getOwnerPayments().getNextPayment().getYaz())<=this.YAZ).collect(Collectors.toList());
    }

    private List<Integer> makePaymentsList(int amount, int numOfLoans)//6
    {//split
        List<Integer> splitAmountList=new ArrayList<>();
        if(amount<=0 ||numOfLoans<=0)
            splitAmountList.add(0);
        else
        {

            int capitalPerPayment=(int)amount/numOfLoans;
            int capitalPerPaymentRest=amount%numOfLoans;

            for(int i=1;i<=numOfLoans;i++)
            {
                if((i)<=capitalPerPaymentRest)
                    splitAmountList.add(capitalPerPayment+1);
                else
                    splitAmountList.add(capitalPerPayment);
            }
        }
        return splitAmountList;
    }

    //buy\sell functions:
    @Override
    public List<LoanDTO> getBuyLoans(String loginName){
        return this.loansService.getBuyLoansDto(loginName);
    }

    @Override
    public void addLoanToSellList(String userName, String loandId) {
        this.loansService.addLoanToSellList(userName,loandId);
    }

        @Override
    public Exception buyLoan(String selectedLoans, String loginName, String lenderName) {
        Loan loan=this.loansService.findLoan(selectedLoans);
        Customer buyerCustomer = this.customersService.findCustomer(loginName);
        Customer lenderOfLoan=this.customersService.findCustomer(lenderName);

        if (buyerCustomer.thereIsEnoughInPassing(loan.capitalLeftToPayAmount())) {
            //remove loan from buy
            List<Loan> lenderBuyList = this.loansService.getBuyLoans().get(lenderName);
            if (lenderBuyList.size() == 1)
                this.loansService.getBuyLoans().remove(lenderName);
            else
                lenderBuyList.remove(this.loansService.getBuyLoans().get(lenderName).stream().filter(loan1 -> loan1.getId().equals(selectedLoans)).findFirst().get());

            buyerCustomer.addTranscation(this.YAZ, loan.capitalLeftToPayAmount(), '-');
            lenderOfLoan.addTranscation(this.YAZ, loan.capitalLeftToPayAmount(), '+');

            PaymentsPerCustomer olddLenderPayments = loan.getPaymentsByName(lenderName);
            loan.getLendersMap().put(loginName, olddLenderPayments);
            loan.getLendersMap().remove(lenderName);
        }
        else
        {
            return new Exception("there is no enogh money");
        }
            return null;
        }
}
