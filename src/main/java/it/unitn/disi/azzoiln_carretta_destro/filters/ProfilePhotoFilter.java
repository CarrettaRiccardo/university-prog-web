package it.unitn.disi.azzoiln_carretta_destro.filters;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Filtro per controllare che un Paziente accede solo alle proprie foto e che i Medici accedano alle foto 
 * solo dei propri Pazienti.
 * Inoltre solo un utente con sessione attiva può accedere alla foto
 * @author Steve
 */
public class ProfilePhotoFilter implements Filter {
    
    private static final boolean debug = true;
    private UtenteDao userDao;
   
    private FilterConfig filterConfig = null;
    
    public ProfilePhotoFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("ProfilePhotoFilter:DoBeforeProcessing");
        }

        if(request instanceof HttpServletRequest){
            try {
                HttpServletRequest req = (HttpServletRequest) request;
                
                String[] tmp = req.getRequestURI().split("/");
                String foto_utente = tmp[tmp.length - 2];
                
                Utente u = (Utente) ((HttpServletRequest) request).getSession(false).getAttribute("utente");
                
                switch(u.getType()){
                    case PAZIENTE:    if( !(u.getUsername().equals(foto_utente)) ) throw new ServletException("not_authorized_photo"); break;
                    case MEDICO:      if( !userDao.Medico().isMyPatient(foto_utente, u.getId()) ) throw new ServletException("not_authorized_photo");   break;
                    case MEDICO_SPEC: if( !userDao.MedicoSpecialista().isMyPatient(foto_utente, u.getId()) ) throw new ServletException("not_authorized_photo"); break;
                    case SSP: throw new ServletException("not_authorized_photo");
                }
                
                req.setAttribute("u_url", "medico");
            } catch (DaoException ex) {
                System.out.println(ex.getMessage());
                throw new ServletException(ex.getMessage());
            }
            catch(NullPointerException ex){
                System.out.println(ex.getMessage());
                throw new ServletException("no_session");
            }
        }
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("ProfilePhotoFilter:DoAfterProcessing");
        }
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        if (debug) {
            log("ProfilePhotoFilter:doFilter()");
        }
        
        Throwable problem = null;
        try {
            doBeforeProcessing(request, response);
            chain.doFilter(request, response);
        } catch (Throwable t) {
            problem = t;
            t.printStackTrace();
        }
        
        doAfterProcessing(request, response);
        
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("ProfilePhotoFilter:Initializing filter");
            }
        }
        
        DaoFactory daoFactory = (DaoFactory) filterConfig.getServletContext().getAttribute("daoFactory"); 
        if (daoFactory == null)
            Logger.getLogger(MedicoFilter.class.getName()).log(Level.SEVERE, "Impossible to get dao factory for user storage system Filter", new ServletException("Impossible to get dao factory for user storage system Filter"));                    
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
        } catch (DaoFactoryException ex) {
            Logger.getLogger(MedicoFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("ProfilePhotoFilter()");
        }
        StringBuffer sb = new StringBuffer("ProfilePhotoFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
