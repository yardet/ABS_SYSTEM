package admin.components.chatroom;

import admin.components.api.HttpStatusUpdate;
import admin.components.login.LoginController;
import admin.components.main.mainController;
import admin.components.status.StatusController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static admin.util.Constants.*;


public class ChatAppMainController implements Closeable, HttpStatusUpdate {

    @FXML private Parent httpStatusComponent;
    @FXML private StatusController httpStatusComponentController;

    private GridPane loginComponent;
    private LoginController loginController;

    private Parent chatRoomComponent;
    private ChatRoomMainController chatRoomComponentController;

    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;

    private final StringProperty currentUserName;
    private admin.components.main.mainController mainController;

    public ChatAppMainController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));
        //loadPages();
    }

    public void loadPages(){
        // prepare components
        loadLoginPage();
        loadChatRoomPage();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    public void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

    @Override
    public void close() throws IOException {
        chatRoomComponentController.close();
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            this.mainController.setLoginController(loginController);
            this.mainController.setLoginComponent(loginComponent);
            loginController.setChatAppMainController(this);
            loginController.setMainController(this.mainController);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadChatRoomPage() {
        URL loginPageUrl = getClass().getResource(CHAT_ROOM_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            chatRoomComponent = fxmlLoader.load();
            chatRoomComponentController = fxmlLoader.getController();
            chatRoomComponentController.setChatAppMainController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHttpLine(String line) {
        httpStatusComponentController.addHttpStatusLine(line);
    }

    public void switchToChatRoom() {
        setMainPanelTo(chatRoomComponent);
        chatRoomComponentController.setActive();
    }

    public void switchToLogin() {
        Platform.runLater(() -> {
            currentUserName.set(JHON_DOE);
            chatRoomComponentController.setInActive();
            setMainPanelTo(loginComponent);
            try {
                this.mainController.loadLoginPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setLoginController(LoginController loginController) {
        this.loginController=loginController;
    }

    public void setLoginComponent(GridPane loginComponent) {
        this.loginComponent=loginComponent;
    }

    public void setMainController(mainController mainController) {
        this.mainController=mainController;
    }

    public LoginController getLoginController() {
        return this.loginController;
    }

    public GridPane getLoginComponent() {
        return this.loginComponent;
    }

    public void logout() {
        this.mainController.logOut();
    }
}
