package admin.components.login;
import admin.components.chatroom.ChatAppMainController;
import admin.components.main.mainController;
import admin.util.Constants;
import admin.util.http.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import customers.CustomerDTO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginController {

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Label errorMessageLabel;

    private admin.components.main.mainController mainController;
    private ChatAppMainController chatAppMainController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();

    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
        HttpClientUtil.setCookieManagerLoggingFacility(
                line ->
                        Platform.runLater(() ->
                                updateHttpStatusLine(line)));
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) throws IOException {

       // TextField l=new TextField();
      //  l.setText(passwordTextField.getText());
        String password=this.passwordTextField.getText();

        String userName="ADMIN";
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
            return;
        }
        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(Constants.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("username", userName)
                .addQueryParameter("password", password)
                .addQueryParameter("isAdmin", "true")
                .build()
                .toString();

        updateHttpStatusLine("New request is launched for: " + finalUrl);

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                //TODO- RESPONSE CODE ALREADY IN

                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        errorMessageProperty.set("PASSWORD NOT OK");
                        //response.body().close();
                    });
                } else {
                    Platform.runLater(() -> {
                        chatAppMainController.updateUserName(userName);
                        chatAppMainController.switchToChatRoom();
                        mainController.initData();
                        mainController.setLoginName(userName);
                        try {
                            mainController.showAdminController();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //response.body().close();
                    });
                }
            }
        });

    }

    @FXML
    private void userNameKeyTyped(KeyEvent event) {
        errorMessageProperty.set("");
    }

    @FXML
    private void quitButtonClicked(ActionEvent e) {
        Platform.exit();
    }

    private void updateHttpStatusLine(String data) {
        chatAppMainController.updateHttpLine(data);

    }

    public void setMainController(mainController mainController){this.mainController=mainController;}
    public void setChatAppMainController(ChatAppMainController chatAppMainController) {
        this.chatAppMainController = chatAppMainController;
    }
}
