package gui.Customers;

import categories.CategoryDTO;
import gui.LoansTables.*;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import customers.CustomerDTO;
import services.customers_service.notification.Notification;
import customers.notifications.NotificationDTO;
import customers.transactions.TransactionDTO;
import loans.LoanDTO;
import services.loans_service.Loan_status;
import loans.payments.PaymentDto;
import user.components.main.mainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static user.util.Constants.BUY_SELL_FXML_LOCATION;
import static user.util.Constants.NEW_LOAN_FXML_LOCATION;

public class CustomersController implements Initializable {

    private Stage primaryStage;
    private user.components.main.mainController mainController;
    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private buySellLoansController buySellLoansController;
    private newLoanController newLoanController;
    private LoansNewController loansNewController1;
    private LoansPendingController loansPendingController1;
    private LoansActiveController loansActiveController1;
    private LoansRiskController loansRiskController1;
    private LoansFinishedController loansFinishedController1;

    private LoansNewController loansNewController2;
    private LoansPendingController loansPendingController2;
    private LoansActiveController loansActiveController2;
    private LoansRiskController loansRiskController2;
    private LoansFinishedController loansFinishedController2;

    private LoansNewController loansNewController3;
    private LoansPendingController loansPendingController3;
    private LoansActiveController loansActiveController3;
    private LoansRiskController loansRiskController3;
    private LoansFinishedController loansFinishedController3;

    private LoansPendingController filteredLoansController;
    private PaymentsFutureViewController paymentsFutureViewController;

    Map<Loan_status,LoansNewController> controllers1=new HashMap<>();// by status
    Map<Loan_status,LoansNewController> controllers2=new HashMap<>();// by status
    Map<Loan_status,LoansNewController> controllers3=new HashMap<>();// by status
    Map<Integer,Map<Loan_status,LoansNewController>> controllersTables=new HashMap<>();//by table


    private String loginName;
    Map<Loan_status, String> fxmlLoansPaths=new HashMap<>();
    Map<Loan_status,Tab> tabsMap=new HashMap<>();

    CustomerDTO customer;

    //css
    @FXML
    private BorderPane mainBorderPain;

    @FXML
    private AnchorPane acnhorInformation;

    @FXML
    private AnchorPane acnhorLeftSplitInformation;

    @FXML
    private AnchorPane acnhorRightSplitInformation;

    @FXML
    private GridPane lednerLoansGrid;

    @FXML
    private GridPane loanerLoansGrid;

    @FXML
    private GridPane transactionGrid;

    @FXML
    private GridPane scrambleLeftGrid;

    @FXML
    private AnchorPane rightScrambleAnchor;

    @FXML
    private GridPane rightScrambleGrid;

    @FXML
    private AnchorPane leftPaymentsAnchor;


    @FXML
    private GridPane loanerTableGrid;

    @FXML
    private AnchorPane rightPaymentsAnchor;

    @FXML
    private TabPane tabPane;

    @FXML
    private GridPane notoficationsGrid;


    //INFROMATION TAB:

    @FXML
    private Tab informationTab;

    @FXML
    private TabPane tabPaneLender;

    @FXML
    private TabPane tabPaneLoansLoaner;

    @FXML
    private Tab loanActiveTabLender;

    @FXML
    private Tab loanActiveTabLoaner;

    @FXML
    private Tab loanFinishedTabLender;

    @FXML
    private Tab loanFinishedTabLoaner;

    @FXML
    private Tab loanNewTabLender;

    @FXML
    private Tab loanNewTabLoaner;

    @FXML
    private Tab loanPendingTabLender;

    @FXML
    private Tab loanPendingTabLoaner;

    @FXML
    private Tab loanRiskyTabLender;

    @FXML
    private Tab loanRiskyTabLoaner;


    //TRANSACTIONS:
    @FXML
    private Button withdrawMoneyB;

    @FXML
    private Button chargeMoneyB;

    @FXML
    private TableColumn<TransactionDTO, Float> transactionsAmount;

