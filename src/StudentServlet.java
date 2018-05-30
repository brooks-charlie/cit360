import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.*;
import java.util.Iterator;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Extend HttpServlet class
public class StudentServlet extends HttpServlet {

    // Method to handle GET method request.
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SessionFactory factory;
        try {
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Student.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String email = request.getParameter("email");


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Student List";
        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +
                "<body bgcolor = \"#fff\">\n" +
                "<h1 align = \"center\">" + title + "</h1>\n"
        );
        //out.println("/AddStudent");
        //out.println("getRequestURI is: " +request.getRequestURI());
        //out.println("getServletPath is: " +request.getServletPath());
        //out.println("getPathInfo is: " +request.getPathInfo());
        //out.println("getmethod is: " +request.getMethod());
        //out.println("getServletContext is: " +request.getServletContext());


        ///if (last_name != "" || first_name != "") {
        if (request.getRequestURI().equals("/AddStudent")) {
            //out.println("add student servlet section");
            //out.println("getRequestURI is: " +request.getRequestURI());
            //out.println("getServletPath is: " +request.getServletPath());
            Student student = new Student(first_name, last_name, email);
            out.println("The new Student has been added. The new student id is: " + student.addStudent(factory));

        } else if (request.getRequestURI().equals("/ListStudents")){
            // Set response content type


            out.println(
                            "<table width = \"80%\" border = \"1\" align = \"center\">\n" +
                            "<tr bgcolor = \"#949494\">\n" +
                    "<th>Student ID</th> <th>First Name</th> <th>Last Name</th> <th>Email Address</th>\n" +
                            "</tr>\n");

            //List all the students in the database
            Student students = new Student();
            List allStudents = students.listStudents(factory);
            for (Iterator iterator = allStudents.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                out.print("<tr><td>" + student.getId() + "</td>");
                out.print("<td>" + student.getFirstName() + "</td>");
                out.print("<td>" + student.getLastName() + "</td>");
                out.println("<td>" + student.getEmail() + "</td></tr>");

                //out.println("Student ID: " + student.getId());
            }
            out.println("\n</table>\n");
        }
            out.println("</body></html>");
            //Student student = new Student(firstname, lastname, email);

    }

    // Method to handle POST method request.
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}
