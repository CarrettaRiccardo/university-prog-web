/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Steve
 */
public class ChooseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mod = request.getParameter("m");
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) 
            contextPath += "/";
        System.out.println(contextPath);
        HttpSession session = request.getSession(false);
        
        Utente u = (Utente) session.getAttribute("utente");
        
        if(u instanceof Medico || u instanceof MedicoSpecialista){
            //mando a home
        }
        
        if(mod == null || (!mod.equals("m") && !mod.equals("p")) || u instanceof Ssp || u instanceof Paziente){  //mostro un errore anche se l' utente loggato è già stato riconosciuto come paziente o come ssp
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login?login_error=service"));
        }
        else{
            Persona p = ((Persona) u);
            if(mod.equals("m")){  //scelta modalità medico. La differenziazione tra medico e medico_spec la faccio qua
                switch(p.getRuolo()){
                    case "medico": //creo un oggetto Medico(leggendo i dati che mancano [magari usando getByPrimaryKey]) e lo salvo nella sessione al posto di utente break;
                    case "medico_spec"://creo un oggetto Medico e lo salvo nella sessione al posto di utente break;
                    default: //errore, operazione non consentita
                }
            }
            else{
                //creo un oggetto Paziente e lo salvo nella sessione al posto di utente
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
