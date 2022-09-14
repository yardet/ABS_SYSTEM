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

public class buySellLoanServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getABSManager(getServletContext());
            String loginName=SessionUtils.getUsername(request);
            String sellerName=request.getParameter("sellername");
            String loanId=request.getParameter("id");
            boolean isForSell=Boolean.parseBoolean(request.getParameter("isForSell"));
            //Set<CategoryDTO> categoriesInfo = engine.getAllCategoriesInfo();
            //Set<String> categories = categoriesInfo.stream().map(dto->dto.getName()).collect(Collectors.toSet());
            //String json = gson.toJson(categoriesInfo.stream().findFirst().get().getName());
            //out.println(json);
            if(isForSell){
                engine.addLoanToSellList(loginName,loanId);
            }
            else{
                engine.buyLoan(loanId,loginName,sellerName);
            }
            out.flush();
        }
    }

}
