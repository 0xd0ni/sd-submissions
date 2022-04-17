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

       System.out.print("------PROTOLIST -- ENTREI NESTA MERDA -- PONTO 3");
       ClassesDefinitions.ServerEntry serverEntry =
               ClassesDefinitions.ServerEntry.newBuilder().setHostPort(
                       server.getHostPort()).addQualifiers(server.getQualifiers().get(0)).build();
                       //setQualifiers(0, server.getQualifiers().get(0)).build();
       System.out.print("------PROTOLIST -- ENTREI NESTA MERDA -- PONTO 4");
       return serverEntry;


   }

   public static List<ClassesDefinitions.ServerEntry> protoList(List<ServerEntry> serverEntryList) {

        System.out.print(" ------PROTOLIST -- ENTREI NESTA MERDA");

       List<ClassesDefinitions.ServerEntry> list = new ArrayList<>();

       if (!serverEntryList.isEmpty()) {
           for (ServerEntry server : serverEntryList) {
               if (server == null) {

               } else {
                   System.out.print("------PROTOLIST -- ENTREI NESTA MERDA -- PONTO 2");
                   list.add(proto(server));
               }
           }
       }
       return list;
   }





}