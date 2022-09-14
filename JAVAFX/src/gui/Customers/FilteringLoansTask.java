package gui.Customers;

import javafx.beans.property.FloatProperty;
import javafx.concurrent.Task;
import loans.LoanDTO;
import services.main_service.EngineInterface;

import java.util.ArrayList;
import java.util.List;

public class FilteringLoansTask extends Task<List<LoanDTO>> {
    private final EngineInterface api;
    private  final FloatProperty progress;
    private final float amount;
    private final List<String> categoriesNames;
    private final float minimumInterest;
    private final int minimumTotalYazTime;
    private final String customerName;
    private final int numOfOpenLoans;

    public FilteringLoansTask(EngineInterface api, FloatProperty progress, float amount, List<String> categoriesNames, float minimumInterest
            , int minimumTotalYazTime, String customerName, int numOfOpenLoans) {
        this.api = api;
        this.progress = progress;
        this.amount = amount;
        this.categoriesNames = categoriesNames;
        this.minimumInterest = minimumInterest;
        this.minimumTotalYazTime = minimumTotalYazTime;
        this.customerName = customerName;
        this.numOfOpenLoans = numOfOpenLoans;
    }

    @Override
    protected List<LoanDTO> call() throws Exception {
        try {


            int index = 1;
            List<LoanDTO> filteredLoansFromApi = this.api.filteringLoansByParameters(amount
                    , categoriesNames, minimumInterest, minimumTotalYazTime, customerName, numOfOpenLoans);
            int count = filteredLoansFromApi.size();
            long timeToSleep = 6000 / count;
            while (index <= count) {
                Thread.sleep(timeToSleep);
                updateProgress(index, count);
                updateMessage("Loan Found: " + filteredLoansFromApi.get(index - 1));
                index++;
            }
            Thread.sleep(2000);
            updateProgress(2,2);
            return filteredLoansFromApi;

        }catch (Exception e) {
            updateProgress(1, 2);
            Thread.sleep(2000);
            updateProgress(2,2);
            return new ArrayList<>();

        }

    }
}
