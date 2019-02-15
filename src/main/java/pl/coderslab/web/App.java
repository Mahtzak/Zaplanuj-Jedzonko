package pl.coderslab.web;

import pl.coderslab.model.Admins;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static pl.coderslab.dao.PlanDao.howManyPlans;
import static pl.coderslab.dao.PlanDao.lastPlan;
import static pl.coderslab.dao.RecipeDao.recipeCount;

@WebServlet(name = "App", urlPatterns = "/app")
public class App extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.setAttribute( "recipes", recipeCount( (int) session.getAttribute( "id" ) ) );
        session.setAttribute( "plans", howManyPlans( (int) session.getAttribute( "id" ) ) );
        session.setAttribute( "lastPlan", lastPlan( (int) session.getAttribute( "id" ) ) );

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost( request, response );
        response.sendRedirect( "/appDashboard.jsp" );
    }
}
