package gui.LoansTables;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class animation1Controller implements Initializable {

    private Stage stage;
    @FXML
    private Button playAnimation;

    @FXML
    private Circle c1;

    @FXML
    private Circle c2;

    @FXML
    private Circle c3;

    @FXML
    void playAnimation(ActionEvent event) {
        setRotate(c1,true,360,1);
        setRotate(c2,true,180,2);
        setRotate(c3,true,145,3);
        playAnimation.setText("STOP");
        playAnimation.setOnAction(event1 -> {
            stage.close();
        });

    }
    private void setRotate(Circle c,boolean reverse,int angle,int duration){
        RotateTransition rt=new RotateTransition(Duration.seconds(duration),c);
        rt.setAutoReverse(reverse);
        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(3);
        rt.setCycleCount(18);
        rt.play();
        c.setOnMouseClicked(event -> {
            rt.stop();
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        c1.setFill(Color.AQUAMARINE);
        c1.setRadius(50);
        c1.relocate(50,50);
        TranslateTransition translateTransition=new TranslateTransition();
        translateTransition.setDuration(Duration.seconds(2));
        translateTransition.setToX(20);
        translateTransition.setToY(200);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(Animation.INDEFINITE);
        translateTransition.setNode(c1);
        translateTransition.play();

    }
    public void setStage(Stage stage){
        this.stage=stage;
    }
}
