package user.main;

import user.main.GuiMain;

import java.util.TimerTask;

public class userRefresher extends TimerTask {
    private GuiMain main;

    public userRefresher(GuiMain guiMain) {
        this.main=guiMain;
    }


    @Override
    public void run() {
        this.main.updateCurrentYaz();
        this.main.getAllcategroiesNames();
        this.main.getLoansInfo();
        this.main.getBuyLoans();
        this.main.getCustomersInfo();
    }
}
