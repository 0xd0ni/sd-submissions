package pt.ulisboa.tecnico.classes.namingserver.domain;


import java.util.HashSet;

public class ServiceEntry {


    private String serviceName;

    private HashSet<ServerEntry> entries;

    public ServiceEntry() {


    }

    public ServiceEntry(String name) {
        setServiceName(name);

    }

    public synchronized HashSet<ServerEntry> getEntries() {
        return entries;

    }

    public synchronized boolean addEntry(ServerEntry entry) {
        return entries.add(entry);

    }

    public synchronized void setServiceName(String serviceName) {
        this.serviceName = serviceName;

    }

    public synchronized String getServiceName() {
        return serviceName;

    }

    public synchronized void setEntry(ServerEntry entry){
        entries.add(entry);
    }

    public synchronized boolean hasEntry(String address) {
        for (ServerEntry server : entries) {
            if (server.getHostPort().equals(address))
                return true;
        }
        return false;
    }

}