package gui.LoansTables;

import gui.Customers.CustomersController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class newLoanController implements Initializable {

    @FXML
    private TextField totalYazTimeText;

    @FXML
    private TextField capitalText;

    @FXML
    private MenuButton categoriesSelection1;

    @FXML
    private TextField categoryText;

    @FXML
    private Button createSubmitButton;

    @FXML
    private TextField interestText;

    @FXML
    private TextField loanNameText;

    @FXML
    private TextField paysEveryYazText;

    @FXML
    private GridPane grid;

    private CustomersController customerController;

    @FXML
    void submitFunc(ActionEvent event) {

        String id=this.loanNameText.getText();
        String category=this.categoryText.getText();
        int capital=Integer.parseInt(this.capitalText.getText());
        int paymentRate=Integer.parseInt(this.paysEveryYazText.getText());
        int totalYazTime=Integer.parseInt(this.totalYazTimeText.getText());
        float interest=Float.parseFloat(this.interestText.getText());
        this.customerController.createNewLoan(id,category,capital,interest,totalYazTime,paymentRate);
        init();

    }

    public void setCustomerController(CustomersController customersController) {
        this.customerController=customersController;
    }

    private void init(){
        this.capitalText.setText("");
        this.interestText.setText("");
        this.categoryText.setText("");
        this.loanNameText.setText("");
        this.paysEveryYazText.setText("");
        this.totalYazTimeText.setText("");

        makeTextBoxesValid();

    }

    private void makeTextBoxesValid() {
        validityTextBoxInt(totalYazTimeText);
        validityTextBoxInt(paysEveryYazText);
        validityTextBoxInt(capitalText);
        validityTextBoxInt(interestText);
    }

    private void validityTextBoxInt(TextField textbox){
        //amount text
        textbox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textbox.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
        makeTextBoxesValid();
        //this.createSubmitButton.setDisable(true);
    }

    public void setDisableFalse(){this.createSubmitButton.setDisable(false);}

    public void blockButton() {
        this.createSubmitButton.setDisable(true);
    }

    public void freeButton() {
        this.createSubmitButton.setDisable(false);
    }
}
