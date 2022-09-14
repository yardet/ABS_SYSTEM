package user.main;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gui.Customers.CustomersController;
//import gui.Customers.FilteringLoansTask;
import gui.LoansTables.LoansPendingController;
import gui.LoansTables.animation2Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/*import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;*/
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import categories.CategoryDTO;
import customers.CustomerDTO;
import loans.LoanDTO;
import okhttp3.*;
import services.customers_service.Customer;
import services.main_service.Engine;
import services.main_service.EngineInterface;
import user.components.chatroom.ChatAppMainController;
import user.components.login.LoginController;
import user.util.Constants;
import user.util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

import static user.util.Constants.*;


public class GuiMain extends Application {
    private static final String LOAD_OK ="SUCCESSFULLY" ;
    private final StringProperty errorMessageProperty = new SimpleStringProperty();
    private Stage primaryStage;
    private Stage loginStage;
    private BorderPane mainLayout;
    private ScrollPane mainScroll;
    private ScrollPane loginScroll;
    private BorderPane adminLayout;
    private BorderPane customerLayout;
    private boolean isAdminLoaded=false;
    private boolean isCustomerLoaded=false;
    private user.components.main.mainController mainController;
    private LoginController loginController;
    private String loginName;
    private Timer timer;

    //private EngineInterface api;
    private ChatAppMainController chatAppMainController;


    @Override
    public void start(Stage primaryStage) throws Exception {
        //this.api=new Engine();
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
        this.mainController.setMainLayout(mainLayout);
        this.mainController.setMain(this);
    }


    public void showCustomersView() throws IOException{
        if(!this.isCustomerLoaded)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GuiMain.class.getResource(CUSTOMER_PAGE_FXML_LOCATION));
            BorderPane customersLayout=loader.load();
            this.customerLayout=customersLayout;
            mainLayout.setCenter(customersLayout);

            CustomersController customersController=loader.getController();

