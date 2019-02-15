package pl.coderslab.web;

import pl.coderslab.dao.AdminsDao;
import pl.coderslab.dao.PlanDao;
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
import static pl.coderslab.dao.PlanDao.lastPlan;
import static pl.coderslab.dao.RecipeDao.recipeCount;
import static pl.coderslab.utils.HashFunctions.generateHash;

@WebServlet(name = "Login", urlPatterns = "/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter( "email" );
        String password = request.getParameter( "password" );
        List<Admins> adminsList = new AdminsDao().findAll();
        ListIterator<Admins> listIterator = adminsList.listIterator();

        while (listIterator.hasNext()) {
            Admins user = listIterator.next();
            user.getSalt();
            byte[] saltToHash = DatatypeConverter.parseHexBinary( user.getSalt() );  //ma przerobic Stringa na byte[]

            if ((email.equals( "" )) || (password.equals( "" ))) {
                request.setAttribute( "errorMessage", "You do know that your email and password ARE both required in order to login don't you? Give it one more shot!" );
                RequestDispatcher dispatcher = request.getRequestDispatcher( "login.jsp" );
                dispatcher.forward( request, response );
            } else if
            (user.getEmail().equals( email ) && user.getPassword().equals( generateHash( password, "SHA-256", saltToHash ) )) {

                HttpSession session = request.getSession();
               /* if (session.getAttribute( "logined" ) == null) {
                    session.setAttribute( "logined", true );
                } else {*/
                session.setAttribute( "logined", true );
                // luzna propozycja, aby wszystko to zapisac w sesji, pozniej moze sie przydac...
                session.setAttribute( "firstName", user.getFirstName() );
                session.setAttribute( "lastName", user.getLastName() );
                session.setAttribute( "email", user.getEmail() );
                session.setAttribute( "id", user.getId() );
                int id = (Integer) session.getAttribute( "id" );
                System.out.println( "ID z sesji wynosi: " + id );
                session.setAttribute( "plans", howManyPlans( (int) session.getAttribute( "id" ) ) );
                session.setAttribute( "recipes", recipeCount( (int) session.getAttribute( "id" ) ) );
                session.setAttribute( "lastPlan", lastPlan( (int) session.getAttribute( "id" ) ) );
                System.out.println("LAST PLAN TO: " + session.getAttribute( "lastPlan" ));


                System.out.println( howManyPlans( (int) session.getAttribute( "id" ) ) );
                System.out.println( recipeCount( (int) session.getAttribute( "id" ) ) );
                System.out.println( "UDALO SIE ZALOGOWAC" );
                response.sendRedirect( "/app" );    //po zalogowaniu odsyla do servletu App pod adresem url"/app"

            } else if ((user.getEmail().equals( email ) && !user.getPassword().equals( generateHash( password, "SHA-256", saltToHash ) ))) {
                request.setAttribute( "errorMessage", "The password you were kind enough to provide us with is invalid! What were you thinking?!" );
                RequestDispatcher dispatcher = request.getRequestDispatcher( "login.jsp" );
                dispatcher.forward( request, response );
            }

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect( "/login.jsp" );

    }
}
