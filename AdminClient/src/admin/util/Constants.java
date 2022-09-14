package admin.util;

import com.google.gson.Gson;

public class Constants {

    // fxml locations
    public final static String MAIN_CHAT_ROOM_FXML_RESOURCE_LOCATION = "/admin/components/chatroom/chat-app-main.fxml";
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/admin/components/login/login.fxml";
    public final static String CHAT_ROOM_FXML_RESOURCE_LOCATION = "/admin/components/chatroom/chat-room-main.fxml";
    public final static String STATUS_PAGE_FXML_RESOURCE_LOCATION = "/admin/components/status/status.fxml";

    public final static String MAIN_PAGE_FXML_LOCATION="/admin/components/main/mainView.fxml";
    public final static String ADMIN_PAGE_FXML_LOCATION = "/gui/Admin/adminView.fxml";
    public final static String ANIMATION2_PAGE_FXML_LOCATION = "/gui/LoansTables/animation2.fxml";
    public static final String BUY_SELL_FXML_LOCATION = "/gui/LoansTables/buySellLoan.fxml";;
    public static final String NEW_LOAN_FXML_LOCATION ="/gui/LoansTables/newLoan.fxml" ;

    // global constants
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_war";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    //SERVER SET:
    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String INCREASE_YAZ_PAGE = FULL_SERVER_PATH + "/increaseYaz";
    public final static String DECREASE_YAZ_PAGE = FULL_SERVER_PATH + "/decreaseYaz";


    //SERVER GET:
    public final static String GET_CURRENT_YAZ_PAGE = FULL_SERVER_PATH + "/getCurrentYaz";
    public final static String GET_ALL_LOANS_PAGE = FULL_SERVER_PATH + "/getAllLoans";
    public final static String GET_ALL_CUSTOMERS_PAGE = FULL_SERVER_PATH + "/getAllCustomers";

    //CHAT
    public final static String USERS_LIST = FULL_SERVER_PATH + "/userslist";
    public final static String LOGOUT = FULL_SERVER_PATH + "/chat/logout";
    public final static String SEND_CHAT_LINE = FULL_SERVER_PATH + "/pages/chatroom/sendChat";
    public final static String CHAT_LINES_LIST = FULL_SERVER_PATH + "/chat";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
