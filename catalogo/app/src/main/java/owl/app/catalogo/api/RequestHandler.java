package owl.app.catalogo.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RequestHandler {

    //Metodo para enviar httpPostRequest
    //este metodo esta tomando dos argumentos
    //el primer argumento es la URL de la script al que enviaremos la solicitud
    //el otro es un HasMap con pares de valor de nombre que contiene los datos a enviar con la solicitud
    public String sendPostRequest(String requestURL,
                                  HashMap<String, String> postDataParams){

        //creacion de una URL
        URL url;

        //objeto stringBuilder para almacenar el mensaje recuperado del servidor
        StringBuilder sb = new StringBuilder();
        try{
            //inicializando url
            url = new URL(requestURL);

            //creacion de coneccion httpUrl
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //configuracion de pripiedades de conexion
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            //creacion de flujo de salida
            OutputStream os = conn.getOutputStream();

            //escribir parametros de la peticion.
            //utilizamos un metodo get opst Data String que se define a continuacion
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                //leer al servidor
                while ((response = br.readLine()) != null){
                    sb.append(response);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }return sb.toString();
    }

    public String sendGetRequest(String requestURL){
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null){
                sb.append(s + "\n");
            }
        }catch (Exception e){
            }
            return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
}
