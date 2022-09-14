package gui.LoansTables;

import gui.Customers.CustomersController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import loans.LoanDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class buySellLoansController implements Initializable {
    public final static String BUY_LOAN_FXML_LOCATION="/gui/LoansTables/buy.fxml";
    private CustomersController customerController;

    @FXML
    private Tab buyLoansTab;

    @FXML
    private Tab sellLoansTab;

    @FXML
    private Tab sellTabTableContent;

    @FXML
    private Tab buyTabTableContent;

    @FXML
    private Button sumbitBuy;

    private buyLoansController buyLoansTable;
    private buyLoansController sellLoansTable;

    @FXML
    void submitBuyFunc(ActionEvent event) {
        List<LoanDTO> buyLoans;
        buyLoans=this.buyLoansTable.getSelectedLoans();

        if(!buyLoans.isEmpty()){
            this.customerController.buySellLoans(buyLoans.get(0).getId(),buyLoans.get(0).getLendersNames().get(0),false);
        }
    }

    @FXML
    void submitSellFunc(ActionEvent event) {
        List<LoanDTO> sellLoans;
        sellLoans=this.sellLoansTable.getSelectedLoans();

        if(!sellLoans.isEmpty()){
            this.customerController.buySellLoans(sellLoans.get(0).getId(),sellLoans.get(0).getLendersNames().get(0),true);
        }
    }


    public void setCustomerController(CustomersController customersController) {
        this.customerController=customersController;
    }

    public void initBuyTable(){//TODO SELECTION
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(BUY_LOAN_FXML_LOCATION));
            buyTabTableContent.setContent(loader.load());
            this.buyLoansTable=loader.getController();
            this.buyLoansTable.setBuySellController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initSellTable(){//TODO SELECTION
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(BUY_LOAN_FXML_LOCATION));
            sellTabTableContent.setContent(loader.load());
            this.sellLoansTable=loader.getController();

            this.sellLoansTable.setBuySellController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateData(List<LoanDTO> buyLoans, List<LoanDTO> sellLoans){
        this.buyLoansTable.setLoansValues(buyLoans);
        this.sellLoansTable.setLoansValues(sellLoans);


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBuyTable();
        initSellTable();
       // buyLoansTable.setMultiSelectionMode();
       // sellLoansTable.setMultiSelectionMode();
    }

    public void blockButton() {
        this.sumbitBuy.setDisable(true);
    }

    public void freeButton() {
        this.sumbitBuy.setDisable(false);
    }
}
