package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.MedicoDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Steve
 */
public class LoginServlet extends HttpServlet {

    private UtenteDao userDao;
    private HashMap<String, Integer> hashRememberMe;
    

    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
        hashRememberMe = (HashMap<String, Integer>) getServletContext().getAttribute("hashRememberMe"); //Ricky
        if (hashRememberMe == null){
            throw new ServletException("Impossible to get hashMap for remember_me"); //Steve: se non la trova è un errore, non può essere che sia null
        }
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = null;
        String token = null;
        String email = null;
        String password = null;
        Utente u = null;
        Cookie ck = null;
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        Cookie[] cookies = request.getCookies();     // request is an instance of type 
                                                     //HttpServletRequest
        
                                                     
        // cerca i cookie di "ricordami"
        for(int i = 0; i < cookies.length && token == null; i++){ 
            Cookie c = cookies[i];
            if (c.getName().equals("user_token")){
                ck = c;
                token = c.getValue();
                // cerco l'id corrispondente
                id = hashRememberMe.get(token);
                if (id != null){// c'è nell'Hash; altrimenti se è null probabilmente il server è stato riavviato
                    try{
                        u = (Utente) userDao.getByPrimaryKey(id);
                        if (u.getRes() < 0){// qualsiasi errore -> redirect login e cancella cookie
                            ck = new Cookie("user_token", "");
                            ck.setMaxAge(0);// cancella
                            response.addCookie(ck);
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "login"));
                        }
                    }
                    catch(DaoException ex){
                        throw new ServletException("Impossible to get dao factory for user storage system", ex);
                    }
                }
                else{
                    ck.setMaxAge(0);// cancella
                    response.addCookie(ck);
                }
            }
        }  
        
        
        // se non ci sono Cookie o non trova l'utente, prende i parametri
        if (token == null || id == null){
            email = request.getParameter("username");
            password = request.getParameter("password");
            if (email == null || password == null) {
                response.sendRedirect(response.encodeRedirectURL(contextPath + "login?login_error=user"));
            }
        }        

        try {
            if (u == null)// Ricky; se non c'era il cookie/token/valido cerca l'utente tramite mail/pass
                u = (Utente) userDao.login(email, password);  //Non capisco perchè sia necessario il cast, se qualcuno lo sa lo dica a Steve :)
            String where = "";
            switch (u.getRes()) {
                case -2:  where = "login?login_error=pwd"; break;
                case -1:  where = "login?login_error=user"; break;
                case 0:   where = "app/home"; request.getSession(true).setAttribute("utente", u); break;
                case 2: where = "app/home"; request.getSession(true).setAttribute("utente", u); break;
                case 1: where = "app/home";  request.getSession(true).setAttribute("utente", u); break;
                case -3:
                default: where = "login?login_error=service"; break;
            }
            
            if(u.getRes() >= 0 && request.getParameter("remember_me") != null)
            {
                String hash = "";
                do{
                    hash = it.unitn.disi.azzoiln_carretta_destro.utility.Common.randomAlphaNumeric();
                } while(hashRememberMe.containsKey(hash) || hash.compareTo("") == 0);// controllo di sicurezza
                hashRememberMe.put(hash, u.getId());
                Cookie c = new Cookie("user_token", hash);
                c.setMaxAge(30*24*60*60);// 30 giorni
                response.addCookie(c);
            }

            response.sendRedirect(response.encodeRedirectURL(contextPath + where));
        } catch (DaoException ex) {
            request.getServletContext().log("Impossible to retrieve the user", ex);
        } catch (NoSuchAlgorithmException ex) {
            request.getServletContext().log("Metodo non trovato Login per RememberMe", ex);
        }
    }
    
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }


}
