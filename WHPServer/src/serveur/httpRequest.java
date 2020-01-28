package serveur;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class httpRequest {


    public static String getStringHttp(String URL) {
        StringBuilder returned = new StringBuilder();
        String usernameColonPassword = "admin:@laclaireFONTAINE";
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
        String string = getStringHttp("http://10.122.52.253/api/v3/packages");
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

    public static void main(String[] args) {

    }

}
