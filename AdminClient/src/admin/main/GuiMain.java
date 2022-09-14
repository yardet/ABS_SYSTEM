package admin.main;

import admin.components.login.LoginController;
import admin.util.http.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import gui.Admin.AdminController;
import gui.LoansTables.animation2Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import customers.CustomerDTO;
import jdk.internal.org.objectweb.asm.TypeReference;
import loans.LoanDTO;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static admin.util.Constants.*;


public class GuiMain extends Application {
    private static final String LOAD_OK ="SUCCESSFULLY";
    private Stage primaryStage;
    private BorderPane mainLayout;
    private ScrollPane mainScroll;
    private BorderPane adminLayout;
    private boolean isAdminLoaded=false;
    private admin.components.main.mainController mainController;
    private String loginName;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    Timer timer;



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        this.primaryStage.setTitle("ABS APP");
        showMainView();
        this.mainLayout=this.mainController.getMainPane();
        this.mainController.setMain(this);
    }

    private void showMainView() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiMain.class.getResource(MAIN_PAGE_FXML_LOCATION));
        this.mainScroll=loader.load();
        this.mainScroll.setFitToHeight(true);
        this.mainScroll.setFitToWidth(true);
        Scene scene = new Scene(this.mainScroll);

        primaryStage.setScene(scene);
        primaryStage.show();

        this.mainController=loader.getController();
        this.mainController.setPrimaryStage(this.primaryStage);
        this.mainController.setMainLayout(this.mainLayout);
        this.mainController.setMain(this);
    }

    public void showAdminView() throws IOException{
        if(!this.isAdminLoaded)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(ADMIN_PAGE_FXML_LOCATION));
            BorderPane adminLayout=loader.load();
            this.adminLayout=adminLayout;
            mainLayout.setCenter(adminLayout);

            AdminController adminController=loader.getController();
            this.mainController.setAdminController(adminController);
            adminController.setLayout(this.adminLayout);
            adminController.setMainController(this.mainController);
            this.isAdminLoaded=true;
        }
        else
        {
            mainLayout.setCenter(this.adminLayout);
            this.primaryStage.show();
        }

    }

    public void showAnimation2View() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ANIMATION2_PAGE_FXML_LOCATION));
        BorderPane animationLayout=loader.load();

        Scene scene = new Scene(animationLayout);
        Stage stage=new Stage();
        stage.setScene(scene);
        animation2Controller animationController=loader.getController();
        animationController.setStage(stage);
        animationController.setAnimation(2,false);
    }
    public void getAllLoansInfo(){
        // return  api.getAllLoansInfo();
        final List<LoanDTO>[] loansInfo = new List[]{new ArrayList<>()};
        String finalUrlInformation = HttpUrl.parse(GET_ALL_LOANS_PAGE)
                .newBuilder()
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() -> {
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        System.out.println(String.valueOf(errorMessageProperty));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        response.body().close();
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());
                } else{

                    Platform.runLater(() -> {
                        try {
                            List<LoanDTO> loanDTOS = null;
                            String jsonArrayOfInformation = response.body().string();
                            loanDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation, new TypeToken<List<LoanDTO>>(){}.getType());
                            mainController.updateLoans(loanDTOS);
                            response.body().close();
                            //openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        //return loansInfo[0];
    }

    public void getAllCustomersInfo(){
        //return  api.getAllCustomersInfo();
        final List<CustomerDTO>[] customerInfo = new List[]{new ArrayList<>()};
        String finalUrlInformation = HttpUrl.parse(GET_ALL_CUSTOMERS_PAGE)
                .newBuilder()
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        System.out.println(String.valueOf(errorMessageProperty));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        response.body().close();
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());
                } else{
                    Platform.runLater(() -> {
                        try {
                            List<CustomerDTO> customerDTOS ;
                            String jsonArrayOfInformation = response.body().string();
                            customerDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation,new TypeToken<List<CustomerDTO>>(){}.getType());
                            mainController.updateCustomers(customerDTOS);
                            //System.out.println(response.body().string());
                            response.body().close();
                            //openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        //return customerInfo[0];
    }
    public void updateCurrentYaz(){
        //return this.api.getCurrentYaz();
        final int[] currentYaz = new int[1];
        String finalUrlInformation = HttpUrl.parse(GET_CURRENT_YAZ_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() -> {
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        System.out.println(String.valueOf(errorMessageProperty));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set(response.message());
                        response.body().close();
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                } else{
                    Platform.runLater(() -> {
                        try {

                            String responseFromJson= GSON_INSTANCE.fromJson(response.body().string(), String.class);
                            String[] arrOfStr=responseFromJson.split(" ",2);
                            currentYaz[0]= Integer.parseInt(arrOfStr[0]);
                            String systemStatus=arrOfStr[1];
                            mainController.setYazLabel(currentYaz[0]);
                            mainController.updateSystemStatus(new boolean[]{systemStatus.equals("t") ? true :
                                    false});
                            response.body().close();
                            //openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        //return currentYaz[0];
    }

    public void increaseYaz(){
        //this.api.promotingTimeline(false);
        final boolean[] isRewind = new boolean[1];
        //isRewind[0]=false;
        String finalUrlInformation = HttpUrl.parse(INCREASE_YAZ_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() -> {
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());

                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        response.body().close();
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());
                } else{
                    Platform.runLater(() -> {
                        try {
                            openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                }
            }
        });
    }


    //TODO SAVE TO THE UPDATE BACK
    public void decreaseYaz(){
        final boolean[] isRewind = {false};
        //this.api.promotingTimeline(true);
        //this.api.promotingTimeline(false);
        String finalUrlInformation = HttpUrl.parse(DECREASE_YAZ_PAGE)
                .newBuilder()
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() -> {
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        response.body().close();
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());
                } else{
                    Platform.runLater(() -> {

                        try {

                            openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();

                    });
                }
            }
        });
    }

    private void openTextDialogText(String msg) throws Exception {
        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Error dialog");
        textInputDialog.setContentText(msg);
        textInputDialog.showAndWait();
    }


    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String name){this.loginName=name;}

    public void startInfoRefresh(){
        AdminRefresher adminInfoRefresher = new AdminRefresher(this);
        timer=new Timer();
        timer.schedule(adminInfoRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public static void main(String args[]){
        launch(args);
    }
}




