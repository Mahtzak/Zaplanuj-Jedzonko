package pl.coderslab.web;

import pl.coderslab.dao.AdminsDao;
import pl.coderslab.model.Admins;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import static pl.coderslab.dao.PlanDao.howManyPlans;
import static pl.coderslab.dao.RecipeDao.recipeCount;
import static pl.coderslab.utils.HashFunctions.createSalt;
import static pl.coderslab.utils.HashFunctions.generateHash;

@WebServlet(name = "Register", urlPatterns = "/register")
public class Register extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter( "name" );
        String surname = request.getParameter( "surname" );
        String email = request.getParameter( "email" );
        byte[] saltToHash = createSalt();
        //System.out.println(saltToHash);
        //String salt = Arrays.toString( saltToHash );
        String salt = DatatypeConverter.printHexBinary( saltToHash ).toLowerCase();
        //System.out.println(salt);
        String password = generateHash(request.getParameter( "password" ),"SHA-256", saltToHash);
        String password2 = generateHash(request.getParameter( "password2" ),"SHA-256", saltToHash);
        if (!password.equals( password2 )) {
            request.setAttribute("errorMessage", "Given passwords do not match dude! What were you thinking?!");
            RequestDispatcher dispatcher =  request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request, response);
            response.sendRedirect( "/registration.jsp" );
        }


        ListIterator<Admins> listIterator = null;
        List<Admins> adminsList = new AdminsDao().findAll();
        listIterator = adminsList.listIterator();

        boolean check = true;


        while (listIterator.hasNext()) {
            if
            (listIterator.next().getEmail().equals( email )) {
                check = false;
                request.setAttribute("errorMessage", "User with this email already exists.");
                RequestDispatcher dispatcher =  request.getRequestDispatcher("registration.jsp");
                dispatcher.forward(request, response);
                response.sendRedirect( "/registration.jsp" );
                break;
            }
        }

        if (check == true) {
            Admins user = new Admins();
            user.setEmail( email );
            user.setFirstName( name );
            user.setLastName( surname );
            user.setPassword( password );
            user.setEnable( 1 );
            user.setSuperadmin( 0 );
            user.setSalt(salt);
            AdminsDao newUser = new AdminsDao();
            newUser.create( user );


            HttpSession session = request.getSession();
            // zdublowanie kodu z login, aby nie musiec sie logowac tylko od razu przeniesc na /app
            session.setAttribute( "logined", true );
            session.setAttribute( "firstName", user.getFirstName() );
            session.setAttribute( "lastName", user.getLastName() );
            session.setAttribute( "email", user.getEmail() );
            session.setAttribute( "id", user.getId() );
            int id = (Integer) session.getAttribute( "id" );
            session.setAttribute( "plans", howManyPlans( (int) session.getAttribute( "id" ) ) );
            session.setAttribute( "recipes", recipeCount( (int) session.getAttribute( "id" ) ) );


            response.sendRedirect( "/app" );
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect( "/registration.jsp" );

    }
}
