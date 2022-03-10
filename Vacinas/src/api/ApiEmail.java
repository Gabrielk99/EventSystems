package src.api;
import com.google.gson.*;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.http.HttpClient.*;
import java.io.*;
import java.time.Duration;

import src.models.EmailMessage;

/**
 * Classe que faz chamada de api para envio de email
 *
 * @author mikaella
 */
public class ApiEmail {
    private static HttpURLConnection prepareRequest(EmailRouter router) throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(router.getURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(router.getMethod());
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        con.connect();
        return con;
    }

    public static void postEmail(EmailMessage email) {
        Gson gson = new Gson();
        String emailRequestJsonBody = gson.toJson(email);

        System.out.println(emailRequestJsonBody);
        try {
            EmailRouter router = EmailRouter.SEND;
            HttpURLConnection connection = null;

            connection = prepareRequest(router);

            PrintStream ps = new PrintStream(connection.getOutputStream());
            ps.print(emailRequestJsonBody);
            ps.close();

            System.out.println(connection.getResponseCode());

        } catch(Exception exception) {
            System.out.println("[ERROR] Não foi possível enviar o email: \n" + exception.getMessage());
        }
    }

    private enum EmailRouter {
        SEND;

        private String urlBase = "http://localhost:3001/api/email/";

        protected String getMethod() {
            return "POST";
        }

        protected String getPath() { return "send"; }

        protected String getURL() {
            return urlBase + this.getPath();
        }
    }
}