package gui.LoansTables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import loans.payments.PaymentsPerCustomerDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LendersController implements Initializable {
    public class lendersMapClass{
        private String name;
        private int amount;

        public lendersMapClass(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }
    }
    
    @FXML
    private TableView<lendersMapClass> lendersTable;

    @FXML
    private TableColumn<lendersMapClass, Integer> lenderInvestment;

    @FXML
    private TableColumn<lendersMapClass, String> lenderName;
    
    private Stage stage;
    private Scene scene;

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

         lenderInvestment.setCellValueFactory(new PropertyValueFactory<>("amount"));
         lenderName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void setLendersValues(Map<String, PaymentsPerCustomerDto> lendersMap){
        List<lendersMapClass> lendersMapList=new ArrayList<>();
        lendersMap.forEach((name,payment)->{
            lendersMapClass lenders=new lendersMapClass(name,payment.getCapital());
            lendersMapList.add(lenders);
        });

        ObservableList<lendersMapClass> observableList=FXCollections.observableList(lendersMapList);
        this.lendersTable.getItems().setAll(observableList);

    }

    public void setScene(Scene scene) {
        this.scene=scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage=stage;
    }

}
