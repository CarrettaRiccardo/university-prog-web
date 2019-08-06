package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.RicettaDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Steve
 */
public class RicetteServlet extends HttpServlet {

    private RicettaDao ricettaDao;
    
    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            ricettaDao = daoFactory.getDAO(RicettaDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*RequestDispatcher rd = request.getRequestDispatcher("/include/ricette.jsp");
        String mio = "ciao";
        request.setAttribute("mio", mio);
        rd.include(request, response);*/
        
        try {
            /*Ricetta r = new Ricetta(1,2,1,(short)3);
            System.out.println(ricettaDao.addRicetta(r));*/
            
            List<Ricetta> elenco = ricettaDao.getRicette(1);
            elenco.add(0, ricettaDao.getByPrimaryKey(1));
            System.out.println(elenco.get(0).getNomeFarmaco() + "-" + elenco.get(0).getCosto());
        } catch (DaoException ex) {
            Logger.getLogger(RicetteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
