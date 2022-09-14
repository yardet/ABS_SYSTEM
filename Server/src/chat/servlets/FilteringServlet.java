package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loans.LoanDTO;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FilteringServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            try {
                //gson.fromJson(request.)
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> categories = gson.fromJson(ServletUtils.getBody(request), type);

                float amount=Float.parseFloat(request.getParameter("amount"));
                float minimumInterest=Float.parseFloat(request.getParameter("minmumInterest"));
                int minimumTotalYaz=Integer.parseInt(request.getParameter("minmumTotalYaz"));
                int numOfOpenLoans=Integer.parseInt(request.getParameter("numOfOpenLoans"));
                List<LoanDTO> loans = engine.filteringLoansByParameters(amount
                        , categories, minimumInterest
                        , minimumTotalYaz
                        , SessionUtils.getUsername(request)
                        , numOfOpenLoans);

                String json = gson.toJson(loans);
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.toString());
                out.flush();
            }

        }
    }



}
