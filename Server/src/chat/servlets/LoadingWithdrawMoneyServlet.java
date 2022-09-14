package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loans.LoanDTO;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class LoadingWithdrawMoneyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            try {
                engine.loadingWithdrawalMoney(SessionUtils.getUsername(request)
                        , request.getParameter("type").charAt(0), Integer.parseInt(request.getParameter("amount")));
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                //String json = gson.toJson(e.toString());
                out.println(e.toString());
                out.flush();
            }

        }
    }

}
