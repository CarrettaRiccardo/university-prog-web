package it.unitn.disi.azzoiln_carretta_destro.utility;

import com.sun.mail.smtp.SMTPTransport;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author Ricky
 */
public class SendEmail {
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "29studs@gmail.com";
    private static final String PASSWORD = "Twentyninestudios29";

    private static final String EMAIL_FROM = "29studs@gmail.com";
    //private static final String EMAIL_TO = "s";
    private static final String EMAIL_TO_CC = "";

    //private static final String EMAIL_SUBJECT = "Prova di Email";
    //private static final String EMAIL_TEXT = "<h1>Prova Java Mail \n ABC123</h1>";
    
    public static void Invia(String email_to, String email_subject, String email_text_in_html) throws MessagingException{
        Properties prop = System.getProperties();
        prop.setProperty("mail.transport.protocol", "smtp");     
        prop.setProperty("mail.host", "smtp.gmail.com");  
        prop.put("mail.smtp.auth", "true");  
        prop.put("mail.smtp.port", "465");  
        prop.put("mail.debug", "true");  
        prop.put("mail.smtp.socketFactory.port", "465");  
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
        prop.put("mail.smtp.socketFactory.fallback", "false");  

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        
        msg.setFrom(new InternetAddress(EMAIL_FROM));

        msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email_to, false));

        msg.setSubject(email_subject);

                    // TEXT email
        //msg.setText(EMAIL_TEXT);

                    // HTML email
        msg.setDataHandler(new DataHandler(new HTMLDataSource(email_text_in_html)));


                    SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

                    // connect
        t.connect(SMTP_SERVER, USERNAME, PASSWORD);

                    // send
        t.sendMessage(msg, msg.getAllRecipients());

        System.out.println("Response: " + t.getLastServerResponse());

        t.close();

    }

    static class HTMLDataSource implements DataSource {

        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("html message is null!");
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public String getName() {
            return "HTMLDataSource";
        }
    }
}
