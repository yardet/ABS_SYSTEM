package gui.LoansTables;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class animation2Controller implements Initializable {

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
    private Polygon p4;


    @FXML
    private Button playAnimationB;

    @FXML
    private ScrollPane scrollPane;
    private TranslateTransition translateTransitionPolygon;
    private RotateTransition rtC1Transition;
    private RotateTransition rtC2Transition;
    private RotateTransition rtC3Transition;
    private boolean isStopButton=true;

    @FXML
    void playAnimation(ActionEvent event) {
        if(isStopButton)
        {
            pause();
            isStopButton=false;
            playAnimationB.setText("play");
        }
        else{
            setAnimation(2,false);
            playAnimationB.setText("stop");
            isStopButton=true;
        }

    }

    public void pause() {
        rtC1Transition.stop();
        rtC2Transition.stop();
        rtC3Transition.stop();
        translateTransitionPolygon.stop();
    }

    private void setRotate(Circle c,boolean reverse,int angle,int duration,RotateTransition rt){
        rt.setAutoReverse(reverse);
        rt.setByAngle(angle);
        rt.setDelay(Duration.seconds(0));
        rt.setRate(3);
        rt.setCycleCount(18);
        rt.play();
    }

    private void setPolygon(int duration){
        p4.setFill(Color.AQUAMARINE);
        p4.relocate(20,20);
        this.translateTransitionPolygon.setDuration(Duration.seconds(duration));
        this.translateTransitionPolygon.setToX(200);
        this.translateTransitionPolygon.setToY(30);
        this.translateTransitionPolygon.setAutoReverse(true);
        this.translateTransitionPolygon.setCycleCount(Animation.INDEFINITE);
        this.translateTransitionPolygon.setNode(p4);
        this.translateTransitionPolygon.play();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rtC1Transition=new RotateTransition(Duration.seconds(10),c1);
        this.rtC2Transition=new RotateTransition(Duration.seconds(18),c2);
        this.rtC3Transition=new RotateTransition(Duration.seconds(24),c3);
        TranslateTransition translateTransition=new TranslateTransition();
        this.translateTransitionPolygon=translateTransition;
        setVisableFalseAnimation();
    }
    public void setAnimation(int duration,boolean withTimer){
        playAnimationB.setDisable(false);
        c1.setVisible(true);
        c2.setVisible(true);
        c3.setVisible(true);
        p4.setVisible(true);
        setRotate(c1,true,360,10,this.rtC1Transition);
        setRotate(c2,true,180,18,this.rtC2Transition);
        setRotate(c3,true,145,24,this.rtC3Transition);
        setPolygon(duration);
        stage.show();
        if(withTimer){
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished( event -> stage.close() );
            delay.play();

        }
    }
    public void setVisableFalseAnimation(){
        c1.setVisible(false);
        c2.setVisible(false);
        c3.setVisible(false);
        p4.setVisible(false);
        playAnimationB.setDisable(true);
    }
    public void setStage(Stage stage){
        this.stage=stage;
    }
    public void setTimer(long duration){
        long mTime = System.currentTimeMillis();
        long end = mTime + duration;

        while (System.currentTimeMillis() > end)
        {
            stage.close();
        }
    }
}
