package src.api;

/**
 * Enum com rotas de gestor
 *
 * @author mikaella
 */

public enum ManagerVaccineRouter {
    MANAGER_INFO,
    MANAGERS_INFO,
    VACCINE_INFO;

    private String urlBase = "http://localhost:3001/api/";
    Integer id = null;

    protected String getMethod() {
        return "GET";
    }

    protected String getPath() {
        switch (this) {
            case MANAGER_INFO:
                return "gestor/" + id;
            case MANAGERS_INFO:
                return "gestor/";
            case VACCINE_INFO:
                return "vacina/" + id;
        }
        return "";
    }

    protected String getURL() {
        return urlBase + this.getPath();
    }

    public void setId(Integer id) {
        this.id = id;
    }
}