/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Entities.Plays;
import Entities.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Oriol
 */
@WebServlet(name = "Moon", urlPatterns = {"/Moon"})
public class Moon extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Moon</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Moon at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            RequestDispatcher a = request.getRequestDispatcher("index.jsp");
            a.forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    DataBaseHandler dbh= new DataBaseHandler();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] galletas= request.getCookies();
        if(existsCookie(galletas, "fecha")){
            try {
                Date date= new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a").parse(findCookie(galletas, "fecha").getValue());
                dbh.setStartDate(date);
            } catch (ParseException ex) {
                Logger.getLogger(Moon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(-Double.parseDouble(request.getParameter("score"))<1.4){
        dbh.insertScore(dbh.findUser(findCookie(galletas,"nick").getValue()), new Date(),(int)(-100/Double.parseDouble(request.getParameter("score"))));
        }
        dbh.setStartDate(new Date());
    }
    
    private boolean existsCookie(Cookie[] galletas, String name){
        boolean exists=false;
        for(int i=0;i!=galletas.length;i++){
            if(name.equals(galletas[i].getName())) exists=true;
        }
        return exists;
    }
    
    private Cookie findCookie(Cookie[] galletas, String name){
        Cookie c = null;
        for(int i=0;i!=galletas.length;i++){
            if(name.equals(galletas[i].getName())) c=galletas[i];
        }
        return c;
    }
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    
    

}
