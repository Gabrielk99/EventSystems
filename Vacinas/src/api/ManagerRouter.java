package src.api;

public enum ManagerRouter {
    MANAGER_INFO,
    MANAGERS_INFO;

    private String urlBase = "http://localhost:3001/api/gestor/";
    Integer id = null;

    protected String getMethod() {
        return "GET";
    }

    protected String getPath() {
        switch (this) {
            case MANAGER_INFO:
                return "" + id;
            case MANAGERS_INFO:
                return "";
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