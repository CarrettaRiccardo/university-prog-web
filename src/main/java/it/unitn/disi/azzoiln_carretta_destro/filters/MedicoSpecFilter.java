package it.unitn.disi.azzoiln_carretta_destro.filters;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Steve
 */
@WebFilter(filterName = "MedicoSpecFilter", urlPatterns = {"/app/medico_spec/*"}, dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE})
public class MedicoSpecFilter implements Filter {
    
    private static final boolean debug = true;

   
    private FilterConfig filterConfig = null;
    private UtenteDao userDao;
    
    public MedicoSpecFilter() {
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MedicoSpecFilter:DoBeforeProcessing");
        }

        if(request instanceof HttpServletRequest){
            HttpServletRequest req = (HttpServletRequest) request;
            Integer id_paziente = null;
            
            if(req.getParameter("id_paziente") != null){
                try{
                    id_paziente = Integer.parseInt(req.getParameter("id_paziente"));
                }catch(NumberFormatException e){
                    throw  new ServletException("id_paziente not valid", e);
                }
                finally{
                    if(id_paziente == null || id_paziente <= 0)
                        throw new ServletException("id_paziente not valid");
                }
                
                Utente u = (Utente) ((HttpServletRequest) request).getSession(false).getAttribute("utente");
                if( ! (u instanceof MedicoSpecialista) ) 
                  throw new ServletException("not_authorized");
                if(id_paziente == u.getId())
                    throw new ServletException("not_my_patient");  //Non posso compilare dati su me stesso
            }           
            
            
            req.setAttribute("u_url", "medico_spec");
        }
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MedicoSpecFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
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
            log("MedicoSpecFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }
        
        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
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
                log("MedicoSpecFilter:Initializing filter");
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
            return ("MedicoSpecFilter()");
        }
        StringBuffer sb = new StringBuffer("MedicoSpecFilter(");
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
