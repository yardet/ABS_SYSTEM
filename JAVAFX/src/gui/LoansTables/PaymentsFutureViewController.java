package gui.LoansTables;


import gui.Customers.CustomersController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loans.LoanDTO;
import loans.payments.PaymentDto;
import gui.LoansTables.SpecialLoanDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentsFutureViewController extends PaymentsController {

    @FXML
    private TableColumn<SpecialLoanDTO, String> loanId;

    @FXML
    private TableColumn<SpecialLoanDTO, Button> payButton;

    private PaymentPayController paymentController;

    private CustomersController customer;

    private boolean paymentsStageExist;
    private int currYaz;
    private boolean isRewind;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        isRewind=false;

        loanId.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        payButton.setCellValueFactory(new PropertyValueFactory<>("pay"));
        initPayButtonFxml();
    }
    public void setPayments(int currYaz,List<PaymentDto> paymentDtoList){
        this.currYaz=currYaz;
        super.setPayments(paymentDtoList);
        setPayButton();
    }

    private void initPayButtonFxml() {
        FXMLLoader paymentLoader=new FXMLLoader();
        URL paymentFXML=getClass().getResource("/gui/LoansTables/paymentPayView.fxml");
        paymentLoader.setLocation(paymentFXML);
        Scene scene;
        Stage stage=new Stage();
        try{
            scene=new Scene(paymentLoader.load());
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        this.paymentController=paymentLoader.getController();
        this.paymentController.setPaymentFutureController(this);
        stage.setScene(scene);
        this.paymentController.setScene(scene);
        this.paymentController.setStage(stage);
    }
   private void setPayButton() {
        List<specialPaymentDto> payments=new ArrayList<>();
       List<specialPaymentDto> allPayments=this.paymentsTable.getItems();

       allPayments.stream().map(paymentDto -> paymentDto.getLoanId())
               .collect(Collectors.toSet()).stream().forEach(loanId->{
               payments.add(allPayments.stream().filter(paymentDto ->
                       paymentDto.getLoanId().equals(loanId)).findFirst().get());
               });



       this.paymentsTable.getItems().forEach(paymentDto -> {
           Button paymentsButton = paymentDto.getPay();
           if(!payments.contains(paymentDto)){
               paymentsButton.setVisible(false);
               return;
           }
           paymentsButton.setText("Pay");
           if(isRewind)
               paymentsButton.setDisable(true);


           paymentsButton.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent actionEvent) {

                   if (!paymentsStageExist) {
                       paymentsStageExist = true;
                       paymentController.getStage().initModality(Modality.WINDOW_MODAL);
                   }
                   paymentController.setPaymentsData(paymentDto.getLoanId(),paymentDto.getTotal()
                           ,customer.getLeftToPayPaymentsPerLoan
                                   (paymentDto.getLoanId()),customer.getBalance());
                       paymentController.getStage().show();

               }
           });
       });
   }
   public void setCustomerController(CustomersController customersController){
        this.customer=customersController;
    }
    public void sendAmountFromPayToApi(String loanId,float amount,boolean isPayAllPayments){
        this.customer.sendAmountFromPayToApi(loanId,amount,isPayAllPayments);
    }


    public void validityTextBoxFloat(TextField textbox) {
        this.customer.validityTextBoxFloat(textbox);
    }

    public void updateInformation() {
        try {
            this.customer.updateInformationData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void freeButton() {
        this.isRewind=false;
    }

    public void blockButton() {
        this.isRewind=true;
    }
}
