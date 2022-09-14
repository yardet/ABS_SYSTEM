package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;

public class PayPaymentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            try {

                engine.payPayments(request.getParameter("loanId")
                        , Float.parseFloat(request.getParameter("amount"))
                        , Boolean.parseBoolean(request.getParameter("isPayAllPayments")));

                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.getMessage());
                out.flush();
            }

        }
    }



}
