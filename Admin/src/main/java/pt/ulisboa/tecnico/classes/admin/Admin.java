package pt.ulisboa.tecnico.classes.admin;

import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.LookupUtils;
import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Admin {

  private static final String EXIT_CMD = "exit";
  private static final String ACTIV_CMD = "activate";
  private static final String DEACT_CMD = "deactivate";
  private static final String LOOK_CMD = "lookup";
  private static final String DUMP_CMD = "dump";


  public static void main(String[] args) {

    String host = "localhost";
    int port = 5000;

    HashMap<String,ArrayList<ServerEntry>> servers = new HashMap<>();
    int p_count = 0;
    int s_count = 0;
    LookupUtils look = new LookupUtils();


    try (AdminFrontend frontend = new AdminFrontend(host, port); Scanner scanner = new Scanner(System.in)) {

      Signal.handle(new Signal("INT"), sig -> {
        System.out.println("\nShutting down the Admin");
        frontend.close();
        System.exit(0);
      });

      NamingServerGlobalFrontend global_frontend = new NamingServerGlobalFrontend(host,port) {
        @Override
        public ClassServerNamingServer.RegisterResponse register(ClassServerNamingServer.RegisterRequest request) {
          return null;
        }

        @Override
        public LookupResponse lookup(LookupRequest request) {
          return super.lookup(request);
        }

        @Override
        public ClassServerNamingServer.DeleteResponse delete(ClassServerNamingServer.DeleteRequest request) {
          return null;
        }
      };

      while (true) {
        System.out.printf("> ");
        try {

          String line = scanner.nextLine();
          switch (line) {

            case EXIT_CMD -> System.exit(0);

            case DUMP_CMD -> {
              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"");
              frontend.setupSpecificServer(address,port_server);

              DumpRequest dump_req = DumpRequest.newBuilder().build();
              DumpResponse dump_res = frontend.setDump(dump_req);

              if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(dump_res))+"\n");
              else if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER)+"\n");
            }

            case ACTIV_CMD -> {
              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,args[1]);
              frontend.setupSpecificServer(address,port_server);

              ActivateRequest req = ActivateRequest.newBuilder().build();
              ActivateResponse res = frontend.setActivate(req);
              System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case LOOK_CMD -> {
              ArrayList<String> qualifiers = new ArrayList<>();
              qualifiers.add(args[2]);

              LookupRequest req = LookupRequest.newBuilder().setServiceName(args[1]).
                      setQualifiers(0,"").addAllQualifiers(qualifiers).build();
              LookupResponse res = global_frontend.lookup(req);

              res.getServerList().stream().forEach(server -> servers.get(args[1]).add(server));
              //System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case DEACT_CMD -> {
              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,args[1]);
              frontend.setupSpecificServer(address,port_server);

              DeactivateRequest d_req = DeactivateRequest.newBuilder().build();
              DeactivateResponse d_res = frontend.setDeactivate(d_req);
              System.out.println(Stringify.format(d_res.getCode())+"\n");
            }
            default -> System.out.println(Stringify.format(ResponseCode.UNRECOGNIZED)+"\n");
          }
        } catch (NullPointerException e) {
          System.err.println("Error: null pointer caught");
        }
      }
    } finally {
      System.out.println("");
    }
  }

}
