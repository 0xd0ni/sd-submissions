package pt.ulisboa.tecnico.classes.namingserver.domain;


import java.util.HashMap;

public class NamingServices {


    private HashMap<String,ServiceEntry> namingServerState;


    public NamingServices() {
        this.namingServerState = new HashMap<>();

    }

    public void addEntry(String service,ServiceEntry serviceEntry) {
        namingServerState.put(service,serviceEntry);

    }

    public void removeService(String service) {
        namingServerState.remove(service);

    }

    public ServiceEntry getServiceEntry(String serviceName) {
        return namingServerState.get(serviceName);

    }

    public boolean checkForExistenceOfService(String service) {
        if(!namingServerState.isEmpty()) {
            return namingServerState.containsKey(service);
        }
        return false;
    }

}