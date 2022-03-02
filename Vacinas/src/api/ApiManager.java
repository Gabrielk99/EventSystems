package src.api;
import com.google.gson.*;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ApiManager {
    private static HttpURLConnection prepareRequest(ManagerRouter router) throws MalformedURLException, IOException, ProtocolException {
        URL url = new URL(router.getURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(router.getMethod());
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        return con;
    }

    private static String processRequestResponse(HttpURLConnection conn, String url) throws IOException {
        if (conn.getResponseCode() != 200) {
            System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
            return null;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        String jsonTextResponse = readAll(bufferedReader);

        return jsonTextResponse;
    }

    public static JsonObject getManagerInfo(int id) {
        ManagerRouter router = ManagerRouter.MANAGER_INFO;
        router.setId(id);
        String responseContent = null;
        HttpURLConnection connection = null;

        try {
            connection = prepareRequest(router);
            responseContent = processRequestResponse(connection, router.getURL());
        } catch (IOException exception) {
            System.out.println("[ERROR]: Erro ao ler resposta da requição");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return new JsonParser().parse(responseContent).getAsJsonObject();
    }

    public static JsonArray getAllManagerInfo() {
        ManagerRouter router = ManagerRouter.MANAGERS_INFO;
        String responseContent = null;
        HttpURLConnection connection = null;

        try {
            connection = prepareRequest(router);
            responseContent = processRequestResponse(connection, router.getURL());
        } catch (IOException exception) {
            System.out.println("[ERROR]: Erro ao ler resposta da requição");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        return new JsonParser().parse(responseContent).getAsJsonArray();
    }

    // Utils
    private static String readAll(BufferedReader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}