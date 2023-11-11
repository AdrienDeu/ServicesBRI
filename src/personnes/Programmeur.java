package personnes;


import java.util.ArrayList;

public class Programmeur {
    private final String login;
    private final String mdp;
    private String adresseFtp;

    private final ArrayList<Integer> services;

    public Programmeur(String login, String mdp, String adresseFtp) {
        this.login = login;
        this.mdp = mdp;
        this.adresseFtp = adresseFtp;
        services = new ArrayList<>();
    }

    public String getLogin() {
        return this.login;
    }

    public String getMdp() {
        return this.mdp;
    }
    public String getAdresseFtp() {
        return this.adresseFtp;
    }

    public void setAdresseFtp(String ftpAdresse) {
        this.adresseFtp = ftpAdresse;
    }

    public ArrayList<Integer> getServices() {
        return this.services;
    }

    public void addService(Integer service) {
        services.add(service);
    }
}
