package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *Class defines a web page reader. (DEFAULT CODE USED BY PROFESSOR GOLDBERG USED)
 */
public class WebPageReader {

    private final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2) Gecko/20100115 Firefox/3.6";

    private BufferedReader reader;

    public WebPageReader(String webpage) {
        try {
            reader = readerFor(webpage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Return line of HTML code from web page
    public String readLine() throws IOException {
        return reader.readLine();
    }// readLine

    //Returns an HTML content reader for a specified URL (ORIGINAL CODE FROM PROFESSOR)
    private BufferedReader readerFor(String sURL) throws Exception {
        InputStream content = (InputStream) getURLInputStream(sURL);
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        return reader;
    }// read

    //Sets user agent (ORIGINAL CODE FROM PROFESSOR)
    private InputStream getURLInputStream(String sURL) throws Exception {
        URLConnection oConnection = (new URL(sURL)).openConnection();
        oConnection.setRequestProperty("User-Agent", USER_AGENT);
        return oConnection.getInputStream();
    }// getURLInputStream
}
