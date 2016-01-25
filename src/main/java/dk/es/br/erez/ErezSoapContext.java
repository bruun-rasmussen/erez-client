package dk.es.br.erez;

import java.net.URL;

/**
 *
 * @author osa
 */
public interface ErezSoapContext {

  URL getSoapUrl();
  String getSoapUser();
  char[] getSoapPassword();

//String getFtpHost();
//String getFtpUser();
//char[] getFtpPassword();
}
