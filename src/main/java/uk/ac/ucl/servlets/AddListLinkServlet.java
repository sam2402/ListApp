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

@WebServlet("/addListLink.html")
public class AddListLinkServlet extends HttpServlet {

    // Add a list link to a list
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        UUID listId = ((List) request.getSession().getAttribute("underlyingList")).getId();
        UUID chosenListId = UUID.fromString(request.getParameter("chosenListId")); // The id of the list being linked to
        model.addListLinkToList(listId, chosenListId);
        request.setAttribute("lists", model.getLists());
        request.getSession().setAttribute("searchListText", null); // Cancel any current search
        request.getSession().setAttribute("currentList", model.getList(listId));

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

}
