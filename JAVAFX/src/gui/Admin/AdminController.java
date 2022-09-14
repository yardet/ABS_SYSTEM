package gui.Admin;

import admin.components.login.LoginController;
import admin.components.main.mainController;
import gui.LoansTables.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import customers.CustomerDTO;
import loans.LoanDTO;
import services.loans_service.Loan_status;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminController implements Initializable {
    private Stage primaryStage;
    private BorderPane layout;

    private admin.components.main.mainController mainController;
    private LoansNewController loansNewController;
    private LoansPendingController loansPendingController;
    private LoansActiveController loansActiveController;
    private LoansRiskController loansRiskController;
    private LoansFinishedController loansFinishedController;
    private LoginController loginController;

    private String loginName="ADMIN";
    Map<Loan_status,LoansNewController> controllers=new HashMap<>();
    Map<Loan_status, String> fxmlLoansPaths=new HashMap<>();
    Map<Loan_status,Tab> tabsMap=new HashMap<>();

    @FXML
    private Label mainNameL;

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gridCustomers;

    @FXML
    private GridPane gridLoans;

    @FXML
    private GridPane mainGrid;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TabPane tabPaneLoans;

    @FXML
    private Tab loanActiveTab;

    @FXML
    private Tab loanFinishedTab;

    @FXML
    private Tab loanNewTab;

    @FXML
    private Tab loanPendingTab;

    @FXML
    private Tab loanRiskyTab;

    @FXML
    private Button increaseYazB;

    @FXML
    private Button decreaseYazB;


    @FXML
    private TableView<CustomerDTO> customersTable;

    @FXML
    private TableColumn<CustomerDTO, Float> customerBalance;

    @FXML
    private TableColumn<CustomerDTO, String> customerName;


    @FXML
    private TableColumn<CustomerDTO, Integer> customerLenderActive;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerLenderRisk;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerLenderFinished;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerLenderNew;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerLenderPending;


    @FXML
    private TableColumn<CustomerDTO, Integer> customerOwnerActive;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerOwnerFinished;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerOwnerNew;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerOwnerPending;

    @FXML
    private TableColumn<CustomerDTO, Integer> customerOwnerRisk;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initAlldata();


    }

    public void initAlldata() {
        mainNameL.setText("Hello "+loginName);
        initalCustomerTable();
        initalLoansTable();
    }

    private FXMLLoader initLoanTable(String fileName,Tab loanTab){
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(fileName));//FXML PATH
            loanTab.setContent(loader.load());

        } catch (IOException e) {
            System.out.println("File not found");
        }
        return loader;
    }

    private void initalLoansTable(){

        fxmlLoansPaths.put(Loan_status.New, "/gui/LoansTables/loansNew.fxml");
        fxmlLoansPaths.put(Loan_status.pending, "/gui/LoansTables/loansPending.fxml");
        fxmlLoansPaths.put(Loan_status.active, "/gui/LoansTables/loansActive.fxml");
        fxmlLoansPaths.put(Loan_status.risk, "/gui/LoansTables/loansRisky.fxml");
        fxmlLoansPaths.put(Loan_status.finished, "/gui/LoansTables/loansFinished.fxml");

        tabsMap.put(Loan_status.New,loanNewTab);
        tabsMap.put(Loan_status.pending,loanPendingTab);
        tabsMap.put(Loan_status.active,loanActiveTab);
        tabsMap.put(Loan_status.risk,loanRiskyTab);
        tabsMap.put(Loan_status.finished,loanFinishedTab);

        this.loansNewController=initLoanTable(fxmlLoansPaths.get(Loan_status.New),loanNewTab).getController();
        this.loansNewController.setAdminController(this);

        this.loansPendingController=initLoanTable(fxmlLoansPaths.get(Loan_status.pending),loanPendingTab).getController();
        this.loansPendingController.setAdminController(this);

        this.loansActiveController=initLoanTable(fxmlLoansPaths.get(Loan_status.active),loanActiveTab).getController();
        this.loansActiveController.setAdminController(this);

        this.loansRiskController=initLoanTable(fxmlLoansPaths.get(Loan_status.risk),loanRiskyTab).getController();
        this.loansRiskController.setAdminController(this);

        this.loansFinishedController=initLoanTable(fxmlLoansPaths.get(Loan_status.finished),loanFinishedTab).getController();
        this.loansFinishedController.setAdminController(this);

        controllers.put(Loan_status.New,this.loansNewController);
        controllers.put(Loan_status.pending,this.loansPendingController);
        controllers.put(Loan_status.active,this.loansActiveController);
        controllers.put(Loan_status.risk,this.loansRiskController);
        controllers.put(Loan_status.finished,this.loansFinishedController);


    }

    private void initalCustomerTable(){

        customerName.setCellValueFactory(new PropertyValueFactory<CustomerDTO,String>("Name"));
        customerBalance.setCellValueFactory(new PropertyValueFactory<CustomerDTO,Float>("Balance"));

        customerOwnerNew.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOwnerLoansNew"));
        customerOwnerPending.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOwnerLoansPending"));
        customerOwnerActive.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOwnerLoansActive"));
        customerOwnerRisk.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOwnerLoansRisk"));
        customerOwnerFinished.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOwnerLoansFinished"));

        customerLenderNew.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfOLenderLoansNew"));
        customerLenderPending.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfLenderLoansPending"));
        customerLenderActive.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfLenderLoansActive"));
        customerLenderRisk.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfLenderLoansRisk"));
        customerLenderFinished.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Integer>("numOfLenderLoansFinished"));

    }

    public void updateLoansTables(List<LoanDTO> loans) {
        if (loans != null) {

            controllers.forEach((status, controller) -> {

                controller.setLoansValues(loans.stream().filter(loanDTO -> loanDTO.getStatus().equals(status)).collect(Collectors.toList()));

            });
        }
    }

    public void updateAdminData(){
        //this.mainController.updateCurrentYaz();
        updateLoansTables(this.mainController.getAllLoansInfo());
        setCustomersValues(this.mainController.getAllCustomersInfo());
    }

    @FXML
    void increaseYazAction(ActionEvent event) {

        this.mainController.increaseYaz();
        //updateAdminData();
    }

    @FXML
    void decreaseYazAction(ActionEvent event) {
        this.mainController.decreaseYaz();
        //updateAdminData();

    }

    public void setSkin(int numSkin){
        switch (numSkin){
            case 2:
                this.gridCustomers.getStyleClass().setAll("skin3");
                this.gridLoans.getStyleClass().setAll("skin3");
                this.mainGrid.getStyleClass().setAll("skin4");
                this.borderPane.getStyleClass().setAll("skin6");


                break;
            case 3:
                this.gridCustomers.getStyleClass().setAll("skin2");
                this.gridLoans.getStyleClass().setAll("skin2");
                this.mainGrid.getStyleClass().setAll("skin5");
                this.borderPane.getStyleClass().setAll("skin1");
                break;
            default:
                this.gridCustomers.getStyleClass().setAll("skin1");
                this.gridLoans.getStyleClass().setAll("skin1");
                this.mainGrid.getStyleClass().setAll("skin6");
                this.borderPane.getStyleClass().setAll("skin4");
                break;
        }
    }
    public void setMainController(mainController mainController){this.mainController=mainController;}

    private void setCustomersValues(List<CustomerDTO>customerDTOS){
        if(customerDTOS!=null)
        {
            ObservableList<CustomerDTO> customerDTOObservableList =FXCollections.observableList(customerDTOS);
            customersTable.getItems().setAll(FXCollections.observableList(customerDTOObservableList));
        }

    }

    public void setLayout(BorderPane layout){this.layout=layout;}

    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String name){this.loginName=name;}

    public void freeButton() {
        decreaseYazB.setDisable(false);
    }

    public void blockButton() {
        decreaseYazB.setDisable(true);

    }
}
