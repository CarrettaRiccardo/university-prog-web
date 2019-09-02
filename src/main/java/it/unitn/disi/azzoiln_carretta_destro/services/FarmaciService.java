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
import it.unitn.disi.azzoiln_carretta_destro.persistence.wrappers.Farmaci;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * REST Web Service per elenco farmaci
 *
 * @author Steve
 */
@Path("farmaci")
public class FarmaciService {
    @Context
    private UriInfo context;
    private UtenteDao user;


    public FarmaciService(@Context ServletContext sc) throws DaoFactoryException {
        user = ((DaoFactory) sc.getAttribute("daoFactory")).getDAO(UtenteDao.class);
        System.out.println("WB farmaci()");
    }

    /**
     * Retrieves representation of an instance of it.unitn.disi.azzoiln_carretta_destro.services.FarmaciService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getFarmaci() throws DaoFactoryException {
        System.out.println(context.getRequestUri());
        return getFarmaci("");
    }


    @GET
    @Path("{hint_nome}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFarmaci(@PathParam("hint_nome") String hint) throws DaoFactoryException {
        //List<Object> ret = new LinkedList<>();
        Farmaci f = null;
        Gson gson = new Gson();

        try {
            if ((hint == null) || "undefined".equals(hint))
                f = new Farmaci();
            else
                f = user.getFarmaci(hint);
        } catch (DaoException ex) {
            return gson.toJson(new String(""));     //in questo modo se ci sono errori simulo di non aver trovato farmaci
        }
        return gson.toJson(f);
    }
}
