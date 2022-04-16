package pt.ulisboa.tecnico.classes.namingserver.domain;

import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

public class ServerEntry {

    private String hostPort;

    private List<String> qualifiers =  new ArrayList<>();


    public ServerEntry() {


    }

    public ServerEntry(String hostPort) {
         setHostPort(hostPort);

    }

    public synchronized boolean hasQualifier(String qualifier) {
        return qualifiers.contains(qualifier);
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


   public static ClassesDefinitions.ServerEntry proto(ServerEntry server) {

       ClassesDefinitions.ServerEntry serverEntry =
               ClassesDefinitions.ServerEntry.newBuilder().setHostPort(
                       server.getHostPort()).setQualifiers(0, server.getQualifiers().get(0)).build();

       return serverEntry;


   }

   public static List<ClassesDefinitions.ServerEntry> protoList(List<ServerEntry> serverEntryList) {
       List<ClassesDefinitions.ServerEntry> list = new ArrayList<>();

       if (!serverEntryList.isEmpty()) {
           for (ServerEntry server : serverEntryList) {
               if (server == null) {

               } else {
                   list.add(proto(server));
               }
           }
       }
       return list;
   }

}