            customersController.setMainController(this.mainController);
            customersController.setLoginName(mainController.getLoginName());
            customersController.initNewLoanFxml();
            //customersController.initBuySellFxml();
            this.mainController.setCustomerController(customersController);
          //  getAllcategroiesNames();
          //  customersController.setCategoriesNames();
            this.isCustomerLoaded=true;//TODO
        }
        else
        {
            mainLayout.setCenter(this.customerLayout);
            this.primaryStage.show();
        }

    }

    public void showAnimation2View() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/gui/LoansTables/animation2.fxml"));
        BorderPane animationLayout=loader.load();

        Scene scene = new Scene(animationLayout);
        Stage stage=new Stage();
        stage.setScene(scene);
        animation2Controller animationController=loader.getController();
        animationController.setStage(stage);
        animationController.setAnimation(2,false);
    }

    public void setFile(String pathName, File selectedFile, String loginName) throws Exception {
        try {
            initEngineFromXmlFile(pathName,selectedFile,loginName);
            setLoginName(loginName);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void startInfoRefresh(){
        userRefresher customerInfoRefresher = new userRefresher(this);
        Timer timer=new Timer();
        timer.schedule(customerInfoRefresher, REFRESH_RATE, REFRESH_RATE);
    }



    //SERVER:
    //GET FROM SERVER:
    public void getLoansInfo(){
        //return  api.getAllLoansInfo();
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
                    errorMessageProperty.set("not connected to server " + e.getMessage());
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
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
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

    public void getAllcategroiesNames(){
        final Set<CategoryDTO>[] categoryInfo = new Set[]{new HashSet<>()};
        //return api.getAllCategoriesInfo();
        String finalUrlInformation = HttpUrl.parse(GET_ALL_CATEGORIES_PAGE)
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
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());
                } else{
                    Platform.runLater(() -> {
                        try {
                            Set<CategoryDTO> categoryDTOS = null;
                            String jsonArrayOfInformation = response.body().string();
                            categoryDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation, new TypeToken<Set<CategoryDTO>>(){}.getType());
                            mainController.updateCategories(categoryDTOS);
                            // System.out.println(response.body().string());
                            response.body().close();
                            //openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        //return categoryInfo[0];
    }

    public void getCustomersInfo(){
        //return  api.getAllCustomersInfo();
        final List[] customerInfo = new List[]{new ArrayList<>()};
        String finalUrlInformation = HttpUrl.parse(GET_CUSTOMER_PAGE)
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
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {
                        try {
                            CustomerDTO customerDTOS = null;
                            String jsonArrayOfInformation = response.body().string();
                            customerDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation, CustomerDTO.class);
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

    public void getBuyLoans() {
        //return this.api.getBuyLoans(loginName);
        final List<LoanDTO>[] loansInfo = new List[]{new ArrayList<>()};
        String finalUrlInformation = HttpUrl.parse(GET_BUY_LOANS_PAGE)
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
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {

                        try {
                            List<LoanDTO> loanDTOS = null;
                            String jsonArrayOfInformation = response.body().string();
                            loanDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation, new TypeToken<List<LoanDTO>>(){}.getType());
                            mainController.updateBuyLoans(loanDTOS);
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

        //return loansInfo[0];
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
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        System.out.println(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }

                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        try {
                            System.out.println(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());
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
        // return currentYaz[0];

    }
    public List<LoanDTO> filteringLoansByParameters(LoansPendingController filteredLoansController, FloatProperty progress, Button submitScambleB, float amount, List<String> categoriesNames, float minimumInterest
            , int minimumTotalYazTime, String customerName, int numOfOpenLoans){
        final List<LoanDTO>[] loansInfo = new List[]{new ArrayList<>()};
        Gson gson = new Gson();
        String json = gson.toJson(categoriesNames);
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json"));
        //finalUrl
        String finalUrlInformation = HttpUrl.parse(FILTERING_PAGE)
                .newBuilder()
                .addQueryParameter("amount", String.valueOf(amount))
                .addQueryParameter("minmumInterest", String.valueOf(minimumInterest))
                .addQueryParameter("minmumTotalYaz", String.valueOf(minimumTotalYazTime))
                .addQueryParameter("customerName", customerName)
                .addQueryParameter("numOfOpenLoans", String.valueOf(numOfOpenLoans))
                .build()
                .toString();
        //Request
        Request request = new Request.Builder()
                .url(finalUrlInformation)
                .post(body)
                .build();



        HttpClientUtil.runAsync(request,false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());
                } else{

                    Platform.runLater(() -> {
                        try {
                            List<LoanDTO> loanDTOS = null;
                            String jsonArrayOfInformation = response.body().string();
                            loanDTOS = GSON_INSTANCE.fromJson(jsonArrayOfInformation, new TypeToken<List<LoanDTO>>(){}.getType());
                            mainController.filterdLoans(loanDTOS);
                            filteredLoansController.setLoansValues(loanDTOS);
                            response.body().close();
                            openTextDialogText(LOAD_OK);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
        return loansInfo[0];
    }

    //SET FOR SERVER:
    public void loadingWithdrawalMoney(String name,char type,int amount) throws Exception {//4

        String finalUrlInformation = HttpUrl.parse(LOADING_WITHDRAW_MONEY_PAGE)
                .newBuilder()
                .addQueryParameter("name", name)
                .addQueryParameter("type", String.valueOf(type))
                .addQueryParameter("amount", String.valueOf(amount))
                .build()
                .toString();


        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());

                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }

                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        try {
                            errorMessageProperty.set("Something went wrong: " + response.body().string());
                            try {
                                openTextDialogText(String.valueOf(errorMessageProperty));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                } else{
                    Platform.runLater(() -> {

                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }


    private void initEngineFromXmlFile(String pathToXml,File selectedFile,String nameFromLogin) throws Exception {//1
        File f = new File(pathToXml);
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("file",f.getName(),RequestBody.create(f,MediaType.parse("text/xml"))).build();

        //Request
        Request request = new Request.Builder()
                .url(LOADING_XML_FILE_PAGE)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request,false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
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
                        try {
                            errorMessageProperty.set("Something went wrong: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {
                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void sendAmountFromPayToApi(String loanId, float amount, boolean isPayAllPayments) {
        //this.api.payPayments(loanId,amount,isPayAllPayments);
        String finalUrlInformation = HttpUrl.parse(PAY_PAYMENT_PAGE)
                .newBuilder()
                .addQueryParameter("loanId", loanId)
                .addQueryParameter("amount", String.valueOf(amount))
                .addQueryParameter("isPayAllPayments", String.valueOf(isPayAllPayments))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {
                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void createNewLoan(String id, String loginName, String category, int capital, float interest, int totalYazTime, int paymentRate) {
        //this.api.createNewLoan(id,loginName,category,capital,interest,totalYazTime,paymentRate);
        //finalUrl
        String finalUrlInformation = HttpUrl.parse(CREATE_NEW_LOAN_PAGE)
                .newBuilder()
                .addQueryParameter("id", id)
                .addQueryParameter("loginName", loginName)
                .addQueryParameter("category", category)
                .addQueryParameter("capital", String.valueOf(capital))
                .addQueryParameter("interest", String.valueOf(interest))
                .addQueryParameter("totalYazTime", String.valueOf(totalYazTime))
                .addQueryParameter("paymentRate", String.valueOf(paymentRate))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
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
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {

                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }

    public void buySellLoans(String loanId,String loginName,String sellerName,boolean isForSell) {
        //Gson

        //finalUrl
        String finalUrlInformation = HttpUrl.parse(BUY_SELL_LOAN)
                .newBuilder()
                .addQueryParameter("id", loanId)
                .addQueryParameter("sellername", sellerName)
                .addQueryParameter("isForSell", String.valueOf(isForSell))
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrlInformation, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() ->{
                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    /*try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
                    }*/

                } );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    Platform.runLater(()->{
                        errorMessageProperty.set("Something went wrong: " + response.message());
                        try {
                            openTextDialogText(String.valueOf(errorMessageProperty));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.body().close();
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {
                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }

    public void scheduling(Set<String> selectedLoansId,String customerName
            ,int amount,int percentLoan) throws Exception//6
    {
        //api.scheduling(selectedLoansId,customerName,amount,percentLoan);
        //Gson
        Gson gson = new Gson();
        String json = gson.toJson(selectedLoansId);
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json"));
        //finalUrl
        String finalUrlInformation = HttpUrl.parse(SCHEDULING_PAGE)
                .newBuilder()
                .addQueryParameter("amount", String.valueOf(amount))
                .addQueryParameter("percentLoan", String.valueOf(percentLoan))
                .build()
                .toString();
        //Request
        Request request = new Request.Builder()
                .url(finalUrlInformation)
                .post(body)
                .build();

        HttpClientUtil.runAsync(request,false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("call = " + call + ", e = " + e);
                Platform.runLater(() -> {

                    errorMessageProperty.set("Something went wrong: " + e.getMessage());
                    try {
                        openTextDialogText(String.valueOf(errorMessageProperty));
                    } catch (Exception xe) {
                        xe.printStackTrace();
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    //System.out.println(response.body());//TODO
                } else{
                    Platform.runLater(() -> {
                        response.body().close();
                        try {
                            openTextDialogText(LOAD_OK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });
    }





    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        mainController.close();
    }

    public String getLoginName() {
        return loginName;
    }

    private void openTextDialogText(String msg) throws Exception {
        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Error dialog");
        textInputDialog.setContentText(msg);
        textInputDialog.showAndWait();

    }
    public void setLoginName(String name){this.loginName=name;}
    public static void main(String args[]){
        launch(args);
    }
}





