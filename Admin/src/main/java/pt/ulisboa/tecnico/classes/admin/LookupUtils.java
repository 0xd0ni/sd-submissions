package pt.ulisboa.tecnico.classes.admin;

import java.util.ArrayList;
import java.util.HashMap;
import pt.ulisboa.tecnico.classes.contract.Lookup.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.Lookup.LookupResponse;

public class LookupUtils {
    public LookupUtils(){}

    public void set_address_server(String service,int writes, int reads, String address, int port, HashMap<String, ArrayList<LookupResponse.ServerInfo>> servers)
    {
        if ( (writes/(writes+reads)) > (reads/(writes+reads)))
        {
            LookupResponse.ServerInfo host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().contains("S")).toList().get(0);
            address = host_server.getAddress().split(":")[0];
            port = Integer.parseInt(host_server.getAddress().split(":")[1]);
        }
        else
        {
            LookupResponse.ServerInfo host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().contains("P")).toList().get(0);
            address = host_server.getAddress().split(":")[0];
            port = Integer.parseInt(host_server.getAddress().split(":")[1]);
        }
    }
}
