package pt.ulisboa.tecnico.classes;

import java.util.ArrayList;
import java.util.HashMap;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;

public class Utils {

    public static final String LOOK_CMD = "lookup";
    public static final String SERVICE = "turmas";
    public static final String SIGINT = "INT";
    public static final String NAMING_HOST = "localhost";
    public static final String PRIMARY = "P";
    public static final String SECONDARY = "S";
    public static final String EXIT_ADMIN = "\nShutting down the Admin";
    public static final String EXIT_STUDENT = "\nShutting down the Student";
    public static final String EXIT_PROFESSOR = "\nShutting down the Professor";
    public static final String EXIT_SERVER = "\nShutting down the Server";
    public static final String DEBUG = "-debug";
    public static final String PROMPT = "> ";
    public static final int NAMING_PORT = 5000;
    public static final int SUCCESS = 0;


    public Utils() {

    }

    public void set_address_server(String service,int writes, int reads, String address, int port,
                                   HashMap<String, ArrayList<ServerEntry>> servers,String flag)
    {
        //Case the server is secondary
        if ( (writes/(writes+reads)) > (reads/(writes+reads)) || flag.equals(SECONDARY))
        {
            ServerEntry host_server = servers.get(service).stream().
                    filter(server -> server.getQualifiersList().contains(SECONDARY)).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = Integer.parseInt(host_server.getHostPort().split(":")[1]);
            reads++;
        }
        else if ((writes/(writes+reads)) <= (reads/(writes+reads)) || flag.equals(PRIMARY))
        {
            ServerEntry host_server = servers.get(service).stream().
                    filter(server -> server.getQualifiersList().contains(PRIMARY)).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = Integer.parseInt(host_server.getHostPort().split(":")[1]);
            writes++;
        }
    }
}
