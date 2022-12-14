package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Paziente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.UtenteType;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Steve
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 5,    // 5 MB
        maxFileSize = 1024 * 1024 * 10,        // 10 MB
        maxRequestSize = 1024 * 1024 * 20)    // 20 MB
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
        response.setContentType("text/html;charset=UTF-8");
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/"))
            contextPath += "/";

        try {
            HttpSession session = request.getSession(false);
            Utente u = (Utente) session.getAttribute("utente");
            if (u.getType() == UtenteType.PAZIENTE) {
                Paziente newPaz = (Paziente) u;
                List<String> pr;
                pr = new LinkedList<>(userDao.Ssp().getListProvince());
                session.setAttribute("province", pr);
                if (newPaz.getProvincia() != null) {
                    List<String> cm = new LinkedList<>(userDao.Ssp().getListComuni(newPaz.getProvincia()));
                    session.setAttribute("comuni", cm);
                }
                List<Medico> md = new ArrayList<>(userDao.Ssp().getMedici(newPaz.getProvincia()));
                // rimuovo dalla lista se stesso se ?? anche un medico
                Integer i = 0;
                Boolean removed = false;
                while (!removed && i < md.size()) {
                    if (md.get(i).getId() == newPaz.getId()) {
                        md.remove(md.get(i));
                        removed = true;
                    }
                    i++;
                }
                session.setAttribute("medici", md);
                if (newPaz.getId_medico() != null) {
                    Medico myMedico = (Medico) userDao.getByPrimaryKey(newPaz.getId_medico(), "medico");
                    session.setAttribute("medico", myMedico);
                } else {
                    session.removeAttribute("medico");
                }
            } else if (u.getType() == UtenteType.MEDICO_SPEC) {
                // Caricamento lista competenze medico spec
                List<String> competenze = userDao.MedicoSpecialista().getCompetenze(u.getId());
                session.setAttribute("competenze_medico_spec", competenze);
            }
        } catch (Exception ex) {
            throw new ServletException("update_error");
        }

        response.sendRedirect(response.encodeRedirectURL(contextPath + "app/" + request.getAttribute("u_url") + "/settings"));
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
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        // se sono lasciati nulli dovrebbe dare errore "invalid_selection"
        try {
            HttpSession session = request.getSession(false);
            Utente u = (Utente) session.getAttribute("utente");
            Utente newUtente = u;
            Boolean updateFoto = request.getPart("file").getSize() > 0;
            String updateFotoPath = "";

            // se ?? un aggiornemento della foto...
            if (updateFoto) {
                // gets absolute path of the web application
                String applicationPath = request.getServletContext().getRealPath("");
                String relativePath = getServletContext().getAttribute("USERS_DIR").toString();//getServletContext().getAttribute("USERS_DIR").toString();
                String userPath = u.getUsername();
                // constructs path of the directory to save uploaded file
                String uploadFilePath = applicationPath + relativePath + File.separator + userPath;
                File file = new File(uploadFilePath);
                if (!file.exists()) file.mkdirs();

                Part filePart = request.getPart("file");
                //String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix. (to get only filename)
                updateFotoPath = uploadFilePath + File.separator + "foto.jpg";

                System.out.println("relativePath = " + updateFotoPath);
            
                filePart.write(updateFotoPath);

                // per creare una copia pi?? piccola da mostrare nella barra di navigazione
                resize(updateFotoPath, uploadFilePath + File.separator + "foto_small.jpg", 50, 50);
            }


            // se ?? un aggiornamento dei dati...
            if (u.getType() == UtenteType.PAZIENTE) {
                Integer idMed = null;
                String idM = request.getParameter("medico");
                String nomeProv = request.getParameter("provincia");
                String nomeCom = request.getParameter("comune");
                if (idM != null)
                    idMed = Integer.parseInt(idM);

                // creo l'oggetto Utente con i nuovi valori
                Paziente p = (Paziente) u;

                Integer a = nomeProv != null ? (!nomeProv.equals(u.getProvinciaNome()) ? null : idMed) : null;
                Integer b = nomeProv != null ? userDao.Ssp().getIdProvincia(nomeProv) : null;
                Integer c = nomeProv != null ? (!nomeProv.equals(u.getProvinciaNome()) ? null : (nomeCom != null ? userDao.Ssp().getIdComune(nomeCom) : null)) : null;
                String d = nomeProv != null ? (!nomeProv.equals(u.getProvinciaNome()) ? null : nomeCom) : null;

                newUtente = new Paziente(p.getId(), p.getUsername(),
                        p.getNome(), p.getCognome(), p.getData_nascita(), p.getCf(),
                        a,// controllo che se cambia provincia annulla il medico e comune
                        b,
                        c,
                        true, nomeProv,
                        d, updateFoto ? updateFotoPath : p.getFoto(), p.getSesso());

                // aggiorno l'utente
                userDao.update(newUtente);
                session.setAttribute("utente", newUtente);

                Paziente newPaz = (Paziente) newUtente;
                List<String> pr = new LinkedList<>(userDao.Ssp().getListProvince());
                request.setAttribute("province", pr);
                if (nomeProv != null) {
                    List<String> cm = new LinkedList<>(userDao.Ssp().getListComuni(userDao.Ssp().getIdProvincia(nomeProv)));
                    request.setAttribute("comuni", cm);
                }
                List<Medico> md = new ArrayList<>(userDao.Ssp().getMedici(newUtente.getProvincia()));
                // rimuovo dalla lista se stesso se ?? anche un medico
                Integer i = 0;
                Boolean removed = false;
                while (!removed && i < md.size()) {
                    if (md.get(i).getId() == newPaz.getId()) {
                        md.remove(md.get(i));
                        removed = true;
                    }
                    i++;
                }
                request.setAttribute("medici", md);
                if (newPaz.getId_medico() != null) {
                    Medico myMedico = (Medico) userDao.getByPrimaryKey(newPaz.getId_medico(), "medico");
                    session.setAttribute("medico", myMedico);
                } else {
                    session.removeAttribute("medico");
                }
            } else { // il medico non credo debba modificare niente (?)
                /*if (u.getType() == UtenteType.MEDICO || u.getType() == UtenteType.MEDICO_SPEC){
                    Medico m = (Medico) u;

                    newUtente = new Medico(m.getId(), m.getUsername(), 
                        m.getNome(), m.getCognome(), m.getCf(), m.getData_nascita(),
                        true, userDao.Ssp().getIdProvincia(nomeProv), m.getId_Comune(), m.getLaurea(), m.getInizioCarriera(), nomeProv, m.getFoto());
                }*/
            }
        } catch (NullPointerException ex) {
            throw new ServletException("invalid_selection");
        } catch (IllegalStateException ex) {
            throw new ServletException("max_file_size");
        } catch (Exception ex) {
            throw new ServletException("update_error");
        }

        doGet(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
