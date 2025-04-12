package controller;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "registerServlet", value = "/registerServlet")
@MultipartConfig(
        fileSizeThreshold = 10124*1024,
        maxFileSize = 1024*1024*50,
        maxRequestSize = 1024*1024*50
)
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        byte[] picture = null;
        Part picturePart = request.getPart("picture");

        try(InputStream is = picturePart.getInputStream()) {
            picture = new byte[(int) picturePart.getSize()];
            is.read(picture);

        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        User user = new User(name, email, password, picture);
        int errorStatus = UserDAO.registerUser(user);
    }
}