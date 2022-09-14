package admin.main;

import java.util.TimerTask;

public class AdminRefresher extends TimerTask {
    private GuiMain main;

    public AdminRefresher(GuiMain guiMain) {
        this.main=guiMain;
    }


    @Override
    public void run() {
        this.main.updateCurrentYaz();
        this.main.getAllCustomersInfo();
        this.main.getAllLoansInfo();
        //TODO
    }
}
