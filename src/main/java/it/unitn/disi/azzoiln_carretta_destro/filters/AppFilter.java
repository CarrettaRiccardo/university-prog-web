package it.unitn.disi.azzoiln_carretta_destro.filters;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Steve
 */
public class AppFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    private UtenteDao userDao;

    public AppFilter() {
    }

    /**
     * @param request
     * @param response
     * @return F <-> E' stato fatto un redirect
     * @throws IOException
     * @throws ServletException
     */
    private boolean doBeforeProcessing(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (debug) log("AppFilter:DoBeforeProcessing");

        if (request instanceof HttpServletRequest) {
            ServletContext servletContext = ((HttpServletRequest) request).getServletContext();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            Utente user = null;
            if (session != null)
                user = (Utente) session.getAttribute("utente");

            String contextPath = servletContext.getContextPath();
            if (!contextPath.endsWith("/")) contextPath += "/";

            if (user == null) {
                ((HttpServletResponse) response).sendRedirect(((HttpServletResponse) response).encodeRedirectURL(contextPath + "login?login_error=auth"));
                return false;
            } else if (((HttpServletRequest) request).getRequestURI().indexOf("choose") > 0) {  //se sono nella pagina choose e continuo i controlli sotto stanti, entro in un loop. Quindi i blocco
                return true;
            } else if (user.getClass() == Persona.class) { //non è ancora stata fatta la scelta del tipo di utente da usare
                ((HttpServletResponse) response).sendRedirect(contextPath + "app/choose");
                return false;
            }


            // Caricamento sezioni visualizzabili
            String sezioni = ""; // Sezioni mostrate nella barra di navigazione laterale
            String sezioniDettagli = ""; // Sezioni mostrate nei dettagli dell'utente
            switch (((Utente) ((HttpServletRequest) request).getSession(false).getAttribute("utente")).getType()) {
                case PAZIENTE:
                    sezioni = "prenotazioni,visite,visite_specialistiche,esami,ricette";
                    sezioniDettagli = "";
                    break;
                case MEDICO:
                    sezioni = "pazienti,stats";
                    sezioniDettagli = "visite,visite_specialistiche,esami,ricette";
                    break;
                case MEDICO_SPEC:
                    sezioni = "pazienti";
                    sezioniDettagli = "visite,visite_specialistiche,esami";
                    break;
                case SSP:
                    sezioni = "medici,medici_specialisti";
                    sezioniDettagli = "stats";
                    break;
            }
            String[] sezioniTitles = sezioni.replaceAll("_", " ").split(",");
            String[] sezioniDettagliTitles = sezioniDettagli.replaceAll("_", " ").split(",");
            request.setAttribute("sezioni", sezioni);
            request.setAttribute("sezioni_titles", sezioniTitles);
            request.setAttribute("sezioni_dettagli", sezioniDettagli);
            request.setAttribute("sezioni_dettagli_titles", sezioniDettagliTitles);

        } else {
            throw new ServletException("Richiesta non riconusciuta valida");
        }


        return true;
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AppFilter:DoAfterProcessing");
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
     * @param request  The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain    The filter chain we are processing
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        if (debug) {
            log("AppFilter:doFilter()");
        }

        long startTime = System.nanoTime();
        
        
        if (!doBeforeProcessing(request, response))  //Se il BeforeProcessing ha fatto un redirect allora non ha senso fare il chain.doFilter e tutto quello che viene dopo
            return;

        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            
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
        
        
        long endTime = System.nanoTime();
        userDao.addLogTime(((HttpServletRequest)request).getRequestURI(), (endTime - startTime)/ 1000000); //registro il tempo in ms
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
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("AppFilter:Initializing filter");
            }
        }
        
        //Ottengo accesso al DAO per poter registrare il tempo di risposta
        DaoFactory daoFactory = (DaoFactory) filterConfig.getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
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
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AppFilter()");
        }
        StringBuffer sb = new StringBuffer("AppFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        try {
            ((HttpServletResponse)response).sendRedirect(((HttpServletResponse)response).encodeRedirectURL(getFilterConfig().getServletContext().getContextPath() + "/error"));
        } catch (Exception ex) {
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
