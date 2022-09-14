package gui.LoansTables;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import loans.LoanDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;




public class LoansRiskController extends LoansActiveController {

    @FXML
    private TableColumn<LoanDTO, Button> riskPayments;//TODO

    private PaymentsController paymentsController;
    private boolean paymentsStageExist=false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);

        riskPayments.setCellValueFactory(new PropertyValueFactory<LoanDTO, Button>("riskButton"));

    }
    @Override
    public void setLoansValues(List<LoanDTO> loanDTOS) {
        super.setLoansValues(loanDTOS);

        this.setPaymentsfunc(false);//risk
    }

   // public void setAdminController(AdminController adminController){this.adminController=adminController;}
}
