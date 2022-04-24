package pt.ulisboa.tecnico.classes;

import java.util.ArrayList;
import java.util.HashMap;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;

public class Utilities {

    public static final String LOOKUP_CMD = "lookup";
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


    public Utilities() {

    }

    public ArrayList<String> set_address_server(String service,int writes, int reads,HashMap<String, ArrayList<ServerEntry>> servers,String flag)
    {
        ArrayList<String> result = new ArrayList<>();
        String address;
        String port;
        ServerEntry host_server;

        double write_percentage = writes * 100.0/(writes+reads);
        double read_percentage = reads * 100.0/(writes+reads);

        System.out.println("WRITE % - "+write_percentage);
        System.out.println("READ % - "+read_percentage);


        //Case the server is secondary
        if ( write_percentage > read_percentage || flag.equals(SECONDARY))
        {
            host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().
                    contains(SECONDARY)).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = host_server.getHostPort().split(":")[1];
            result.add(address);
            result.add(port);
            result.add(SECONDARY);

        }
        else if (write_percentage <= read_percentage || flag.equals(PRIMARY))
        {
            host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().
                    contains(PRIMARY)).toList().get(0);
            address = host_server.getHostPort().split(":")[0];
            port = host_server.getHostPort().split(":")[1];
            result.add(address);
            result.add(port);
            result.add(PRIMARY);

        }
        return result;
    }
}