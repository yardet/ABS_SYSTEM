package chat.servlets;

import categories.CategoryDTO;
import chat.utils.ServletUtils;
import com.google.gson.Gson;
import engine.users.UserManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.main_service.Engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;

public class AllCategoriesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            //Set<CategoryDTO> categoriesInfo = engine.getAllCategoriesInfo();
            //Set<String> categories = categoriesInfo.stream().map(dto->dto.getName()).collect(Collectors.toSet());
            //String json = gson.toJson(categoriesInfo.stream().findFirst().get().getName());
            //out.println(json);

            String json = gson.toJson(engine.getAllCategoriesInfo());
            out.println(json);
            out.flush();
        }
    }

}
