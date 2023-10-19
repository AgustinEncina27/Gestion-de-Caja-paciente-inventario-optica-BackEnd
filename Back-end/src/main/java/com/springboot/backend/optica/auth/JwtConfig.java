package com.springboot.backend.optica.auth;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JwtConfig {
	
	private static final String RSA_PRIVADA_PATH  = System.getenv("RSA_PRIVATE_KEY");
	
	public static final String RSA_PUBLICA = "-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu38ed+5E/IYfOBq/ilfZ\r\n"
			+ "pkrfNfOHsx1WGccQl21PKvRTOWwy7KeUookxu2db2mz/Y2+L1DskecL3ke9O5tx7\r\n"
			+ "KiX8/JBT1dTmfkfnlw3/BtUkjuPCMxq4kht2fR86hNe1h5PdfNZNO1auvutT2qhO\r\n"
			+ "actaPiZCeVIjAXCAbeI1V4t18zJJlrMXNFlKlMJB+lrU8IZl+vImgN3CJtIP2DVa\r\n"
			+ "B77SS/jhrFC1Kmg92RG5kBxMRbfbn3qcx3M3bQwLBo0fUVYWcICt/FojpHaLAzst\r\n"
			+ "mWRqspUFHKOnbaBmzpj1w2C2wWlXYCVdjJM6lzz3ktgrdPEdVBsmO4csBJ8ElFN+\r\n"
			+ "OwIDAQAB\r\n"
			+ "-----END PUBLIC KEY-----";
	
	
	public static String getRsaPrivatekey(){
		try {
	        if (RSA_PRIVADA_PATH != null) {
	            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(RSA_PRIVADA_PATH));
	            String rsaPrivateKey = new String(privateKeyBytes, "UTF-8");
	           return rsaPrivateKey;
	        } else {
	            System.err.println("sLa variable de entorno RSA_PRIVATE_KEY no está configurada.");
	            
	            return null;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}
}