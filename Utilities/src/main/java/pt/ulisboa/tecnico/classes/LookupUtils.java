package pt.ulisboa.tecnico.classes;

import java.util.ArrayList;
import java.util.HashMap;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;

public class LookupUtils {
    public LookupUtils(){}

    public void set_address_server(String service,int writes, int reads, String address, int port,
                                   HashMap<String, ArrayList<ServerEntry>> servers,String flag)
    {
        //Case the server is secondary
        if ( (writes/(writes+reads)) > (reads/(writes+reads)) || flag.equals("S"))
        {
            ServerEntry host_server = servers.get(service).stream().
                    filter(server -> server.getQualifiersList().contains("S")).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = Integer.parseInt(host_server.getHostPort().split(":")[1]);
            reads++;
        }
        else if ((writes/(writes+reads)) <= (reads/(writes+reads)) || flag.equals("P"))
        {
            ServerEntry host_server = servers.get(service).stream().
                    filter(server -> server.getQualifiersList().contains("P")).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = Integer.parseInt(host_server.getHostPort().split(":")[1]);
            writes++;
        }
    }
}
