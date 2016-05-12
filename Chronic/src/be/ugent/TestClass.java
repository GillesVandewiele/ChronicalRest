package be.ugent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
 
/**
 *
 * @author Kiani
 */
public class TestClass {
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
 
    public static void generateAndSendEmail(String subject, String message) throws AddressException, MessagingException {
 
    	Properties props = new Properties();
    	Session session = Session.getDefaultInstance(props, null);

    	try {
    	  Message msg = new MimeMessage(session);
    	  msg.setFrom(new InternetAddress("root@kiani", "Root admin bigot"));
    	  msg.addRecipient(Message.RecipientType.TO,
    	                   new InternetAddress("root@bigot.ugent.be", "Root admin bigot"));
    	  msg.setSubject(subject);
    	  msg.setText(message);
    	  Transport.send(msg);
    	} catch (AddressException e) {
    	  // ...
    	} catch (MessagingException e) {
    	  // ...
    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}