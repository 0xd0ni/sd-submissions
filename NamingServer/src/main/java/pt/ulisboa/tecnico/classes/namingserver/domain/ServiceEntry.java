package pt.ulisboa.tecnico.classes.namingserver.domain;


import java.util.Set;

public class ServiceEntry {


    private String serviceName;

    private Set<ServerEntry> entries;




    public ServiceEntry() {

    }

    public synchronized Set<ServerEntry> getEntries() {
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















}