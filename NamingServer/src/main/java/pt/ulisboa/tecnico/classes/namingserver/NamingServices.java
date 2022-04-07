package pt.ulisboa.tecnico.classes.namingserver;


import pt.ulisboa.tecnico.classes.namingserver.domain.ServiceEntry;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NamingServices {



    private ConcurrentHashMap<String,ServiceEntry> namingServerState;


    public NamingServices() {

    }

    public void addEntry(String service,ServiceEntry serviceEntry) {
        namingServerState.put(service,serviceEntry);

    }

    public void removeService(String service) {
        namingServerState.remove(service);

    }

}