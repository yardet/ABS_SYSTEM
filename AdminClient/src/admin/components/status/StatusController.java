package admin.components.status;

import admin.util.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;

public class StatusController {

    private BooleanProperty autoScroll;
    @FXML private TextArea httpStatusTextArea;
    @FXML private CheckBox autoScrollCB;

    public StatusController() {
        autoScroll = new SimpleBooleanProperty();
    }

    @FXML
    public void initialize() {
         autoScroll.bind(autoScrollCB.selectedProperty());
    }

    public void addHttpStatusLine(String line) {
        Platform.runLater(() -> {
            if (autoScroll.get()) {
                httpStatusTextArea.appendText(line + Constants.LINE_SEPARATOR);
                httpStatusTextArea.selectPositionCaret(httpStatusTextArea.getLength());
                httpStatusTextArea.deselect();
            } else {
                int originalCaretPosition = httpStatusTextArea.getCaretPosition();
                httpStatusTextArea.appendText(line + Constants.LINE_SEPARATOR);
                httpStatusTextArea.positionCaret(originalCaretPosition);
            }
        });
    }
}