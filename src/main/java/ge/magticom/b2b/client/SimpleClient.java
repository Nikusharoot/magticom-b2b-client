package ge.magticom.b2b.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zviad on 6/3/18.
 * Secure connection test
 */
public class SimpleClient {

    Logger logger=Logger.getLogger(SimpleClient.class);
    String filePath = "/home/zviad/Desktop/keys/playapp/playapp1.jks";
    String urlString="https://b2b.magticom.ge/b2b-package-service/rest/simple/hello";
    String urlStringGet="https://b2b.magticom.ge/b2b-package-service/rest/simple/hello?name=pita";
    String keyPassword="Defender1";
    String requestParams="name=pit333a";




    public static void main(String[] argv) throws CertificateException,
            UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        SimpleClient instance=new SimpleClient();
        instance.testGetRequest();
    }


    public void testGetPackages() throws IOException, NoSuchAlgorithmException,
            KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        String result = "";
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream keyInput = new FileInputStream(filePath);
        keyStore.load(keyInput, keyPassword.toCharArray());
        keyInput.close();
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());
        KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
        ksCACert.load(new FileInputStream(filePath), keyPassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ksCACert);
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        con.setDoOutput(true);
        con.setSSLSocketFactory(context.getSocketFactory());
        PrintWriter out = new PrintWriter(con.getOutputStream());
        out.println(requestParams);
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            result = result + inputLine ;
        }
        System.out.printf(result);
        in.close();
    }




    public   HttpClient getHttpClient() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream keyInput = new FileInputStream(filePath);
        keyStore.load(keyInput, keyPassword.toCharArray());
        keyInput.close();
        keyManagerFactory.init(keyStore, keyPassword.toCharArray());
        KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
        ksCACert.load(new FileInputStream(filePath), keyPassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ksCACert);
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(context,
                new String[]{"TLSv1.2", "TLSv1.1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
    }


    public void testGetRequest() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpClient  httpClient=getHttpClient();
        HttpGet httpGet=new HttpGet(urlStringGet);
        HttpResponse response = httpClient.execute(httpGet);
        logger.info("Response Code : "
                + response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        logger.info(result);
    }


    public void postMethodTest() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpClient httpClient=getHttpClient();
        HttpPost httpPost=new HttpPost(urlString);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("name", "pitapost"));
        httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response=httpClient.execute(httpPost);
        logger.info("Response Code : "
                + response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.printf(result.toString());
    }


}

