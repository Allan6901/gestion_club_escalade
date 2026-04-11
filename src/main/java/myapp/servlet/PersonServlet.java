package myapp.servlet;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myapp.jpa.dao.JpaDao;

/**
 * Une servlet pour les actions sur les personnes.
 */
@WebServlet(//
        description = "Les actions sur les personnes", //
        urlPatterns = { "/person" })
public class PersonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private InMemoryPersonService manager=new InMemoryPersonService();

    /**
     * Requêtes GET
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String id = request.getParameter("id");
        String ajout = request.getParameter("ajout");

        if ("1".equals(ajout)) {
            request.setAttribute("person", new Person());
            request.getSession().removeAttribute("originalId");

            request.getRequestDispatcher("edition.jsp").forward(request, response);
            return;
        }

        if (id != null && !id.isEmpty()) {
            var person = manager.find(id);
            if (person != null) {
                request.setAttribute("person", person);
                request.getSession().setAttribute("originalId", id);

                request.getRequestDispatcher("edition.jsp").forward(request, response);
                return;
            }
        }

        request.setAttribute("persons", manager.findAll());
        request.getRequestDispatcher("lister.jsp").forward(request, response);
    }

    /**
     * Requêtes POST (la même chose que GET)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idSaisi = request.getParameter("id");
        String name = request.getParameter("name");
        String mail = request.getParameter("mail");

        String originalId = (String) request.getSession().getAttribute("originalId");

        String finalId;
        if (originalId != null) {

            finalId = originalId;
        } else {
            finalId = idSaisi;
        }

        Person p = new Person(finalId, name, mail);

        Map<String, String> errors = manager.validate(p);

        if (errors == null || errors.isEmpty()) {
            manager.save(p);

            request.getSession().removeAttribute("originalId");

            response.sendRedirect("person");
        } else {

            request.setAttribute("errors", errors);
            request.setAttribute("person", p);

            request.getRequestDispatcher("edition.jsp").forward(request, response);
        }
    }

}