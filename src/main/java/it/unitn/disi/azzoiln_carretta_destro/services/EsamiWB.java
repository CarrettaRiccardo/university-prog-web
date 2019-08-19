/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.services;

import com.google.gson.Gson;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Esami;
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Farmaci;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Steve
 */
@Path("esami")
public class EsamiWB {

    @Context
    private UriInfo context;
    private UtenteDao user;

    
    public EsamiWB(@Context ServletContext sc) throws DaoFactoryException {
        user = ((DaoFactory) sc.getAttribute("daoFactory")).getDAO(UtenteDao.class);
        System.out.println("WB esami()");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEsami() throws DaoFactoryException {
        System.out.println(context.getRequestUri());
        return getEsami("");
    }
    
    
    @GET
    @Path("{hint_nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEsami(@PathParam("hint_nome") String hint) throws DaoFactoryException {
        //List<Object> ret = new LinkedList<>();
        Esami f = null;
        Gson gson = new Gson();
        
        try{
            if ((hint == null) || "undefined".equals(hint))
                f = new Esami();
            else 
                f = user.getEsami(hint);
        }
        catch(DaoException ex){
            return gson.toJson(new String(""));     //in questo modo se ci sono errori simulo di non aver trovato farmaci
        }
        return gson.toJson(f);
    }
}
