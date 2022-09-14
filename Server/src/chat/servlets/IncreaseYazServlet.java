package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import customers.CustomerDTO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class IncreaseYazServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            try {
                String userName = SessionUtils.getUsername(request);
                if(userName!=null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    int current_yaz = engine.getCurrentYaz();
                    if (ServletUtils.isRewind(getServletContext(), current_yaz))
                        ServletUtils.updateEngineToAnotherYazEngine(getServletContext(), current_yaz + 1);
                    else
                        ServletUtils.addNewEngineAndSaveOldEngine(getServletContext(), current_yaz);
                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.getMessage());
                out.flush();
            }

        }
    }



}
