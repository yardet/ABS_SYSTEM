package admin.components.main;

import admin.components.api.HttpStatusUpdate;
import admin.components.chatroom.ChatAppMainController;
import admin.components.login.LoginController;
import admin.components.status.StatusController;
import admin.main.GuiMain;
import com.sun.deploy.panel.JreFindDialog;
import gui.Admin.AdminController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import customers.CustomerDTO;
import loans.LoanDTO;
import okhttp3.internal.platform.Platform;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static admin.util.Constants.*;

public class mainController implements Initializable {

    private String loginName;
    private Stage primaryStage;
    private GuiMain main;
    private AdminController adminController;
    private LoginController loginController;

    private Stage chatRoomPrimaryStage=new Stage();

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label yazLabel;

    @FXML
    private ComboBox<String> skinCombo;

    @FXML
    private GridPane toolBar;

    private gui.LoansTables.animation2Controller animation2Controller;
    private boolean isAdminOpened=false;
    private BorderPane mainLayout;
    private ChatAppMainController chatAppMainController;
    private GridPane loginComponent;
    private boolean chatLoaded=false;
    private List<CustomerDTO> customers;
    private List<LoanDTO> loans;
    private boolean systemStatus=false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.yazLabel.setText("0");
        skinCombo.getItems().addAll("skin1","skin2","skin3","animation","chat");
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
        if(skinCombo.getValue().equals("chat"))
        {
            loadChatRoom();
        }
    }

    private void setSkin(int numSkin){
        if(isAdminOpened)
            this.adminController.setSkin(numSkin);
        /*if(isCustomersOpened)
            this.customersController.setSkin(numSkin);*/
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
    public BorderPane getMainPane(){return this.mainPane;}
    public Stage getPrimaryStage(){return this.primaryStage;}
    public void updateCurrentYaz(){
        this.main.updateCurrentYaz();}
    public List<LoanDTO> getAllLoansInfo(){
        //this.main.getAllLoansInfo();
        return loans;
    }
    public List<CustomerDTO> getAllCustomersInfo(){
        //this.main.getAllCustomersInfo();
        return customers;
    }
    public List<String> getAllCustomersNames(){return customers.stream().map(customerDTO -> customerDTO.getName()).collect(Collectors.toList());}
    public CustomerDTO getCustomerByName(String name){
        return customers.stream().filter(customerDTO -> customerDTO.getName().equals(name)).findAny().get();
    }
    public void increaseYaz(){this.main.increaseYaz();}
    public void decreaseYaz() {this.main.decreaseYaz();}
    public void setMain(GuiMain main){this.main=main;}
    public void setYazLabel(int val){
        this.yazLabel.setText(String.valueOf(val));}
    public void setPrimaryStage(Stage primaryStage){this.primaryStage=primaryStage;}
    public void setAdminController(AdminController adminController){this.adminController=adminController;}
    public void setMainLayout(BorderPane mainLayout) {
        this.mainLayout=mainLayout;
    }
    public String getLoginName(){return this.loginName; }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void updateUserName(String userName) {
        this.loginName=userName;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController=loginController;
    }

    public void showAdminController() throws IOException {
        this.main.showAdminView();
        this.main.startInfoRefresh();
        //this.adminController.updateAdminData();
    }

    public void updateCustomers(List<CustomerDTO> customerDTOS) {
        this.customers=customerDTOS;
        this.adminController.updateAdminData();
    }

    public void updateLoans(List<LoanDTO> loanDTOS) {
        this.loans=loanDTOS;

    }
    // public CustomersController getController(){return this.customersController;}

    public void logOut() {
        this.adminController.initAlldata();
    }

    public void setLoginComponent(GridPane loginComponent) {
        this.loginComponent=loginComponent;
    }

    public void initData() {
    }

    public void updateSystemStatus(boolean[] isRewind) {
        this.systemStatus=isRewind[0];
        if(this.systemStatus){
            blockButton();
        }
        else{
            freeButton();

        }
        if(this.getYaz()==1){
            blockButton();
        }
    }

    private void freeButton() {
        if(this.adminController!=null)
        {
            this.adminController.freeButton();
        }

    }

    private void blockButton() {

        if(adminController!=null)
        {
            this.adminController.blockButton();
        }

    }

    public int getYaz() {
        return Integer.parseInt(this.yazLabel.getText());
    }
}
