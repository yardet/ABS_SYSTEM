package user.components.main;

import gui.Customers.CustomersController;
import gui.LoansTables.LoansPendingController;
import gui.LoansTables.SpecialBuyLoanDTO;
import javafx.beans.property.FloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import categories.CategoryDTO;
import customers.CustomerDTO;
import loans.LoanDTO;
import user.components.chatroom.ChatAppMainController;
import user.components.login.LoginController;
import user.main.GuiMain;
import user.util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import static user.util.Constants.*;

public class mainController implements Initializable {

    private Stage primaryStage;
    private GuiMain main;
    private CustomersController customersController;

    private ChatAppMainController chatAppMainController;


    @FXML
    private Label userNameLabel;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button loadFileB;

    @FXML
    private Label yazLabel;


    @FXML
    private ComboBox<String> skinCombo;

    @FXML
    private GridPane toolBar;


    private gui.LoansTables.animation2Controller animation2Controller;
    private boolean isCustomersOpened=false;
    private BorderPane mainLayout;
    private String loginName;
    private LoginController loginController;
    private GridPane loginComponent;
    private boolean chatLoaded=false;
    private Set<CategoryDTO> categories;
    private List<LoanDTO> loans;
    private CustomerDTO customer;
    private List<LoanDTO> filterdLoans;
    private Stage chatRoomPrimaryStage=new Stage();
    private List<LoanDTO> buyLoans;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.loadFileB.setVisible(false);
        this.yazLabel.setText("0");
        skinCombo.getItems().addAll("skin1","skin2","skin3","animation","chat");
        //autoScroll = new SimpleBooleanProperty();
        //autoScroll.bind(autoScrollCB.selectedProperty());
        this.userNameLabel.setVisible(false);
        categories=null;

