package chat.servlets;

import chat.constants.Constants;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import engine.chat.ChatManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import services.main_service.Engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

import static javafx.scene.input.KeyCode.REWIND;
@WebServlet(name = "UploadFileServlet", urlPatterns = {"/loadingXmlFile"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadFileServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName=SessionUtils.getUsername(request);
        //Session doesn't exist!
        if (userName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Engine engine = ServletUtils.getABSManager(getServletContext());
        //User isn't customer!
        if (userName.equals("ADMIN")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        //User entered more than 1 file!
        if(parts.size() > 1){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        StringBuilder fileContent = new StringBuilder();
        for (Part part : parts) {
            if(part.getSize() == 0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            fileContent.append(readFromInputStream(part.getInputStream()));
        }
        if(fileContent.equals("")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        InputStream file = new ByteArrayInputStream(fileContent.toString().getBytes(StandardCharsets.UTF_8));
        try {
            engine.loadXmlFile(file, userName);
            out.println("File loaded successfully!");
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
