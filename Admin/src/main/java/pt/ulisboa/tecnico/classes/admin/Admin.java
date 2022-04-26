package pt.ulisboa.tecnico.classes.admin;

import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateGossipRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateGossipRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.GossipRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateGossipResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateGossipResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.GossipResponse;
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
import pt.ulisboa.tecnico.classes.Utilities;
import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import sun.misc.Signal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.classes.Utilities.*;

public class Admin {

  private static final String EXIT_CMD = "exit";
  private static final String ACTIV_CMD = "activate";
  private static final String DEACT_CMD = "deactivate";
  private static final String DUMP_CMD = "dump";
  private static final String ACTIV_GOSSIP = "activateGossip";
  private static final String DEACT_GOSSIP = "deactivateGossip";
  private static final String GOSSIP = "gossip";
  private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());
  private static String debugInput;
  static boolean debugFlag = false;

  public static void debug(String msg) {
    if(debugFlag) {
      LOGGER.info(msg);
    }
  }

  public static void main(String[] args) {

    String host = NAMING_HOST;
    int port = NAMING_PORT;

    HashMap<String,ArrayList<ServerEntry>> servers = new HashMap<>();
    servers.put(SERVICE,new ArrayList<>());
    int p_count = 1;
    int s_count = 1;
    Utilities look = new Utilities();

    if (args.length == 1)
    {
      debugInput = args[0];
      if(debugInput.equals(DEBUG)) {
        debugFlag = true;
      }
    }

    debug("Creating Admin Frontend");
    try (AdminFrontend frontend = new AdminFrontend(host, port); Scanner scanner = new Scanner(System.in)) {

      Signal.handle(new Signal(SIGINT), sig -> {
        debug("SIGINT found");
        System.out.println(EXIT_ADMIN);
        frontend.close();
        System.exit(SUCCESS);
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
        System.out.print(PROMPT);
        try {

          String[] line = scanner.nextLine().split(" ");
          switch (line[0]) {

            case EXIT_CMD -> System.exit(0);

            case DUMP_CMD -> {
              debug("Invoking dump command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }
              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set up");

              debug("Creating dump request");
              DumpRequest dump_req = DumpRequest.newBuilder().build();
              debug("Dump request sent");
              DumpResponse dump_res = frontend.setDump(dump_req);

              debug("Dump response arrived");
              if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(dump_res))+"\n");
              else if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER)+"\n");
            }

            case ACTIV_CMD -> {
              debug("Invoking activate command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }

              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set");

              debug("Creating and sending activate request");
              ActivateRequest req = ActivateRequest.newBuilder().build();
              ActivateResponse res = frontend.setActivate(req);
              debug("Activate response arrived");
              System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case LOOKUP_CMD -> {
              debug("Invoking lookup command");
              Arrays.stream(line[2].split(",")).
                      collect(Collectors.toCollection(ArrayList::new)).stream().forEach( qualifier -> {

                        LookupRequest req = LookupRequest.newBuilder().setServiceName(line[1]).
                                addQualifiers(qualifier).build();

                        LookupResponse res = global_frontend.lookup(req);

                        res.getServerList().stream().forEach(server -> servers.get(line[1]).add(server));
                      });
              debug("Servers found");
            }

            case DEACT_CMD -> {
              debug("Invoking deactivate command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }

              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set");

              debug("Creating and sending deactivate request");
              DeactivateRequest d_req = DeactivateRequest.newBuilder().build();
              DeactivateResponse d_res = frontend.setDeactivate(d_req);
              debug("Deactivate request arrived");
              System.out.println(Stringify.format(d_res.getCode())+"\n");
            }

            case ACTIV_GOSSIP -> {
              debug("Invoking activateGossip command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }

              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set");

              debug("Creating and sending activateGossip request");
              ActivateGossipRequest req = ActivateGossipRequest.newBuilder().build();
              ActivateGossipResponse res = frontend.setActivateGossip(req);
              debug("ActivateGossip responsed arrived");
              System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case DEACT_GOSSIP -> {
              debug("Invoking deactivateGossip command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }

              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set");

              debug("Creating and sending deactivateGossip request");
              DeactivateGossipRequest req = DeactivateGossipRequest.newBuilder().build();
              DeactivateGossipResponse res = frontend.setDeactivateGossip(req);
              debug("DeactivateGossip response arrived");
              System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case GOSSIP -> {
              debug("Invoking gossip command");
              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              debug("Server set");
              if(result.get(2).equals(PRIMARY)) {
                debug("Increasing primary count");
                p_count++;
              }
              else {
                debug("Increasing secondary count");
                s_count++;
              }

              debug("Setting up specific stub");
              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));
              debug("Specific stub set");

              debug("Creating and sending Gossip request");
              GossipRequest req = GossipRequest.newBuilder().build();
              GossipResponse res = frontend.setGossip(req);
              debug("Gossip response arrived");
              System.out.println(Stringify.format(res.getCode())+"\n");
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
