package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.main_service.Engine;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SchedulingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            try {
                //gson.fromJson(request.)
                Type setType = new TypeToken<HashSet<String>>(){}.getType();
                Set<String> set =gson.fromJson(ServletUtils.getBody(request), setType);


                int amount=Integer.parseInt(request.getParameter("amount"));
                int percentLoan=Integer.parseInt(request.getParameter("percentLoan"));
                engine.scheduling(set ,SessionUtils.getUsername(request)
                        ,amount
                        , percentLoan);
                response.setStatus(HttpServletResponse.SC_OK);


            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(e.toString());
                out.flush();
            }

        }
    }



}
