package be.ugent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
 
/**
 *
 * @author Kiani
 */
public class TestClass {
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
 
    public static void generateAndSendEmail(String gmailID, String gmailPW, String[] toAddresses, String subject, String message) throws AddressException, MessagingException {
 
        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        mailServerProperties.put("mail.debug.auth", "true");
        System.out.println("Mail Server Properties have been setup successfully..");
 
        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        for (String string : toAddresses) {
        	generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(string));
		}
        generateMailMessage.setSubject(subject);
        String emailBody = message;
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");
 
        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");
 
        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com", gmailID, gmailPW);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}