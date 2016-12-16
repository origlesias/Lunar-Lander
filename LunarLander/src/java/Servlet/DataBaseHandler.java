

package Servlet;

// @author: Oriol Iglesias

import Controllers.exceptions.PlaysJpaController;
import Controllers.exceptions.UsersJpaController;
import Entities.Plays;
import Entities.Users;
import Entities.utilPlay;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


public class DataBaseHandler {

    private Connection c;
    private Date startDate= new Date();
    
    public DataBaseHandler() {
        c= conectDataBase();
        createUserTable();
        createScoreTable();
    }

    private Connection conectDataBase() {
      Connection c = null;
      try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
            "postgres", "1234");
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      return c;
    }
    
    private void createUserTable(){
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS USERS  "
                    + "(ID             SERIAL PRIMARY KEY NOT NULL,"
                    + " NICK           CHAR(40)    UNIQUE NOT NULL, "
                    + " PASSWORD       CHAR(40)    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
    
    private void createScoreTable(){
        try {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PLAYS  "
                    + "(ID                SERIAL PRIMARY KEY NOT NULL,"
                    + " USER_ID           INT     REFERENCES USERS(ID), "
                    + " START_DATE        TIMESTAMP    NOT NULL,"
                    + " FINISH_DATE       TIMESTAMP    NOT NULL,"
                    + " SCORE             INT     NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
    }
    
    public Users findUser(String nick){
        Statement stmt = null;
        Users u = null;
        UsersJpaController uc= new UsersJpaController(Persistence.createEntityManagerFactory("LunarLanderPU"));
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users u WHERE u.nick='" +nick+ "';");
            if(rs.next()) u= (Users)  uc.findUsers(rs.getInt("id"));
            rs.close();
            stmt.close();
        } catch (SQLException sQLException) {
        }
        return u;
    }
    public void insertUser(String nick, String password){
            UsersJpaController pjc= new UsersJpaController(Persistence.createEntityManagerFactory("LunarLanderPU"));
            pjc.addUser(nick, password);
    }
    
    public void insertScore(Users id, Date finish, int score){
       
            PlaysJpaController pjc= new PlaysJpaController(Persistence.createEntityManagerFactory("LunarLanderPU"));
            pjc.addScore(id, startDate, finish, score);
    }
    
    public ArrayList<utilPlay> jugadas(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LunarLanderPU");
        EntityManager em=emf.createEntityManager();
        Query q= em.createQuery("SELECT p FROM Plays p ORDER BY p.score DESC");
        List<Plays> list= q.getResultList();
        ArrayList<utilPlay> jugadas= new ArrayList<utilPlay>();
        for (Plays t : list) {
                jugadas.add(new utilPlay(t.getId(),t.getStartDate().toString(),t.getFinishDate().toString(),t.getScore(),t.getUserId()));
            }
        return jugadas;
    }
    
    public Users findUserByID(int id){
        UsersJpaController ujc= new UsersJpaController(Persistence.createEntityManagerFactory("LunarLanderPU"));
        return ujc.findUsers(id);
    }
            
    
    public void setStartDate(Date date){
        startDate= date;
    }
    
    
    
}
