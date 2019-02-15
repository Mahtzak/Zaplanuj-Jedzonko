package pl.coderslab.web;

import pl.coderslab.dao.PlanDao;
import pl.coderslab.dao.RecipeDao;
import pl.coderslab.model.Plan;
import pl.coderslab.model.Recipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AppPlanDetails", urlPatterns = "/app/plan/details")
public class AppPlanDetails extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt( request.getParameter( "planIdDetails" ) );
        System.out.println( "ID plan to SHOW DETAILS is: " + id );
        Plan plan = new Plan(  );
        plan = PlanDao.read( id );
        System.out.println(plan.toString());

        HttpSession session = request.getSession();
        session.setAttribute( "name", plan.getName() );
        session.setAttribute( "description", plan.getDescription() );



        response.sendRedirect( "/appPlanDetails.jsp" );
    }
}
