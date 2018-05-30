import com.google.gson.JsonParser;
import com.oracle.javafx.jmx.json.JSONReader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.util.*;
//import org.json.simple.JSONObject;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Entity
@Table(name="student")
public class Student {

    @Id
    @Column(name="id")
    private int id;
    @Column(name="firstname")
    private String firstName;
    @Column(name="lastname")
    private String lastName;
    @Column(name="email")
    private String email;

    public Student(){

    }
    public Student(String firstname,String lastName,String email){
        this.firstName=firstname;
        this.lastName=lastName;
        this.email=email;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getNewStudentId(SessionFactory factory) {
        Session session = factory.openSession();
        Transaction tx = null;
        int studentId = 0;
        try {
            tx = session.beginTransaction();

            // Create a query to get the highest student id
            String studentIdHql = "select max(id) from Student";
            Query studentIdQuery = session.createQuery(studentIdHql);
            List maxId = studentIdQuery.list();
            studentId = ((Integer) maxId.get(0)).intValue();
            tx.commit();
        }catch(Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }finally{
            session.close();
        }
        return studentId;
    }

    //
    public Integer addStudent(SessionFactory factory){
        //save the student to the database
        Session session = factory.getCurrentSession();
        try {
            // begin the session transaction
            session.beginTransaction();
            // insert the student info to the database
            //System.out.println("inserting and committing transaction...");
            session.save(this);
            // commit the database transaction
            session.getTransaction().commit();
            //System.out.println("committed");

        } catch (Exception e) {
            if (session.beginTransaction() != null) session.beginTransaction().rollback();
            e.printStackTrace();
        }finally {
            // Clean things up
            //System.out.println("Closing transaction");
            session.close();
        }
        // get and return the student id of the new student
        return this.getNewStudentId(factory);
    }

    //JSON Build
    public String createJson(List studentList){

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            //System.out.println(gson.toJson(studentList));
        } catch (Exception e) { }

        try{
            FileWriter file = new FileWriter("students.txt");
            file.write(gson.toJson(studentList));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.toJson(studentList);
    }

    //JSON Parse
    public JSONArray parseJson(String filePath) {
        //String filePath = "students.txt";
        JSONArray jsonArray = null;
        try {
            FileReader reader = new FileReader(filePath);
            JSONParser jsonParser = new JSONParser();
            jsonArray = (JSONArray) jsonParser.parse(reader);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return jsonArray;
    }


    //Java Collection List
    public List listStudents(SessionFactory factory){

        Transaction tx = null;
        Session session = factory.openSession();
        List students = null;

        try {
            tx = session.beginTransaction();

            String hql = "from Student";
            Query query = session.createQuery(hql);
            students = query.list();

            Student student1 = (Student) students.get(0);
            System.out.println("First Student ID from List: " + student1.getId());

            for (Iterator iterator = students.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                System.out.print("Student ID: " + student.getId());
                System.out.print(" First Name: " + student.getFirstName());
                System.out.print("  Last Name: " + student.getLastName());
                System.out.println("  email: " + student.getEmail());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
        return students;
    }

    //Java Collections


    //Java Collection Set
    public Set<Integer> setStudents(List studentList){
        Set<Integer> studentIds = null;

        try {
            studentIds = new HashSet<Integer>();
            for (Iterator iterator = studentList.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                //Set add method
                studentIds.add(student.getId());
            }
        } catch (Exception e) { }

        //Set size method
        System.out.println("The last Student ID is: " + studentIds.size());
        return studentIds;
    }

    //Java Collection Map
    public Map mapStudents(List studentList){
        Map students = null;

        try {
            students = new HashMap();
            for (Iterator iterator = studentList.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                //Map add method
                students.put(student.id,student.firstName);
            }
        } catch (Exception e) { }

        //Print Map
        System.out.println("Print of student HashMap: " + students);
        return students;
    }

    //Java Collection TreeMap
    public TreeMap treeMapStudents(List studentList){
        TreeMap students = null;

        try {
            students = new TreeMap();
            for (Iterator iterator = studentList.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                //TreeMap add method
                students.put(student.id,student.firstName);
            }
        } catch (Exception e) { }

        //print TreeMap
        System.out.println("Print of student TreeMap: " + students);
        return students;
    }

    //Java Collection TreeSet
    public TreeSet treeSetStudents(List studentList){
        TreeSet students = null;

        try {
            students = new TreeSet();
            for (Iterator iterator = studentList.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                //TreeSet add method
                students.add(student.getId());
            }
        } catch (Exception e) { }

        //Set size method
        System.out.println("Print of student TreeSet: " + students);
        return students;
    }

    public void searchStudents(SessionFactory factory, String name){
        Transaction tx = null;
        Session session = factory.openSession();

        try {
            tx = session.beginTransaction();

            String hql = "from Student where firstName like '"+ name +"%'";
            Query query = session.createQuery(hql);
            List students = query.list();
            for (Iterator iterator = students.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                System.out.print("Student ID: " + student.getId());
                System.out.print(" First Name: " + student.getFirstName());
                System.out.print("  Last Name: " + student.getLastName());
                System.out.println("  email: " + student.getEmail());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }

        // Do another search, but for last name this time.
        tx = null;
        session = factory.openSession();
        try {
            tx = session.beginTransaction();

            String hql = "from Student where lastName like '"+ name +"%'";
            Query query = session.createQuery(hql);
            List students = query.list();
            for (Iterator iterator = students.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                System.out.print("Student ID: " + student.getId());
                System.out.print(" First Name: " + student.getFirstName());
                System.out.print("  Last Name: " + student.getLastName());
                System.out.println("  email: " + student.getEmail());
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
    }
}
