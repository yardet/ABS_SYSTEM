package gui.LoansTables;

import gui.Admin.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import loans.LoanDTO;
import services.loans_service.Loan_status;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class LoansNewController implements Initializable {

    AdminController adminController;


    @FXML
    protected TableView<SpecialLoanDTO> loansTable;

    @FXML
    private TableColumn<SpecialLoanDTO, Integer> loanCapital;

    @FXML
    private TableColumn<SpecialLoanDTO, String> loanCategory;

    @FXML
    private TableColumn<SpecialLoanDTO, Integer> loanDuration;

    @FXML
    private TableColumn<SpecialLoanDTO, String> loanId;

    @FXML
    private TableColumn<SpecialLoanDTO, Float> loanInterest;

    @FXML
    private TableColumn<SpecialLoanDTO, String> loanOwner;

    @FXML
    private TableColumn<SpecialLoanDTO, Integer> loanRate;

    @FXML
    private TableColumn<SpecialLoanDTO, Loan_status> loanStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loanCapital.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Integer>("Capital"));
        loanDuration.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Integer>("totalYaz"));
        loanRate.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Integer>("paymentRate"));
        loanInterest.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Float>("interestPerPayment"));
        loanId.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,String>("id"));
        loanOwner.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,String>("owner"));
        loanCategory.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,String>("category"));
        loanStatus.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Loan_status>("status"));
    }

    public void setLoansValues(List<LoanDTO> loanDTOS){
        if(loanDTOS!=null)
        {
            ObservableList<SpecialLoanDTO> loanDTOObservableList = FXCollections.observableList(loanDTOS.stream().map(t->new SpecialLoanDTO(t)).collect(Collectors.toList()));
            loansTable.getItems().setAll(loanDTOObservableList);
        }
    }
    public void setAdminController(AdminController adminController){this.adminController=adminController;}
}
