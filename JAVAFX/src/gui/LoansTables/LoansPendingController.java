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


public class LoansPendingController extends LoansNewController {

    List<Integer> selectedIndex=new ArrayList<>();
    List<Boolean> selectedClicked=new ArrayList<>();

    boolean multipleMode=false;

    private LendersController lendersController;

    private Stage primaryStage;
    private boolean lendersStageExist=false;

    @FXML
    private TableColumn<SpecialLoanDTO, Float> leftToPayLenders;

    @FXML
    private TableColumn<SpecialLoanDTO, Button> lenders;//TODO

    @FXML
    private TableColumn<SpecialLoanDTO, Float> raisedByLedners;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loansTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        super.initialize(location,resources);
        lenders.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO, Button>("lenderButton"));
        raisedByLedners.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Float>("accumulatedByLenders"));
        leftToPayLenders.setCellValueFactory(new PropertyValueFactory<SpecialLoanDTO,Float>("leftToPayByLenders"));

        initLenders();

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

    public void setLoansValues(List<LoanDTO> loanDTOS){
        super.setLoansValues(loanDTOS);
        setLendersFunc();

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

    @FXML
    void Handle() {
        if(multipleMode)
        {
            initalClickListMultiMode();

            int index=loansTable.getSelectionModel().getSelectedIndex();
            boolean flag=false;
            if((selectedClicked.size()<=index || index<0))
                return;
            if(!selectedClicked.isEmpty())
            {
                if(selectedClicked.get(index)==true)
                {
                    selectedClicked.set(index,false);
                    flag=true;
                    loansTable.getSelectionModel().clearSelection(index);
                }

            }
            if(multipleMode)
            {
                selectedIndex.add(index);
                if(!flag)
                    selectedClicked.add(index,true);
                selectedIndex.forEach(i->{
                    if(selectedClicked.get(i))
                        loansTable.getSelectionModel().selectIndices(i);
                });
            }
        }

    }
    public List<LoanDTO> getSelectedLoans(){
        List<LoanDTO> loanDTOS = new ArrayList<>();
        loansTable.getSelectionModel().getSelectedItems().forEach(loan -> {
            loanDTOS.add(new LoanDTO(loan.getId(),loan.getOwner(),
                    loan.getCategory(),loan.getCapital(),
                    loan.getTotalYaz(),loan.getInitalYaz(),
                    loan.getFinalYaz(),loan.getStatus(),
                    loan.getPaymentRate(),loan.getNextPaymentYaz(),
                    loan.getInterestPerPayment(),
                    loan.getLendersMap(),loan.getOwnerPayments(),
                    loan.getLendersNames(),
                    loan.getAccumulatedByLenders(),
                    loan.getLeftToPayByLenders(),
                    loan.getTotalPaidPaymentsAmount(),
                    loan.getTotalAmountLeftToPay(),
                    loan.getCapitalPaidPaymentAmount(),
                    loan.getCapitalLeftToPay(),loan.getInterestPaidPaymentAmount(),loan.getInterestAmountLeftToPay(),loan.getNextPaymentAmountSum()));
        });
        return loanDTOS;
    }
    public void setMultiSelectionMode(){
        loansTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        multipleMode=true;
    }

    private void initalClickListMultiMode(){
        loansTable.getColumns().stream().forEach(l->this.selectedClicked.add(false));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void clearTable() {
        this.loansTable.getItems().clear();
    }
    // public void setAdminController(AdminController adminController){this.adminController=adminController;}
}
