package gui.LoansTables;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import loans.LoanDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class buyLoansController extends LoansActiveController {

    @FXML
    private TableColumn<SpecialBuyLoanDTO, String> lenderName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loansTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        super.initialize(location, resources);
       // lenderName.setCellValueFactory(new PropertyValueFactory<>("lenderName"));
    }

    public void setLoansValues(List<LoanDTO> loanDTOS, String loginName) {


        super.setLoansValues(loanDTOS);

    }
}
