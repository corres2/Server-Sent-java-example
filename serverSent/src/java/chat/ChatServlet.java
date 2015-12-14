/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author PC
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/ChatServlet"})
public class ChatServlet extends HttpServlet {
    private HashMap<String, HttpServletResponse> clients=new HashMap<>();
    private HashMap<String, Long> listas = new HashMap<String, Long>();
    Calendar d=Calendar.getInstance();
    String eliminar="";
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */


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
        
        
        response.setContentType("text/event-stream; charset=utf-8");
        
        clients.put(UUID.randomUUID().toString(),response);
        
        while(true){
            try{
                Thread.sleep(5000);
            }catch(Exception e){
                
            }
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    @SuppressWarnings("empty-statement")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Calendar d=Calendar.getInstance();
        d.setTime(new Date()); /* whatever*/
        //c.setTimeZone(...); if necessary
        
        String text=request.getParameter("text");
        String user=request.getParameter("user");
        String emoji=request.getParameter("emoji");
        String nombreGrupo=request.getParameter("grupo");
        String sala=request.getParameter("sala");
        listas.put("inicial",d.getTimeInMillis());
       d.get(Calendar.MILLISECOND);
        //listas.remove("21");
        Iterator<String> keySetIterator = listas.keySet().iterator(); 
        Long l;
        d.setTime(new Date());
        while(keySetIterator.hasNext()){ 
            String key = keySetIterator.next(); 
            l=listas.get(key);  
            if(d.getTimeInMillis()-l>10000){
                eliminar=key;
                if(!"inicial".equals(key)){
                    listas.remove(key);
                } else {
                }
            }
        //    System.out.println("key: " + key + " value: " + listas.get(key)); 
        }
        
        if((!nombreGrupo.equals("null"))||!sala.equals("null")){
            if((!nombreGrupo.equals("null"))){
                listas.put(nombreGrupo,d.getTimeInMillis());
            }else{
                listas.put(sala,d.getTimeInMillis());
            }
            
        }
        
        
        
        for(HttpServletResponse c: clients.values()){
            c.getWriter().write("event:Censo\n");
            c.getWriter().write("data:{\"censo\":\"listo\"}\n\n");
            c.getWriter().flush();
            
            c.getWriter().write("event:recogeSalas\n");
            c.getWriter().write("data:{\"otrocoso\":\""+eliminar+"\"}\n\n");
            c.getWriter().flush();
            
            c.getWriter().write("event:agregaGrupo\n");
            c.getWriter().write("data:{\"grupo\":\""+nombreGrupo+"\"}\n\n");
            c.getWriter().flush();
            
            c.getWriter().write("data:{\"type\":\"message\", \"message\":\""+text+"\",\"user\": \""+user+"\",\"sala\": \""+sala+"\"\"}\n\n");
            c.getWriter().flush();
            
            c.getWriter().write("event:eliminar\n");
            c.getWriter().write("data:{\"eliminar\":\""+eliminar+"\"}\n\n");
            c.getWriter().flush();
        }
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