    @FXML
    private TableColumn<TransactionDTO, Float> transactionsAfter;

    @FXML
    private TableColumn<TransactionDTO, Float> transactionsBefore;

    @FXML
    private TableView<TransactionDTO> transactionsTable;

    @FXML
    private TableColumn<TransactionDTO, Character> transactionsType;

    @FXML
    private TableColumn<TransactionDTO, Integer> transactionsYaz;


    //SCRAMBLE tab:

    @FXML
    private BorderPane borderPaneAnimation;

    @FXML
    private Tab filteredLoansTab;

    @FXML
    private TextField amountTextBox;

    final ListView<String> selectedItems = new ListView<>();

    List<String> categoriesNames;

    @FXML
    private MenuButton categoriesSelection;

    @FXML
    private CheckBox maximumOpenCheckBox;

    @FXML
    private TextField maximumOpenText;

    @FXML
    private CheckBox maximumPercentCheckBox;

    @FXML
    private TextField maximumPercentText;

    @FXML
    private CheckBox minimumDurationCheckBox;

    @FXML
    private TextField minimumDurationText;

    @FXML
    private CheckBox minimumInterestCheckBox;

    @FXML
    private TextField minimumInterestText;

    @FXML
    private ProgressBar progressScroll;

    @FXML
    private Tab startScramble;

    @FXML
    private Button submitFilterB;

    @FXML
    private Button submitScambleB;

    @FXML
    private Label notEnoghMoneyL;

    private int maximumOpenLoansDefault=5;

    //animation
    @FXML
    private Tab animationTab;

    private animation2Controller animation2Controller;
    private boolean initAnimationDone=false;


    //PAYMENTS tab:
    @FXML
    private Tab paymentsTab;

    @FXML
    private TabPane tabPanePayments;

    @FXML
    private TableView<NotificationDTO> notoficationsPayments;

    @FXML
    private Tab loanActiveTabPayments;

    @FXML
    private Tab loanFinishedTabPayments;

    @FXML
    private Tab loanNewTabPayments;

    @FXML
    private Tab loanPendingTabPayments;

    @FXML
    private Tab loanRiskyTabPayments;

    @FXML
    private Tab futureTabPayments;

    @FXML
    private TableColumn<NotificationDTO, Float> notificationAmount;

    @FXML
    private TableColumn<NotificationDTO, String> notificationName;

    @FXML
    private TableColumn<Notification, Integer> notificationYaz;


    //NEW LOAN TAB
    @FXML
    private Tab newLoanTab;
    //SELL/BUY TAB
    @FXML
    private Tab sellBuyTab;
    private boolean categoriesIsLoaded=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.primaryStage=this.mainController.getPrimaryStage();

