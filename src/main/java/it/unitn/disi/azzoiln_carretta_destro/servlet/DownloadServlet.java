package it.unitn.disi.azzoiln_carretta_destro.servlet;

import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.UtenteDao;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.exceptions.DaoFactoryException;
import it.unitn.disi.azzoiln_carretta_destro.persistence.dao.external.factories.DaoFactory;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Esame;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Medico;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Ricetta;
import it.unitn.disi.azzoiln_carretta_destro.persistence.entities.Utente;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DownloadServlet extends HttpServlet {
    private static String[] columns = { "Data e ora", "Farmaco", /*"Farmacia",*/ "Medico di base", "Paziente", "Prezzo" };
    
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
  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean startDownload = false;
        String filePath = "";
        
        HttpSession session = request.getSession(false);
        Utente u = (Utente) session.getAttribute("utente");
        if (u.isSsp()){        
            try {
                // GET LIST RICETTE

                List<Ricetta> ricette = userDao.Ssp().getRicette(new Date());


                // CREATE FILE

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Ricette");
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 14);
                headerFont.setColor(IndexedColors.RED.getIndex());

                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);

                // Create a Row
                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < columns.length; i++) {
                  Cell cell = headerRow.createCell(i);
                  cell.setCellValue(columns[i]);
                  cell.setCellStyle(headerCellStyle);
                }   

                // Create Other rows and cells with contacts data
                int rowNum = 1;

                for (Ricetta es : ricette) {
                    Row row = sheet.createRow(rowNum++);
                    String date = es.getTime() != null ? es.getTime().toString() : "-";
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        sdf.setLenient(false);
                        sdf.parse(date);// restituisce una "ParseException" se non e' valida
                    } catch (ParseException ex) {
                        // lascio la data nel formato di default
                    }

                    row.createCell(0).setCellValue(date);
                    row.createCell(1).setCellValue(es.getNomeFarmaco());
                    row.createCell(2).setCellValue(userDao.getUsername(es.getId_medico()));
                    row.createCell(3).setCellValue(userDao.getUsername(es.getId_paziente()));
                    row.createCell(4).setCellValue(es.getCosto()+ "€ (x " + es.getQuantita() + ")");
                }

                // Resize all columns to fit the content size
                for (int i = 0; i < columns.length; i++) {
                  sheet.autoSizeColumn(i);
                }
                SimpleDateFormat formatter= new SimpleDateFormat("dd_MM_yyyy");
                Date date = new Date(System.currentTimeMillis());

                String contextPath = getServletContext().getContextPath();
                if (!contextPath.endsWith("/")) contextPath += "/";

                String fileName = "Ricette_" + formatter.format(date) + ".xlsx";
                filePath = getServletContext().getRealPath("") + "Report" + File.separator;

                File file = new File(filePath);
                if (!file.exists()) file.mkdirs();

                filePath += u.getProvinciaNome() + File.separator;

                file = new File(filePath);
                if (!file.exists()) file.mkdirs();

                filePath += fileName;
                file = new File(filePath);

                if (file.exists()) file.delete();// sovrascrive il file

                FileOutputStream fileOut = new FileOutputStream(filePath);
                workbook.write(fileOut);
                fileOut.close();

                startDownload = true;
            } catch (DaoException ex) {
                throw new ServletException("xls_error");
            }
        }
        else { 
            if ((u.isMedico() || u.isMedicoSpecialista()) && request.getParameter("filePath") != null){// il filtro per i propri pazienti dovrebbe essere già fatto sulla pagina di visualizzazione dell'esame
                filePath = request.getParameter("filePath");
                startDownload = true;
            } else
            {
                throw new ServletException("not_authorized_file");
            }
        }
        
        if (startDownload){
            
            // DOWNLOAD FILE


            // reads input file from an absolute path
            File downloadFile = new File(filePath);
            FileInputStream inStream = new FileInputStream(downloadFile);

            // if you want to use a relative path to context root:
            // String relativePath = getServletContext().getRealPath("");
            System.out.println("relativePath = " + filePath);

            // gets MIME type of the file
            String mimeType = getServletContext().getMimeType(filePath);
            if (mimeType == null) {        
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME type: " + mimeType);

            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // obtains response's output stream
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inStream.close();
            outStream.close();
        }
    }
}