        initChatRoom();
        try {
            loadLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void initChatRoom() {
        this.chatRoomPrimaryStage.setMinHeight(600);
        this.chatRoomPrimaryStage.setMinWidth(600);
        this.chatRoomPrimaryStage.setTitle("Chat App Client");

        URL mainChatURL = getClass().getResource(MAIN_CHAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(mainChatURL);
            Parent root = fxmlLoader.load();
            chatAppMainController = fxmlLoader.getController();
            chatAppMainController.setMainController(this);
            chatAppMainController.loadPages();
            Scene scene = new Scene(root, 700, 600);
            this.chatRoomPrimaryStage.setScene(scene);
            //this.chatRoomPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChatRoom() {
        this.chatRoomPrimaryStage.show();
    }

    public void loadLoginPage() throws IOException{
        this.mainPane.setCenter(this.loginComponent);
    }

    @FXML
    void loadFileButtonAction(ActionEvent event) {
        this.customersController.loadFileButtonAction(event);
    }
    public void setFileEngine(String pathName, File selectedFile) throws Exception {
        this.main.setFile(pathName,selectedFile,this.loginName);
    }
    public List<LoanDTO> filteringLoansByParameters(LoansPendingController filteredLoansController, FloatProperty progress,Button submitScambleB, float amount, List<String> categoriesNames, float minimumInterest
            , int minimumTotalYazTime, String customerName, int numOfOpenLoans){
        this.main.filteringLoansByParameters(filteredLoansController, progress,submitScambleB, amount,categoriesNames,minimumInterest,minimumTotalYazTime
                ,customerName,numOfOpenLoans);
        return filterdLoans;
    }

    public void scheduling(Set<String> selectedLoansId,String customerName
            ,int amount,int percentLoan) throws Exception//6
    {this.main.scheduling(selectedLoansId,customerName,amount,percentLoan);}

    public BorderPane getMainPane(){return this.mainPane;}
    public Stage getPrimaryStage(){return this.primaryStage;}

    public void updateCurrentYaz(){
        this.main.updateCurrentYaz();}

    public List<LoanDTO> getLoansInfo(){
        //this.main.getLoansInfo();
        return loans;
    }
    public CustomerDTO getCustomersInfo(){
        //this.main.getCustomersInfo();
        return customer;
    }
    /*public List<String> getAllCustomersNames(){
        return customer.stream().map(customerDTO -> customerDTO.getName()).collect(Collectors.toList());}
*/
    public Set<CategoryDTO> getAllcategroiesNames(){
        //this.main.getAllcategroiesNames();
        return categories;
    }

    /*public CustomerDTO getCustomerByName(String name){
        //return customer.stream().filter(customerDTO -> customerDTO.getName().equals(name)).findAny().get();
        return customer;
    }*/
    public void loadingWithdrawalMoney(String name,char type,int amount) throws Exception {this.main.loadingWithdrawalMoney(name,type,amount);}
    public void setMain(GuiMain main){this.main=main;}
    public void setYazLabel(int val){this.yazLabel.setText(String.valueOf(val));}
    public void setPrimaryStage(Stage primaryStage){this.primaryStage=primaryStage;}

    public void setCustomerController(CustomersController customersController){
        this.customersController=customersController;
    }
    public void sendAmountFromPayToApi(String loanId,float amount, boolean isPayAllPayments) {
        this.main.sendAmountFromPayToApi(loanId,amount,isPayAllPayments);
    }
    public void setMainLayout(BorderPane mainLayout) {
        this.mainLayout=mainLayout;
    }
    public CustomersController getCustomersController(){return this.customersController;}


    public void setLoginName(String userName) {
        this.loginName=userName;
        this.main.setLoginName(userName);
    }

    public void setLoginController(LoginController loginController) {
        this.loginController=loginController;
    }

    public void showCustomerView() throws IOException {
        this.main.showCustomersView();
        userNameLabel.setText("Hello "+this.loginName);
        this.main.startInfoRefresh();
    }

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoadFileVisible() {
        this.loadFileB.setVisible(true);
    }

    public void createNewLoan(String id, String loginName, String category, int capital, float interest, int totalYazTime, int paymentRate) {
        this.main.createNewLoan(id,loginName,category,capital,interest,totalYazTime,paymentRate);
    }

    public List<LoanDTO> getBuyLoans(String loginName) {//TODO
        //this.main.getBuyLoans(loginName);
        return buyLoans;
    }

    public void buySellLoans(String loanId, String loginName,String sellerName, boolean isForSell) {
        this.main.buySellLoans(loanId,loginName,sellerName,isForSell);
    }

    public void setLabelHelloVisiable() {
        this.userNameLabel.setVisible(true);
    }

    public void close() throws IOException {
        HttpClientUtil.shutdown();
        chatAppMainController.close();
    }

    public int getYaz() {
        return Integer.parseInt(yazLabel.getText());
    }

    public void updateCategories(Set<CategoryDTO> categoryDTOS) throws IOException {
        this.categories=categoryDTOS;
    }


    public void updateLoans(List<LoanDTO> loanDTOS) {
        this.loans=loanDTOS;
    }

    public void updateCustomers(CustomerDTO customerDTOS) throws IOException {
        this.customer =customerDTOS;
        this.customersController.updateInformationData();

    }

    public void filterdLoans(List<LoanDTO> loanDTOS) {
        if(loanDTOS!=null){
            this.filterdLoans=loanDTOS;
            if(!loanDTOS.isEmpty()){
                this.customersController.setScrableButton();
            }
        }

        if(!loanDTOS.isEmpty()){

        }
    }

    public void setLoginComponent(GridPane loginComponent) {
        this.loginComponent=loginComponent;
    }

    public void initData() {
    }

    @FXML
    public void skinComboAction(ActionEvent actionEvent) throws IOException {
        if(skinCombo.getValue().equals("skin1")) {
            this.setSkin(1);
        }
        else if(skinCombo.getValue().equals("skin2")){
            this.setSkin(2);
        }
        else if(skinCombo.getValue().equals("skin3")){
            this.setSkin(3);
        }
        if(skinCombo.getValue().equals("animation"))
        {
            this.main.showAnimation2View();
        }
        if(skinCombo.getValue().equals("chat")){
            this.loadChatRoom();
        }
    }

    private void setSkin(int numSkin){
        if(isCustomersOpened)
            this.customersController.setSkin(numSkin);
        switch (numSkin){
            case 2:
                //mainView
                this.mainPane.getStyleClass().setAll("skin3");
                this.mainPane.getStyleClass().setAll("skin3");
                this.toolBar.getStyleClass().setAll("skin4");


                break;
            case 3:
                this.mainPane.getStyleClass().setAll("skin6");
                this.mainPane.getStyleClass().setAll("skin6");
                this.toolBar.getStyleClass().setAll("skin5");
                break;
            default:
                this.mainPane.getStyleClass().setAll("skin2");
                this.mainPane.getStyleClass().setAll("skin2");
                this.toolBar.getStyleClass().setAll("skin7");
                break;
        }
    }

    public void logOut() {
        this.customersController.initAllData();
    }

    public void updateBuyLoans(List<LoanDTO> loanDTOS) {
        this.buyLoans=loanDTOS;
    }

    public void updateSystemStatus(boolean[] ts) {
        boolean sign=ts[0];
        if(sign){
            blockButton();
        }
        else
        {
            freeButton();
        }
    }

    private void freeButton() {
        if(this.customersController!=null)
        {
            this.customersController.freeButton();
            loadFileB.setDisable(false);
        }

    }

    private void blockButton() {
        if(this.customersController!=null)
        {
            this.customersController.blockButton();
            loadFileB.setDisable(true);
        }

    }
}
