package gui.LoansTables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentPayController implements Initializable {

    @FXML
    private TextField amountText;

    @FXML
    private Label pay;

    @FXML
    private Button payAllPaymentsB;

    @FXML
    private Button payPaymentB;

    @FXML
    private Label sumAllPaymentsL;

    private Scene scene;

    private Stage stage;
    private float currPayAmount;
    private float sumOfAllFuturePayments;
    private float customerBalance;
    private PaymentsFutureViewController paymentsFutureController;
    private String loanId;

    @FXML
    void PayAllPaymentsAction(ActionEvent event) {
        this.sumAllPaymentsL.setText(String.valueOf(sumOfAllFuturePayments));
        float sum=Float.parseFloat(this.sumAllPaymentsL.getText());
        if(this.customerBalance>=sum)
        {
            try{
                this.paymentsFutureController.sendAmountFromPayToApi(this.loanId,sum,true);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        else{
            this.sumAllPaymentsL.setText("not enough money");
        }
        this.paymentsFutureController.updateInformation();
        this.stage.close();


    }

    @FXML
    void paySinglePaymentAction(ActionEvent event) {
        float sum=Float.parseFloat(this.amountText.getText());
        if(this.customerBalance>=sum)
        {
            try{
                this.paymentsFutureController.sendAmountFromPayToApi(this.loanId,sum,false);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        else{
            this.sumAllPaymentsL.setText("not enough money");
        }
        this.paymentsFutureController.updateInformation();
        this.stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //validityTextBoxFloat(this.amountText);//valid
    }

    public void setScene() {
        this.stage=new Stage();
        this.scene=this.stage.getScene();
    }
    public void setScene(Scene scene) {
        this.scene=scene;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage=stage;
    }
    public void setPaymentsData(String loanId,float currPayAmount, float sumOfAllFuturePayments, float customerBalance) {
        this.loanId=loanId;
        this.currPayAmount = currPayAmount;
        this.amountText.setText(String.valueOf(currPayAmount));
        this.sumOfAllFuturePayments = sumOfAllFuturePayments;
        this.sumAllPaymentsL.setText(String.valueOf(sumOfAllFuturePayments));
        this.customerBalance = customerBalance;
    }
    public void setPaymentFutureController(PaymentsFutureViewController paymentsFutureViewController){
        this.paymentsFutureController=paymentsFutureViewController;}

    public void validityTextBoxFloat(TextField textbox) {
        this.paymentsFutureController.validityTextBoxFloat(textbox);
    }
}
