package pt.ulisboa.tecnico.classes.namingserver.domain;


import java.util.List;

public class ServerEntry {

    private String hostPort;

    private List<String> qualifiers;


    public ServerEntry() {


    }



    public synchronized String getHostPort() {
        return hostPort;
    }


    public synchronized List<String> getQualifiers() {
        return qualifiers;
    }


    public synchronized void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public synchronized void addQualifier(String qualifier) {
        this.qualifiers.add(qualifier);
    }
}