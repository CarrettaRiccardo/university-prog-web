/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.MedicoSpecialista;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Persona;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ssp;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author Steve
 */
@MultipartConfig(fileSizeThreshold=1024*1024*5, 	// 5 MB 
                 maxFileSize=1024*1024*10,      	// 10 MB
                 maxRequestSize=1024*1024*20)   	// 20 MB
public class SettingsServlet extends HttpServlet {
    
    private UtenteDao userDao;

    @Override
    public void init() throws ServletException {
        DaoFactory daoFactory = (DaoFactory) getServletContext().getAttribute("daoFactory"); //Steve ho tolto super.
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user storage system");
        }
        try {
            userDao = daoFactory.getDAO(UtenteDao.class);
        } catch (DaoFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user storage system", ex);
        }
    }
    
    

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) 
            contextPath += "/";
        HttpSession session = request.getSession(false);
         
        try{
            Utente u = (Utente) session.getAttribute("utente");
            if (u.getType() == UtenteType.PAZIENTE){
                Paziente p = (Paziente) u;
                session.setAttribute("tipo", "paziente");

                String date = p.getData_nascita().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                sdf.parse(date);

                session.setAttribute("nome", p.getNome());
                session.setAttribute("cognome", p.getCognome());
                session.setAttribute("codice_fiscale", p.getCf());
                session.setAttribute("data_nascita", date);
                session.setAttribute("id_medico", p.getId_medico());
                session.setAttribute("nome_provincia", userDao.Ssp().getNomeProvincia(u.getProvincia()));
                List<String> pr = new LinkedList<>(userDao.Ssp().getListProvince());
                session.setAttribute("province", pr);
                List<Medico> md = new ArrayList<>(userDao.Ssp().getMedici(u.getProvincia()));
                // rimuovo dalla lista se stesso se è anche un medico
                Integer i = 0;
                Boolean removed = false;
                while (!removed && i < md.size()){
                    if (md.get(i).getId() == p.getId()){
                        md.remove(md.get(i));
                        removed = true;
                    }
                    i++;
                }
                session.setAttribute("medici", md);

            } else {
                if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC){
                    Medico m = (Medico) u;
                    session.setAttribute("tipo", "medico");

                    String date = m.getData_nascita().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    sdf.parse(date);

                    session.setAttribute("nome", m.getNome());
                    session.setAttribute("cognome", m.getCognome());
                    session.setAttribute("codice_fiscale", m.getCf());
                    session.setAttribute("data_nascita", date);
                    session.setAttribute("laurea", m.getLaurea());
                    session.setAttribute("carriera", m.getInizioCarriera());
                }
            }
        } catch (ParseException ex){
                throw new ServletException("invalid_date_exception");
        } catch (Exception ex) {
            throw new ServletException("retrieving_doctors_error");
        } 
        response.sendRedirect(response.encodeRedirectURL(contextPath + "app/settings"));
    }
    
     public static void resize(String inputImagePath,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
 
        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
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
        // se sono lasciati nulli dovrebbe dare errore "invalid_selection"
        try {
            HttpSession session = request.getSession(false);
            Utente u = (Utente) session.getAttribute("utente");
            Utente newUtente = null;
            Boolean updateFoto = request.getPart("file").getSize() > 0;
            String updateFotoPath = "";
            
            // se è un aggiornemento della foto...
            if (updateFoto) {
                // gets absolute path of the web application
                String applicationPath = request.getServletContext().getRealPath("");
                String relativePath = getServletContext().getAttribute("PHOTOS_DIR").toString();//getServletContext().getAttribute("PHOTOS_DIR").toString();
                String userPath = u.getUsername();
                // constructs path of the directory to save uploaded file
                String uploadFilePath = applicationPath + relativePath + File.separator + userPath;
                File file = new File(uploadFilePath);
                if(!file.exists()) file.mkdirs();
                
                Part filePart = request.getPart("file");
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix. (to get only filename)
                updateFotoPath = uploadFilePath + File.separator + "foto.jpg";
                
                filePart.write(updateFotoPath);
                
                // per creare una copia più piccola da mostrare nella barra di navigazione
                resize(updateFotoPath, uploadFilePath + File.separator + "foto_small.jpg", 50, 50);
            }
            
            
            // se è un aggiornamento dei dati...
            Integer idMed = null;
            String idM = request.getParameter("medico");
            String nomeProv = request.getParameter("provincia");
            if (idM != null && nomeProv != null)
                idMed = Integer.parseInt(idM);
            else
                throw new NullPointerException();

            // creo l'oggetto Utente con i nuovi valori
            if (u.getType() == UtenteType.PAZIENTE){
                Paziente p = (Paziente) u;
                
                newUtente = new Paziente(p.getId(), p.getUsername(), 
                        p.getNome(), p.getCognome(), p.getData_nascita(), p.getCf(),
                        idMed, userDao.Ssp().getIdProvincia(nomeProv), p.getId_Comune(), true, nomeProv, updateFoto ? updateFotoPath : p.getFoto());
            } else { // il medico non credo debba modificare niente (?)
                /*if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC){
                    Medico m = (Medico) u;

                    newUtente = new Medico(m.getId(), m.getUsername(), 
                        m.getNome(), m.getCognome(), m.getCf(), m.getData_nascita(),
                        true, userDao.Ssp().getIdProvincia(nomeProv), m.getId_Comune(), m.getLaurea(), m.getInizioCarriera(), nomeProv, m.getFoto());
                }*/
            }
            // aggiorno l'utente
            userDao.update(newUtente);
            session.setAttribute("utente", newUtente);
        } catch (NullPointerException ex) {
            throw new ServletException("invalid_selection");
        } catch (IllegalStateException ex) {
            throw new ServletException("max_file_size");
        }  catch (Exception ex) {
            throw new ServletException("update_error");
        } 
        
        // prende e inserisce i dati modificati
        doGet(request, response);
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
