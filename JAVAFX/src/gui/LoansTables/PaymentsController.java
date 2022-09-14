package gui.LoansTables;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import loans.payments.PaymentDto;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentsController implements Initializable {

    private Scene scene;
    private Stage stage;

    @FXML
    private TableColumn<specialPaymentDto, Integer> capital;

    @FXML
    private TableColumn<specialPaymentDto, Float> interest;

    @FXML
    protected TableView<specialPaymentDto> paymentsTable;

    @FXML
    private TableColumn<specialPaymentDto, Float> total;

    @FXML
    private TableColumn<specialPaymentDto, Integer> yaz;

    @FXML
    private TextField sumText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        capital.setCellValueFactory(new PropertyValueFactory<specialPaymentDto,Integer>("capital"));
        yaz.setCellValueFactory(new PropertyValueFactory<specialPaymentDto,Integer>("Yaz"));
        total.setCellValueFactory(new PropertyValueFactory<specialPaymentDto,Float>("total"));
        interest.setCellValueFactory(new PropertyValueFactory<specialPaymentDto,Float>("interest"));
    }
    public void setPayments(List<PaymentDto> paymentDtoList){
       /* paymentDtoList=paymentDtoList.stream()
         .sorted((Comparator<? super PaymentDto>) yaz)
         .collect(Collectors.toList());//TODO SORTED BY YAZ*/
        List<specialPaymentDto> specialPaymentDtos;
        final float[] sum = {0};
        paymentDtoList.stream().forEach(paymentDto -> {
            sum[0] +=paymentDto.getTotal();
        });
        sumText.setText("Sum:"+String.valueOf(sum[0]));

        specialPaymentDtos=paymentDtoList.stream().map(t->new specialPaymentDto(t)).collect(Collectors.toList());
        ObservableList<specialPaymentDto> paymentsObservable = FXCollections.observableList(specialPaymentDtos);
        paymentsTable.getItems().setAll(paymentsObservable);
    }
    public void setScene() {
        this.stage=new Stage();
        this.scene=this.stage.getScene();
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
