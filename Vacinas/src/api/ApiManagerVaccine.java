package src.api;
import com.google.gson.*;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Classe que faz chamadas da api sobre dados de gestores e vacinas
 *
 * @author mikaella
 */
public class ApiManagerVaccine {
    private static HttpURLConnection prepareRequest(ManagerVaccineRouter router) throws MalformedURLException, IOException, ProtocolException {
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
        ManagerVaccineRouter router = ManagerVaccineRouter.MANAGER_INFO;
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
        ManagerVaccineRouter router = ManagerVaccineRouter.MANAGERS_INFO;
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

    public static JsonObject getVaccineInfo(int id) {
        ManagerVaccineRouter router = ManagerVaccineRouter.VACCINE_INFO;
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