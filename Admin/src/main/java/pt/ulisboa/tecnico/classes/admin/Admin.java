package pt.ulisboa.tecnico.classes.admin;

import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.contract.Lookup.LookupResponse.ServerInfo;
import pt.ulisboa.tecnico.classes.contract.Lookup.LookupResponse;
import pt.ulisboa.tecnico.classes.contract.Lookup.LookupRequest;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.admin.LookupUtils;

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

    String parameters[] = {"localhost","5000"};
    HashMap<String,ArrayList<ServerInfo>> servers = new HashMap<>();
    int writes = 0;
    int reads = 0;
    LookupUtils look = new LookupUtils();

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("> ");
        try {
          String line = scanner.nextLine();
          switch (line) {

            case EXIT_CMD -> System.exit(0);

            case DUMP_CMD -> {
              String address = "";
              int port_server = 0;
              look.set_address_server(DUMP_CMD,writes,reads,address,port_server,servers);
              AdminFrontend frontend = new AdminFrontend(address,port_server);
              DumpRequest dump_req = DumpRequest.newBuilder().build();
              DumpResponse dump_res = frontend.setDump(dump_req);
              if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(dump_res))+"\n");
              else if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER)+"\n");
            }

            case ACTIV_CMD -> {
              AdminFrontend frontend = new AdminFrontend(host,port);
              ActivateRequest req = ActivateRequest.newBuilder().build();
              ActivateResponse res = frontend.setActivate(req);
              System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case LOOK_CMD -> {
              AdminFrontend frontend = new AdminFrontend("localhost",5000);
              ArrayList<String> qualifiers = new ArrayList<>();
              qualifiers.add(args[2]);
              LookupRequest req = LookupRequest.newBuilder().setService(args[1]).setQualifiers(0,"").addAllQualifiers(qualifiers).build();
              LookupResponse res = frontend.setLookup(req);
              res.getServersList().stream().map(server -> servers.get(args[1]).add(server));
              //System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case DEACT_CMD -> {
              AdminFrontend frontend = new AdminFrontend(parameters[0],Integer.parseInt(parameters[1]));
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
