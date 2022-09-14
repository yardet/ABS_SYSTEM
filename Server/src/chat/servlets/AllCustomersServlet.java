package chat.servlets;

import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;
import customers.CustomerDTO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.main_service.Engine;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class    AllCustomersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        String userName = SessionUtils.getUsername(request);
        List<CustomerDTO> customersList;
        String json;
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            if(userName!=null) {
                customersList = engine.getAllCustomersInfo();
                if(!userName.equals("ADMIN")) {
                    customersList = customersList.stream().filter(customer->
                            customer.getName().equals(userName)).collect(Collectors.toList());
                    json = gson.toJson(customersList.get(0));

                }
                else
                    json = gson.toJson(customersList);
                out.println(json);
                out.flush();
            }
        }
    }

}
