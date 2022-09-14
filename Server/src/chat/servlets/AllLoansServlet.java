package chat.servlets;

import chat.utils.ServletUtils;
import com.google.gson.Gson;
import engine.users.UserManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loans.LoanDTO;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AllLoansServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            List<LoanDTO> loansList = engine.getAllLoansInfo();
            String json = gson.toJson(loansList);
            out.println(json);
            out.flush();
        }
    }

}
