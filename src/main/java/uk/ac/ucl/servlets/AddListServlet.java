package uk.ac.ucl.servlets;

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

@WebServlet("/addList.html")
public class AddListServlet extends HttpServlet {

    // Create a new list
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        UUID newListId = model.addList(request.getParameter("listNameString"));
        request.setAttribute("lists", model.getLists());

        // Set the list being viewed to be the list just added
        request.getSession().setAttribute("currentList", model.getList(newListId));
        request.getSession().setAttribute("underlyingList", model.getList(newListId));

        // Cancel any searches
        request.getSession().setAttribute("searchListsText", null);
        request.getSession().setAttribute("searchListText", null);

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }
}
