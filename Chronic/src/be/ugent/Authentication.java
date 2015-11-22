package be.ugent;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;

import be.ugent.dao.PatientDao;
import be.ugent.entitity.Patient;

public class Authentication {
	private static PatientDao patientDao = new PatientDao();
	public static String APIKEY = "FiFoEdUdLOI4D19lj7Vb5pi72dDZf2aB";

	public static boolean isAuthorized(String header) {
		System.out.println("auth:" + header.split(" ")[1]);
		byte[] decoded = Base64.getDecoder().decode(header.split(" ")[1]);
		String decodedString = new String(decoded, StandardCharsets.UTF_8);
		System.out.println("Decoded: " + decodedString);

		String requestEmail = decodedString.split(":")[0];
		System.out.println("Request email: " + requestEmail);
		String requestRest = decodedString.split(":")[1];

		Patient patient = getPatient(requestEmail);
		DigestSHA3 md = new DigestSHA3(512); // same as DigestSHA3 md = new
												// SHA3.Digest256();
		
		try {
			md.update((patient.getPassword()+Authentication.APIKEY).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		System.out.println("secondpart: "+digest);
		if (digest.equals(requestRest)) {
			return true;
		}
		return false;
	}

	private static Patient getPatient(String email) {
		Patient patient = patientDao.getPatient(email);
		return patient;
	}
}
