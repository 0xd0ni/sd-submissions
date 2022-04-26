package pt.ulisboa.tecnico.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;

import static java.lang.Math.abs;

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
        ServerEntry host_server = null;

        double write_percentage = writes * 100.0/(writes+reads);
        double read_percentage = reads * 100.0/(writes+reads);

        System.out.println("WRITE % - "+write_percentage);
        System.out.println("READ % - "+read_percentage);

        //P 50:50 -> escolhe P
        //S Ps-Ss = 10 -> escolhe S1
        //S Ps-Ss = 20 -> escolhe S2

        //Case the server is secondary
        if ( write_percentage > read_percentage || flag.equals(SECONDARY))
        {
            if (abs(write_percentage - read_percentage) <= 15 )
            {
                host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().
                        contains(SECONDARY)).toList().get(0);
                address = host_server.getHostPort().split(":")[0];
                port = host_server.getHostPort().split(":")[1];
                result.add(address);
                result.add(port);
                result.add(SECONDARY);
            }
            else
            {
                List<ServerEntry> saved_servers = servers.get(service).stream().filter(server -> server.getQualifiersList().
                        contains(SECONDARY)).toList();
                if (saved_servers.size() == 2)
                {
                    host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().
                        contains(SECONDARY)).toList().get(1);
                }
                else
                {
                    host_server = servers.get(service).stream().filter(server -> server.getQualifiersList().
                            contains(SECONDARY)).toList().get(0);
                }

                address = host_server.getHostPort().split(":")[0];
                port = host_server.getHostPort().split(":")[1];
                result.add(address);
                result.add(port);
                result.add(SECONDARY);
            }
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