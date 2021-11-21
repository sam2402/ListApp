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

@WebServlet("/searchList.html")
public class SearchListServlet extends HttpServlet {

    // Search the list
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Model model = ModelFactory.getModel();
        String searchText = request.getParameter("searchString");
        UUID listId = ((List) request.getSession().getAttribute("underlyingList")).getId();
        List searchedList = model.searchList(listId, searchText);
        request.getSession().setAttribute("searchListText", searchText);
        request.getSession().setAttribute("currentList", searchedList);
        request.setAttribute("lists", model.getLists());

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

    // Cancel the search
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Model model = ModelFactory.getModel();
        UUID listId = ((List) request.getSession().getAttribute("underlyingList")).getId();
        List list = model.cancelListSearch(listId);
        request.getSession().setAttribute("currentList", list);
        request.getSession().setAttribute("searchListText", null);
        request.setAttribute("lists", model.getLists());

        // Invoke the JSP.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }

}
