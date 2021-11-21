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

@WebServlet("/editItem.html")
public class EditItemServlet extends HttpServlet {

    /// Edit an item
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        UUID listId = ((List) request.getSession().getAttribute("underlyingList")).getId();
        String newItemText = request.getParameter("newText");
        UUID itemId = UUID.fromString(request.getParameter("itemId"));
        model.editItem(listId, itemId, newItemText);

        // If the user is currently searching the list, re-perform that search
        if (request.getSession().getAttribute("searchListText") != null) {
            List searchedList = model.searchList(listId, (String) request.getSession().getAttribute("searchListText"));
            request.getSession().setAttribute("currentList", searchedList);
        }
        request.setAttribute("lists", model.getLists());

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

}
