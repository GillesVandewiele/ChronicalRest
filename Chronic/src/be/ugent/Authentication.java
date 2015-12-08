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
		if(header == null){
			System.out.println("Header for Authorization is null");
			return false;
		}
		String [] headerArray = header.split(" ");
		if(headerArray.length!=2){
			System.out.println("Wrong header for Authorization");
			return false;
		}
		
//		System.out.println("auth:" + header.split(" ")[1]);
		byte[] decoded = Base64.getDecoder().decode(headerArray[1]);
		String decodedString = new String(decoded, StandardCharsets.UTF_8);
//		System.out.println("Decoded: " + decodedString);

		String[] decodedArray = decodedString.split(":");
		if(decodedArray.length!=2){
			System.out.println("Wrong header for Authorization: no valid email");
			return false;
		}
		String requestEmail = decodedArray[0];
		System.out.println("Request email for autorhization: " + requestEmail);
		String requestRest = decodedArray[1];

		Patient patient = getPatient(requestEmail);
		DigestSHA3 md = new DigestSHA3(512); // same as DigestSHA3 md = new
												// SHA3.Digest256();
		if(patient==null){
			return false;
		}
		try {
			md.update((patient.getPassword()+Authentication.APIKEY).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		System.out.println("secondpart of autoriation: "+org.bouncycastle.util.encoders.Hex.toHexString(digest));
		if (org.bouncycastle.util.encoders.Hex.toHexString(digest).equals(requestRest)) {
			return true;
		}
		return false;
	}

	private static Patient getPatient(String email) {
		Patient patient = patientDao.getPatient(email);
		return patient;
	}

	public static int getPatientID(String header) {
		if(header == null){
			System.out.println("Header for Authorization is null");
			return -1;
		}
		String [] headerArray = header.split(" ");
		if(headerArray.length!=2){
			System.out.println("Wrong header for Authorization");
			return -1;
		}
		
//		System.out.println("auth:" + header.split(" ")[1]);
		byte[] decoded = Base64.getDecoder().decode(headerArray[1]);
		String decodedString = new String(decoded, StandardCharsets.UTF_8);
//		System.out.println("Decoded: " + decodedString);

		String[] decodedArray = decodedString.split(":");
		if(decodedArray.length!=2){
			System.out.println("Wrong header for Authorization: no valid email");
			return -1;
		}
		String requestEmail = decodedArray[0];
		System.out.println("Request email for autorhization: " + requestEmail);
		String requestRest = decodedArray[1];

		Patient patient = getPatient(requestEmail);
		DigestSHA3 md = new DigestSHA3(512); // same as DigestSHA3 md = new
												// SHA3.Digest256();
		if(patient==null){
			return -1;
		}
		try {
			md.update((patient.getPassword()+Authentication.APIKEY).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		System.out.println("secondpart of autoriation: "+org.bouncycastle.util.encoders.Hex.toHexString(digest));
		if (org.bouncycastle.util.encoders.Hex.toHexString(digest).equals(requestRest)) {
			return patient.getPatientID();
		}
		return -1;
	}
}
