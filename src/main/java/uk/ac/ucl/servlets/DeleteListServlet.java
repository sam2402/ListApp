package uk.ac.ucl.servlets;

import uk.ac.ucl.model.List;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/deleteList.html")
public class DeleteListServlet extends HttpServlet {

    // Delete a list
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        UUID id = UUID.fromString(request.getParameter("listId"));
        model.deleteList(id);
        List underlyingList = (List) request.getSession().getAttribute("underlyingList");
        if (underlyingList.getId().equals(id)) { // if the user was viewing the deleted list, stop viewing it
            request.getSession().setAttribute("underlyingList", null);
            request.getSession().setAttribute("currentList", null);
        }
        request.setAttribute("lists", model.getLists());

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }
}
