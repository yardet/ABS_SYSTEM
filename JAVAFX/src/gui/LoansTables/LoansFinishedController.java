package gui.LoansTables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loans.LoanDTO;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class LoansFinishedController extends LoansNewController {

    @FXML
    private TableColumn<SpecialLoanDTO, Button> lenders;//TODO

    @FXML
    private TableColumn<SpecialLoanDTO, Button> activePayments;//TODO

    @FXML
    private TableColumn<SpecialLoanDTO, Integer> finalYaz;

    @FXML
    private TableColumn<SpecialLoanDTO, Integer> initalYaz;

    private PaymentsController paymentsController;
    private boolean paymentsStageExist=false;

    private LendersController lendersController;
    private boolean lendersStageExist=false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        lenders.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO, Button>("lenderButton"));
        initLenders();
        activePayments.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO, Button>("paymentsButton"));
        initalYaz.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Integer>("initalYaz"));
        finalYaz.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Integer>("finalYaz"));
        initPayments();
    }

    private void initLenders() {
        FXMLLoader loaderLenders=new FXMLLoader();
        loaderLenders.setLocation(getClass().getResource("/gui/LoansTables/lenders.fxml"));
        Scene scene;
        Stage stage=new Stage();
        try{
            scene=new Scene(loaderLenders.load());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        lendersController=loaderLenders.getController();
        stage.setScene(scene);
        lendersController.setScene(scene);
        lendersController.setStage(stage);
    }

    private void initPayments() {
        FXMLLoader loanerPayments=new FXMLLoader();
        URL paymentsFXML=getClass().getResource("/gui/LoansTables/payments.fxml");
        loanerPayments.setLocation(paymentsFXML);
        Scene scene;
        Stage stage=new Stage();
        //primaryStage.setScene(scene);
        try{
            scene=new Scene(loanerPayments.load());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        paymentsController=loanerPayments.getController();
        stage.setScene(scene);
        paymentsController.setScene(scene);
        paymentsController.setStage(stage);
    }
    public void setLoansValues(List<LoanDTO> loanDTOS){

        super.setLoansValues(loanDTOS);
        setLendersFunc();
        setPaymentsfunc(true);
    }
    private void setLendersFunc(){
        this.loansTable.getItems().forEach(loanDTO -> {
            Button lendersButton=loanDTO.getLenderButton();

            lendersButton.setText("Show");
            lendersButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if(!lendersStageExist){
                        lendersStageExist=true;
                        lendersController.getStage().initModality(Modality.WINDOW_MODAL);
                    }
                    lendersController.setLendersValues(loanDTO.getLendersMap());
                    lendersController.getStage().show();
                }
            });
        });
    }

    private void setPaymentsfunc(boolean b){
        this.loansTable.getItems().forEach(loanDTO -> {
            Button paymentsButton=loanDTO.getActiveButton();

            paymentsButton.setText("Show");
            paymentsButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    if(!paymentsStageExist){
                        paymentsStageExist=true;
                        paymentsController.getStage().initModality(Modality.WINDOW_MODAL);
                    }
                    if(b)
                        paymentsController.setPayments(loanDTO.getOwnerPayments().paidPaymentDtosList());
                    else
                        paymentsController.setPayments(loanDTO.getOwnerPayments().getAllRiskPayments());
                    paymentsController.getStage().show();
                }
            });
        });
    }
   // public void setAdminController(AdminController adminController){this.adminController=adminController;}
}
