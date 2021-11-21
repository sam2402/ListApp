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
import java.util.ArrayList;
import java.util.UUID;

@WebServlet("/viewList.html")
public class ViewListServlet extends HttpServlet {

    // View a list
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Model model = ModelFactory.getModel();
        UUID id = UUID.fromString(request.getParameter("listId"));
        List list = model.getList(id);
        ArrayList<List> lists = model.getLists();
        request.setAttribute("lists", lists);
        request.getSession().setAttribute("currentList", list);
        request.getSession().setAttribute("underlyingList", list);

        // Invoke the JSP.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/index.jsp");
        dispatch.forward(request, response);
    }
}