        initAllData();



    }

    public void initAllData() {
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);

        notEnoghMoneyL.setVisible(false);
        initNotificationsTable();
        initalTransactionsTable();
        initalLoansTable();
        initBuySellFxml();
        makeValidTextBoxes();

        //initilize visual stramble controllers
        initScramble();
    }

    public void initBuySellFxml(){
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(BUY_SELL_FXML_LOCATION));
            sellBuyTab.setContent(loader.load());
            buySellLoansController=loader.getController();
            buySellLoansController.setCustomerController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initNewLoanFxml(){
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(NEW_LOAN_FXML_LOCATION));
            GridPane gridPane=loader.load();
            newLoanTab.setContent(gridPane);
            newLoanController =loader.getController();
            newLoanController.setCustomerController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initNotificationsTable() {
        notificationAmount.setCellValueFactory(new PropertyValueFactory<>("finalAmount"));
        notificationYaz.setCellValueFactory(new PropertyValueFactory<>("yaz"));
        notificationName.setCellValueFactory(new PropertyValueFactory<>("loanName"));
    }

    private void initScramble() {
        submitScambleB.setDisable(true);
        notEnoghMoneyL.setVisible(false);

        amountTextBox.setText("");

        maximumPercentCheckBox.setSelected(false);
        maximumPercentText.setText("100");
        maximumPercentText.setDisable(true);

        minimumInterestCheckBox.setSelected(false);
        minimumInterestText.setText("0");
        minimumInterestText.setDisable(true);

        minimumDurationCheckBox.setSelected(false);
        minimumDurationText.setText("1");
        minimumDurationText.setDisable(true);

        maximumOpenCheckBox.setSelected(false);
        maximumOpenText.setText(String.valueOf(this.maximumOpenLoansDefault));
        maximumOpenText.setDisable(true);

       // setCategoriesNames();

    }

    private void initAnimation(boolean withTimer) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/LoansTables/animation2.fxml"));
        BorderPane animationLayout=loader.load();

        Scene scene = new Scene(animationLayout);
        Stage stage=new Stage();
        stage.setScene(scene);
        this.animation2Controller=loader.getController();
        this.animation2Controller.setStage(stage);
        if(withTimer)
            this.animation2Controller.setAnimation(2,true);//2 seconds
        else
            this.animation2Controller.setAnimation(2,false);//2 seconds
    }

    private FXMLLoader initLoanTable(String fileName, Tab loanTab){
        FXMLLoader loader=new FXMLLoader();
        try{
            loader.setLocation(getClass().getResource(fileName));
            loanTab.setContent(loader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    private void initalLoansTable(){

        fxmlLoansPaths.put(Loan_status.New,"/gui/LoansTables/loansNew.fxml");
        fxmlLoansPaths.put(Loan_status.pending,"/gui/LoansTables/loansPending.fxml");
        fxmlLoansPaths.put(Loan_status.active,"/gui/LoansTables/loansActive.fxml");
        fxmlLoansPaths.put(Loan_status.risk,"/gui/LoansTables/loansRisky.fxml");
        fxmlLoansPaths.put(Loan_status.finished,"/gui/LoansTables/loansFinished.fxml");

        this.loansNewController1=initLoanTable(fxmlLoansPaths.get(Loan_status.New),loanNewTabLoaner).getController();
        this.loansPendingController1=initLoanTable(fxmlLoansPaths.get(Loan_status.pending),loanPendingTabLoaner).getController();
        this.loansActiveController1=initLoanTable(fxmlLoansPaths.get(Loan_status.active),loanActiveTabLoaner).getController();
        this.loansRiskController1=initLoanTable(fxmlLoansPaths.get(Loan_status.risk),loanRiskyTabLoaner).getController();
        this.loansFinishedController1=initLoanTable(fxmlLoansPaths.get(Loan_status.finished),loanFinishedTabLoaner).getController();

        this.loansNewController3=initLoanTable(fxmlLoansPaths.get(Loan_status.New),loanNewTabPayments).getController();
        this.loansPendingController3=initLoanTable(fxmlLoansPaths.get(Loan_status.pending),loanPendingTabPayments).getController();
        this.loansActiveController3=initLoanTable(fxmlLoansPaths.get(Loan_status.active),loanActiveTabPayments).getController();
        this.loansRiskController3=initLoanTable(fxmlLoansPaths.get(Loan_status.risk),loanRiskyTabPayments).getController();
        this.loansFinishedController3=initLoanTable(fxmlLoansPaths.get(Loan_status.finished),loanFinishedTabPayments).getController();

        this.loansNewController2=initLoanTable(fxmlLoansPaths.get(Loan_status.New),loanNewTabLender).getController();
        this.loansPendingController2=initLoanTable(fxmlLoansPaths.get(Loan_status.pending),loanPendingTabLender).getController();
        this.loansActiveController2=initLoanTable(fxmlLoansPaths.get(Loan_status.active),loanActiveTabLender).getController();
        this.loansRiskController2=initLoanTable(fxmlLoansPaths.get(Loan_status.risk),loanRiskyTabLender).getController();
        this.loansFinishedController2=initLoanTable(fxmlLoansPaths.get(Loan_status.finished),loanFinishedTabLender).getController();

        this.paymentsFutureViewController=initLoanTable("/gui/LoansTables/paymentsFutureView.fxml",futureTabPayments).getController();
        this.paymentsFutureViewController.setCustomerController(this);
        this.filteredLoansController=initLoanTable(fxmlLoansPaths.get(Loan_status.pending),filteredLoansTab).getController();
        this.filteredLoansController.setMultiSelectionMode();

        controllers1.put(Loan_status.New,this.loansNewController1);
        controllers1.put(Loan_status.pending,this.loansPendingController1);
        controllers1.put(Loan_status.active,this.loansActiveController1);
        controllers1.put(Loan_status.risk,this.loansRiskController1);
        controllers1.put(Loan_status.finished,this.loansFinishedController1);

        controllers2.put(Loan_status.New,this.loansNewController2);
        controllers2.put(Loan_status.pending,this.loansPendingController2);
        controllers2.put(Loan_status.active,this.loansActiveController2);
        controllers2.put(Loan_status.risk,this.loansRiskController2);
        controllers2.put(Loan_status.finished,this.loansFinishedController2);

        controllers3.put(Loan_status.New,this.loansNewController3);
        controllers3.put(Loan_status.pending,this.loansPendingController3);
        controllers3.put(Loan_status.active,this.loansActiveController3);
        controllers3.put(Loan_status.risk,this.loansRiskController3);
        controllers3.put(Loan_status.finished,this.loansFinishedController3);

    }

    private void initalTransactionsTable(){
        transactionsYaz.setCellValueFactory(new PropertyValueFactory<TransactionDTO,Integer>("yaz"));
        transactionsAmount.setCellValueFactory(new PropertyValueFactory<TransactionDTO,Float>("amount"));
        transactionsType.setCellValueFactory(new PropertyValueFactory<TransactionDTO,Character>("type"));
        transactionsBefore.setCellValueFactory(new PropertyValueFactory<TransactionDTO,Float>("oldAcountBalance"));
        transactionsAfter.setCellValueFactory(new PropertyValueFactory<TransactionDTO,Float>("newAcountBalance"));
    }
    //load file
    public void loadFileButtonAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        this.primaryStage=this.mainController.getPrimaryStage();

        String currDir = Paths.get("").toAbsolutePath().toString();
        //fileChooser.setInitialDirectory(new File(currDir+"\\Engine\\src\\resources"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }
        // this.mainController.setFileNotLoadedL(false);
        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);

        isFileSelected.set(true);
        try{
            this.mainController.setFileEngine(absolutePath,selectedFile);
            //this.mainController.setFilePathLabel("XML file:"+absolutePath);
            //this.mainController.setYazLabel(this.mainController.getCurrentYaz());
            updateInformationData();
            //this.mainController.updateCategories();
            setCategoriesNames();
            this.newLoanController.setDisableFalse();
        } catch (Exception e) {
            // this.mainController.setFilePathLabel(e.getMessage());
        }
    }

    //Updates
    public void updateInformationData() throws IOException {
        //updateCurrentYaz();

        this.customer=this.mainController.getCustomersInfo();
        //setCustomer(this.loginName);
        if(!categoriesIsLoaded) {
            setCategoriesNames();
            categoriesIsLoaded=true;
        }
        setTransactionValues(this.customer.getTransactions());
        setNotificationValues(this.customer.getNotifications());

        updateLoansTables();


        //init payments
        final float[] sum = {Float.parseFloat("0.0")};
        List<Float> nextPaymentsSumList=new ArrayList<>();

        if(this.customer!=null && this.customer.getOwnerList()!=null)
        {
            this.customer.getOwnerList().forEach(loanDTO -> {
                nextPaymentsSumList.add(loanDTO.getNextPaymentAmountSum());
                sum[0] +=loanDTO.getNextPaymentAmountSum();
            });
        }
    }

    public void updateLoansTables(){

        updateLoansTable(this.customer.getOwnerList(),this.controllers1);

        updateLoansTable(this.customer.getLenderList(),this.controllers2);

        updateLoansTable(this.customer.getOwnerList(),this.controllers3);

        List<PaymentDto> futurePayments=new ArrayList<>();
        this.customer.getOwnerList().forEach(loanDTO ->{
            List<PaymentDto> localFuturePayments=loanDTO.getOwnerPayments().futurePaymentDtosList().stream().filter(paymentDto -> paymentDto.isItDone()==false).collect(Collectors.toList());
            futurePayments.addAll(localFuturePayments);
        });
        this.paymentsFutureViewController.setPayments(mainController.getYaz(),futurePayments);


        //BUY\SELL LOANS
        List<LoanDTO> buyLoans=this.mainController.getBuyLoans(this.loginName);
        List<LoanDTO> sellLoans=this.customer.getLenderList().stream().filter(loan->loan.getStatus()==Loan_status.active).collect(Collectors.toList());
        this.buySellLoansController.updateData(buyLoans,sellLoans);
    }

    private void updateLoansTable(List<LoanDTO> loans,Map<Loan_status,LoansNewController> controllers)
    {

        controllers.forEach((status , controller)->{

            controller.setLoansValues(loans.stream().filter(loanDTO -> loanDTO.getStatus().equals(status)).collect(Collectors.toList()));
        });
    }

    @FXML
    void chargeAction() throws Exception {
        openTextDialogText('+',"");
        //
    }

    @FXML
    void withdrawAction() throws Exception {
        openTextDialogText('-',"");
    }

    @FXML
    void maximumOpenCheckBoxAction(ActionEvent event) {
        if(maximumOpenCheckBox.isSelected())
            maximumOpenText.setDisable(false);
        else
            maximumOpenText.setDisable(true);
    }

    @FXML
    void maximumPercentCheckBoxAction(ActionEvent event) {
        if(maximumPercentCheckBox.isSelected())
            maximumPercentText.setDisable(false);
        else
            maximumPercentText.setDisable(true);
    }

    @FXML
    void minimumDurationCheckBoxAction(ActionEvent event) {
        if(minimumDurationCheckBox.isSelected())
            minimumDurationText.setDisable(false);
        else
            minimumDurationText.setDisable(true);
    }

    @FXML
    void minimumInterestCheckBoxAction(ActionEvent event) {
        if(minimumInterestCheckBox.isSelected())
            minimumInterestText.setDisable(false);
        else
            minimumInterestText.setDisable(true);
    }

    @FXML
    void submitScrambleFunc() throws Exception {//after chozen loans
        Set<String> loansId = new HashSet<>();
        int maximumPercent = 100;
        if (maximumPercentCheckBox.isSelected())
            maximumPercent = Integer.parseInt(maximumPercentText.getText());

        int amount = Integer.parseInt(amountTextBox.getText());

        filteredLoansController.getSelectedLoans().forEach(loanDTO -> {
            loansId.add(loanDTO.getId());
        });
        if(loansId.isEmpty()){

        }
        else
        {
            boolean isEnoghMoney=false;
            try{
                this.mainController.scheduling(loansId, this.customer.getName(), amount, maximumPercent);
                isEnoghMoney=true;
            } catch (Exception e) {
                notEnoghMoneyL.setVisible(true);
                notEnoghMoneyL.setText(e.getMessage());
                isEnoghMoney=false;
            }

            if(isEnoghMoney)
            {
                notEnoghMoneyL.setVisible(false);
                // updateInformationData();
            }


            //ANIMATION
            initAnimation(true);

            //init:
            initScramble();
            setCategoriesNames();
            this.filteredLoansController.clearTable();
        }
    }
    @FXML
    void submitFilterFunc(){
        int minimumDuration=1,maximumOpenLoans=maximumOpenLoansDefault
                ,amount,maximumPercent=100;
        Float minimumInterest=Float.parseFloat("0.0");

        if(minimumDurationCheckBox.isSelected()) {
            minimumDuration = Integer.parseInt(minimumDurationText.getText());
            notEnoghMoneyL.setVisible(false);
        }
        if(minimumInterestCheckBox.isSelected()) {
            minimumInterest = Float.parseFloat(minimumInterestText.getText());
            notEnoghMoneyL.setVisible(false);
        }
        if(maximumOpenCheckBox.isSelected())
            maximumOpenLoans=Integer.parseInt(maximumOpenText.getText());
        if(maximumPercentCheckBox.isSelected())
            maximumPercent=Integer.parseInt(maximumPercentText.getText());

        if(amountTextBox.getText().equals("")){
            notEnoghMoneyL.setVisible(true);
            notEnoghMoneyL.setText("you need to enter val");
        }
        else
        {
            notEnoghMoneyL.setVisible(false);
            amount=Integer.parseInt(amountTextBox.getText());

            List<String> categoriesNames=new ArrayList<>();
            selectedItems.getItems().forEach(name->categoriesNames.add(name));
            if(categoriesNames.isEmpty())
            {
                this.mainController.getAllcategroiesNames().stream().forEach(categoryDTO -> {
                    categoriesNames.add(categoryDTO.getName());
                });
            }
            //UPDATE FILTERD LOANS TO THE RIGHT TABLE
            progressScroll.progressProperty().unbind();
            FloatProperty progress = new SimpleFloatProperty(0);
            progressScroll.progressProperty().bind(progress);
            List<LoanDTO> filteredLoansFromApi= this.mainController.filteringLoansByParameters(filteredLoansController
                    ,progress,submitScambleB, amount,categoriesNames,minimumInterest,minimumDuration, customer.getName(),maximumOpenLoans);
        }
    }

    public void setTransactionValues(List<TransactionDTO> transactionDTOList){
        if(transactionDTOList!=null)
        {
            ObservableList<TransactionDTO> transactionDTOS = FXCollections.observableList(transactionDTOList);
            transactionsTable.getItems().setAll(FXCollections.observableList(transactionDTOS));
        }
    }
    private void setNotificationValues(List<NotificationDTO> notifications) {
        if(notifications!=null)
        {
            ObservableList<NotificationDTO> notificationDTOS = FXCollections.observableList(notifications);
            notoficationsPayments.getItems().setAll(FXCollections.observableList(notificationDTOS));
        }
    }

    public void sellBuyFunc(){}//TODO

    private void openTextDialogText(Character type,String msg) throws Exception {
        int value;
        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Text input dialog");
        if(msg.equals(""))
            msg="Please enter positive value";
        textInputDialog.setHeaderText(msg);
        textInputDialog.setContentText("Value:");

        Optional<String> result=textInputDialog.showAndWait();
        if(result.isPresent())
        {

            try {
                value=Integer.parseInt(result.get());
                if(value>0)
                {
                    this.mainController.loadingWithdrawalMoney(this.customer.getName(),type,value);
                    //updateInformationData();
                }

            } catch (Exception e) {
                openTextDialogText(type,e.getMessage());
            }

        }
    }

    private void validityTextBoxInt(TextField textbox){
        //amount text
        textbox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textbox.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public String getDigitsAndPoint(String str){
        int index=str.indexOf('.');
        String str2;
        System.out.println("i="+index);
        if (index==str.length()-1)
            str2 = "";
        else
            str2 = str.substring(index+1).replaceAll("[\\.]", "");
        str = str.substring(0, index+1);
        str=str+str2;
        System.out.println(str);
        return str.chars().filter(ch -> ch=='.' || (ch>='0' && ch<='9'))
                .mapToObj(i -> "" + (char) i).collect(Collectors.joining());
    }

    public boolean checkIfFloatNumber(String str) {
        long pointsCount = str.chars().filter(ch -> ch=='.').count();
        if(pointsCount > 1)
            return false;
        if(pointsCount == 0)
            return true;
        int index = str.indexOf('.');
        return (index != 0);
    }


    public void validityTextBoxFloat(TextField textbox) {
        //amount text
        textbox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue=getDigitsAndPoint(newValue);
                System.out.println(newValue);
                if(!checkIfFloatNumber(newValue))
                    System.out.println(newValue);
                textbox.setText(newValue);
            }
        });
    }

    private void makeValidTextBoxes(){
        validityTextBoxInt(amountTextBox);
        validityTextBoxInt(maximumOpenText);
        validityTextBoxInt(maximumPercentText);
        validityTextBoxInt(minimumDurationText);
        validityTextBoxFloat(minimumInterestText);
    }
    public void setCategoriesNames() {
        if(this.mainController.getAllcategroiesNames()!=null)
        {

            categoriesNames=this.mainController.getAllcategroiesNames().stream()
                    .map(categoryDTO -> categoryDTO.getName()).collect(Collectors.toList());
            //if(!categoriesNames.get(0).equals("*All categories*"))
            categoriesNames.add(0,"*All categories*");
            ObservableList<String> categoriesTemp= selectedItems.getItems();

            selectedItems.getItems().clear();
            List<CheckMenuItem> checkItems =new ArrayList<>();
            categoriesSelection.getItems().clear();
            categoriesSelection.setText("Categories");
            categoriesNames.forEach(name -> {
                if(!checkItems.contains(name))
                    checkItems.add(new CheckMenuItem(name));

            });
            assert checkItems != null;
            categoriesSelection.getItems().addAll(checkItems);
            selectedItems.getItems().addAll(categoriesTemp);

    /*for (final CheckMenuItem item : checkItems) {
        item.setSelected(true);
    }*/

            // Keep track of selected items
            for (final CheckMenuItem item : checkItems) {
                item.selectedProperty().addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean> obs,
                                        Boolean wasPreviouslySelected, Boolean isNowSelected) {

                        if (isNowSelected) {
                            if(item.getText().equals("*All categories*"))
                            {
                                selectedItems.getItems().clear();
                                selectedItems.getItems().setAll(categoriesNames);
                                for (final CheckMenuItem item : checkItems) {
                                    item.setSelected(true);
                                }
                            }
                            else
                                selectedItems.getItems().add(item.getText());

                        }
                        else{
                            if(item.getText().equals("*All categories*"))
                            {
                                selectedItems.getItems().clear();
                                for (final CheckMenuItem item : checkItems) {
                                    item.setSelected(false);
                                }
                            }
                            else
                            {
                                selectedItems.getItems().remove(item.getText());
                                if(checkItems.get(0).getText().equals("*All categories*")){
                                    checkItems.get(0).setSelected(false);
                                    selectedItems.getItems().remove("*All categories*");
                                }
                            }

                        }
                    }

                });
            }

        }





    }
    public void setSkin(int numSkin){
        switch (numSkin){
            case 2:
                this.lednerLoansGrid.getStyleClass().setAll("skin3");
                this.notoficationsGrid.getStyleClass().setAll("skin3");
                this.loanerLoansGrid.getStyleClass().setAll("skin3");
                this.transactionGrid.getStyleClass().setAll("skin3");
                this.rightScrambleGrid.getStyleClass().setAll("skin3");
                this.loanerTableGrid.getStyleClass().setAll("skin3");


                this.mainBorderPain.getStyleClass().setAll("skin6");
                //this.borderPaneAnimation.getStyleClass().setAll("skin2");

                this.acnhorInformation.getStyleClass().setAll("skin5");
                this.leftPaymentsAnchor.getStyleClass().setAll("skin5");
                this.rightPaymentsAnchor.getStyleClass().setAll("skin5");
                this.rightScrambleAnchor.getStyleClass().setAll("skin5");


                break;
            case 3:
                this.lednerLoansGrid.getStyleClass().setAll("skin7");
                this.notoficationsGrid.getStyleClass().setAll("skin7");
                this.loanerLoansGrid.getStyleClass().setAll("skin7");
                this.transactionGrid.getStyleClass().setAll("skin7");
                this.rightScrambleGrid.getStyleClass().setAll("skin7");
                this.loanerTableGrid.getStyleClass().setAll("skin7");


                this.mainBorderPain.getStyleClass().setAll("skin7");

                this.acnhorInformation.getStyleClass().setAll("skin1");
                this.leftPaymentsAnchor.getStyleClass().setAll("skin1");
                this.rightPaymentsAnchor.getStyleClass().setAll("skin1");
                this.rightScrambleAnchor.getStyleClass().setAll("skin1");
                break;
            default:
                this.lednerLoansGrid.getStyleClass().setAll("skin1");
                this.notoficationsGrid.getStyleClass().setAll("skin1");
                this.loanerLoansGrid.getStyleClass().setAll("skin1");
                this.transactionGrid.getStyleClass().setAll("skin1");
                this.rightScrambleGrid.getStyleClass().setAll("skin1");
                this.loanerTableGrid.getStyleClass().setAll("skin1");


                this.mainBorderPain.getStyleClass().setAll("skin4");
                //this.borderPaneAnimation.getStyleClass().setAll("skin8");
                this.acnhorInformation.getStyleClass().setAll("skin6");
                this.leftPaymentsAnchor.getStyleClass().setAll("skin6");
                this.rightPaymentsAnchor.getStyleClass().setAll("skin6");
                this.rightScrambleAnchor.getStyleClass().setAll("skin6");

                break;
        }
    }
    public float getBalance(){return this.customer.getBalance();}

    public float getLeftToPayPaymentsPerLoan(String loanId){
        return this.customer.getOwnerList().stream()
                .filter(loan->loan.getId().equals(loanId))
                .map(loanDTO -> loanDTO.getTotalAmountLeftToPay()).findFirst().get();

    }

    public void sendAmountFromPayToApi(String loanId,float amount, boolean isPayAllPayments) {
        this.mainController.sendAmountFromPayToApi(loanId,amount,isPayAllPayments);
    }
    public void updateCurrentYaz(){this.mainController.updateCurrentYaz();}
    public void setMainController(mainController mainController){this.mainController=mainController;}
    /* public void setCustomer(String customerName){
         this.customer=this.mainController.getCustomerByName(customerName);
     }*/
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String name){
        this.loginName=name;

    }

    public boolean isIsFileSelected() {
        return isFileSelected.get();
    }

    public void setLayout(BorderPane customerLayout) {
    }

    public void createNewLoan(String id, String category, int capital, float interest, int totalYazTime, int paymentRate) {
        this.mainController.createNewLoan(id,this.loginName,category,capital,interest,totalYazTime,paymentRate);
        //this.mainController.updateCategories();
        setCategoriesNames();
        /*try {
            updateInformationData();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void buySellLoans(String loanId,String sellerName,boolean isForSell) {
        this.mainController.buySellLoans(loanId,this.loginName,sellerName,isForSell);
    }

    public void setScrableButton() {
        this.submitScambleB.setDisable(false);
    }

    public void freeButton() {
        this.submitScambleB.setDisable(false);
        this.withdrawMoneyB.setDisable(false);
        this.chargeMoneyB.setDisable(false);
        this.submitFilterB.setDisable(false);
        this.categoriesSelection.setDisable(false);
        this.paymentsFutureViewController.freeButton();
        this.newLoanController.freeButton();
        //this.buySellLoansController.freeButton();


    }

    public void blockButton() {
        this.submitScambleB.setDisable(true);
        this.withdrawMoneyB.setDisable(true);
        this.chargeMoneyB.setDisable(true);
        this.submitFilterB.setDisable(true);
        this.categoriesSelection.setDisable(true);
        this.paymentsFutureViewController.blockButton();
        this.newLoanController.blockButton();
        //this.buySellLoansController.blockButton();
    }
}
