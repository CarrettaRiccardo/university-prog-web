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
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.VisiteSpecialistiche;
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
@Path("visite_spec")
public class Visite_specWB {

    @Context
    private UriInfo context;
    private UtenteDao user;

    
    public Visite_specWB(@Context ServletContext sc) throws DaoFactoryException {
        user = ((DaoFactory) sc.getAttribute("daoFactory")).getDAO(UtenteDao.class);
        System.out.println("WB farmaci()");
    }

    
    @GET
    @Path("{hint_nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVisite(@PathParam("hint_nome") String hint) {
        VisiteSpecialistiche f = null;
        Gson gson = new Gson();
        
        try{
            if ((hint == null) || "undefined".equals(hint))
                f = new VisiteSpecialistiche();
            else 
                f = user.getAllVisiteSpec(hint);
        }
        catch(DaoException ex){
            return gson.toJson(new String(""));     //in questo modo se ci sono errori simulo di non aver trovato nulla
        }
        return gson.toJson(f);
    }

}
