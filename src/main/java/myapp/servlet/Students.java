package myapp.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/students")
public class Students extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        // les étudiants
        Student john = new Student("John", null, 23);
        Student anne = new Student("Anne", john, 25);
        Student amine = new Student("Amine", anne, 27);
        // la liste des étudiants
        List<Student> students = Arrays.asList(john, anne, amine);
        // les étudiants par leur nom
        Map<String, Student> names = new HashMap<>();
        students.forEach((Student s) -> {
            names.put(s.getName(), s);
            request.setAttribute(s.getName(), s);
        });

        request.setAttribute("students", students);
        request.setAttribute("names", names);
        request.getSession().setAttribute("anne", anne);
        // le jour
        request.setAttribute("today", new Date());
        request.getSession().setAttribute("today", "Hier");
        request.getSession().getServletContext()
            .setAttribute("today", "Avant-hier");
        // Préparation d'une donnée "dangereuse"
        String codeInjection = "<b style='color:red;'>Texte</b> <script>console.log('Injection réussie !');</script>";
        request.setAttribute("danger", codeInjection);
        // appel de la page JSP
        request.getRequestDispatcher("/students.jsp")
            .forward(request, response);
    }

}