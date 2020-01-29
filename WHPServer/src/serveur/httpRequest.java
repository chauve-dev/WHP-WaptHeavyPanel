package serveur;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class httpRequest {


    public static String getStringHttp(String URL, String username, String password) {
        StringBuilder returned = new StringBuilder();
        String usernameColonPassword = username+":"+password;
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

        BufferedReader httpResponseReader = null;
        try {
            // Connect to the web server endpoint
            URL serverUrl = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

            // Set HTTP method as GET
            urlConnection.setRequestMethod("GET");

            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);

            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            httpResponseReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String lineRead;
            while((lineRead = httpResponseReader.readLine()) != null) {
                returned.append(lineRead);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

            if (httpResponseReader != null) {
                try {
                    httpResponseReader.close();
                } catch (IOException ioe) {
                    // Close quietly
                }
            }
        }
        return returned.toString();
    }

    public static String getPackages() throws JSONException {
        StringBuilder toreturn= new StringBuilder();
        String string = getStringHttp("http://10.122.52.253/api/v3/packages", "admin", "@laclaireFONTAINE");
        JSONObject json = new JSONObject(string);
        JSONArray jarray = json.getJSONArray("result");
        for (int i = 0; i <jarray.length(); i++){
            JSONObject obj = jarray.getJSONObject(i);
            toreturn.append(obj.getString("package"));
            toreturn.append(";");
        }
        toreturn.deleteCharAt(toreturn.length()-1);
        return toreturn.toString();
    }

    public static String getHosts() throws JSONException{
        StringBuilder toreturn= new StringBuilder();
        String string = getStringHttp("http://10.122.52.253/api/v1/hosts", "admin", "@laclaireFONTAINE");
        JSONObject json = new JSONObject(string);
        JSONArray jarray = json.getJSONArray("result");
        for (int i = 0; i <jarray.length(); i++){
            JSONObject obj = jarray.getJSONObject(i);
            toreturn.append(obj.getString("computer_name"));
            toreturn.append(";");
        }
        toreturn.deleteCharAt(toreturn.length()-1);
        return toreturn.toString();
    }

    public static String getHostUUID(String name) throws JSONException {
        String string = getStringHttp("http://10.122.52.253/api/v1/hosts",  "admin", "@laclaireFONTAINE");
        JSONObject json = new JSONObject(string);
        JSONArray jarray = json.getJSONArray("result");
        for (int i = 0; i <jarray.length(); i++){
            JSONObject obj = jarray.getJSONObject(i);
            if(obj.getString("computer_name").equals(name)){
                return obj.getString("uuid");
            }
        }
        return "";
    }

    public static String getAlreadyInstalled(String name) throws JSONException {
        StringBuilder toreturn= new StringBuilder();
        String string = getStringHttp("http://10.122.52.253/api/v1/hosts",  "admin", "@laclaireFONTAINE");
        JSONObject json = new JSONObject(string);
        JSONArray jarray = json.getJSONArray("result");
        for (int i = 0; i <jarray.length(); i++) {
            JSONObject obj = jarray.getJSONObject(i);
            if (obj.getString("computer_name").equals(name)) {
                toreturn.append(obj.get("depends"));
            }
        }
        return toreturn.toString().replace(",", ";");
    }

    public static String getHostsDet(String name) throws JSONException {
        StringBuilder toreturn= new StringBuilder();
        String string = getStringHttp("http://10.122.52.253/api/v1/hosts",  "admin", "@laclaireFONTAINE");
        JSONObject json = new JSONObject(string);
        JSONArray jarray = json.getJSONArray("result");
        for (int i = 0; i <jarray.length(); i++){
            JSONObject obj = jarray.getJSONObject(i);
            if(obj.getString("computer_name").equals(name)){
                toreturn.append(obj.getString("computer_name")+";");
                toreturn.append(obj.getString("os_name")+";");
                toreturn.append(obj.getString("manufacturer")+";");
                toreturn.append(obj.getString("productname")+";");
                if(obj.getJSONArray("connected_ips").getString(0)!=null) {
                    toreturn.append(obj.getJSONArray("connected_ips").getString(0)+";");
                }else{
                    toreturn.append("0.0.0.0;");
                }
                if(obj.getJSONArray("mac_addresses").getString(0)!=null) {
                    toreturn.append(obj.getJSONArray("mac_addresses").getString(0)+";");
                }else{
                    toreturn.append("00:00:00:00:00:00;");
                }
                toreturn.append(obj.getString("dnsdomain")+";");
                toreturn.append(obj.getString("reachable")+";");
                toreturn.append(obj.getString("host_status"));
            }
        }
        return toreturn.toString();
    }


    public static void main(String[] args) throws JSONException, ParserConfigurationException, IOException, SAXException {

    }

}
