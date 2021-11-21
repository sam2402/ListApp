package uk.ac.ucl.servlets;

import uk.ac.ucl.model.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/addItem.html")
public class AddItemServlet extends HttpServlet {

    // Add an item to the list
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        UUID listId = ((List) request.getSession().getAttribute("underlyingList")).getId();
        String text = request.getParameter("text");
        String itemTypeText = request.getParameter("itemType");
        model.addItem(listId, text, itemTypeText);
        request.setAttribute("lists", model.getLists());
        request.getSession().setAttribute("currentList", model.getList(listId)); // Set the current list to being the list being viewed
        request.getSession().setAttribute("searchListText", null); // Cancel any current search

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

}
