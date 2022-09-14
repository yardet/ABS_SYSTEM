package chat.servlets;

import chat.utils.ServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import loans.LoanDTO;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CurrentYAZServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            String json;
            if(ServletUtils.isRewind(getServletContext(), engine.getCurrentYaz()))
                json = gson.toJson(String.format("%d %c", engine.getCurrentYaz(), 't'));
            else
                json = gson.toJson(String.format("%d %c", engine.getCurrentYaz(), 'f'));

            out.println(json);
            out.flush();
        }
    }

}
