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

public class DecreaseYazServlet extends HttpServlet {

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

                    ServletUtils.updateEngineToAnotherYazEngine(getServletContext(), current_yaz - 1);

                }

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.getMessage());
                out.flush();
            }

        }
    }



}
