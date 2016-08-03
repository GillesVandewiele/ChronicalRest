package be.ugent;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 
		final Properties p = new Properties();
		p.put("mail.smtp.host", "localhost");
		final Message msg = new MimeMessage(Session.getDefaultInstance(p));
		msg.setFrom(new InternetAddress("root@bigot.ugent.be"));
		msg.addRecipient(RecipientType.TO, new InternetAddress("root@bigot.ugent.be"));
		msg.setSubject(subject);
		msg.setText(message);
		Transport.send(msg);
    	
    	
//    	Runtime rt = Runtime.getRuntime();
//    	try {
//    		System.out.println("mail -s '"+subject+"' root@bigot.ugent.be <<< '"+message+"'");
//			Process pr = rt.exec("mail -s '"+subject+"' root@bigot.ugent.be <<< '"+message+"'");
//			
//			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
//			 
//            String line=null;
//
//            while((line=input.readLine()) != null) {
//                System.out.println("Mail output: "+line);
//            }
//
//            int exitVal = pr.waitFor();
//            System.out.println("Exited with error code "+exitVal);
//		} catch (IOException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}