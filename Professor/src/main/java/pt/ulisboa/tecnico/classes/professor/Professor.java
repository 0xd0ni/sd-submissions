package pt.ulisboa.tecnico.classes.professor;

import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.OpenEnrollmentsRequest;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.OpenEnrollmentsResponse;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import pt.ulisboa.tecnico.classes.LookupUtils;
import sun.misc.Signal;

public class Professor {

  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String OE_CMD = "openEnrollments";
  private static final String CE_CMD = "closeEnrollments";
  private static final String CAN_ENR_CMD = "cancelEnrollment";
  private static final String LOOK_CMD = "lookup";


  public static void main(String[] args) {

    String host = "localhost";
    int port = 5000;

    HashMap<String, ArrayList<ServerEntry>> servers = new HashMap<>();
    int p_count = 0;
    int s_count = 0;
    LookupUtils look = new LookupUtils();

    try (ProfessorFrontend frontend = new ProfessorFrontend(host, port); Scanner scanner = new Scanner(System.in)) {

      Signal.handle(new Signal("INT"), sig -> {
        System.out.println("\nShutting down the Professor");
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

          String[] line = scanner.nextLine().split(" ");
          switch (line[0]) {
            case EXIT_CMD -> System.exit(0);

            case LIST_CMD -> {

              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"");
              frontend.setupSpecificServer(address,port_server);

              ListClassRequest list_req = ListClassRequest.newBuilder().build();
              ListClassResponse list_res = frontend.setListClass(list_req);
              if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(list_res))+"\n");
              else if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER)+"\n");
            }

            case LOOK_CMD -> {
              ArrayList<String> qualifiers = new ArrayList<>();
              qualifiers.add(args[2]);

              LookupRequest req = LookupRequest.newBuilder().setServiceName(line[1]).
                      setQualifiers(0,"").addAllQualifiers(qualifiers).build();
              LookupResponse res = global_frontend.lookup(req);

              res.getServerList().stream().forEach(server -> servers.get(line[1]).add(server));
              //System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case OE_CMD -> {

              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"P");
              frontend.setupSpecificServer(address,port_server);

              int numStudents = Integer.parseInt(line[1]);
              OpenEnrollmentsRequest oe_req = OpenEnrollmentsRequest.newBuilder().setCapacity(numStudents).build();
              OpenEnrollmentsResponse oe_res = frontend.setOE(oe_req);
              System.out.println(Stringify.format(oe_res.getCode())+"\n");
            }

            case CE_CMD -> {

              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"P");
              frontend.setupSpecificServer(address,port_server);

              CloseEnrollmentsRequest ce_req = CloseEnrollmentsRequest.newBuilder().build();
              CloseEnrollmentsResponse ce_res = frontend.setCE(ce_req);
              System.out.println(Stringify.format(ce_res.getCode())+"\n");
            }
            case CAN_ENR_CMD -> {

              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"P");
              frontend.setupSpecificServer(address,port_server);

              CancelEnrollmentRequest c_req = CancelEnrollmentRequest.newBuilder().setStudentId(line[1]).build();
              CancelEnrollmentResponse c_res = frontend.setCanEnr(c_req);
              System.out.println(Stringify.format(c_res.getCode())+"\n");
            }
            default -> System.out.println(Stringify.format(ResponseCode.UNRECOGNIZED)+"\n");
          }
        } catch (NullPointerException e) {
          System.err.println("Error: null pointer caught");
        }
        catch (NumberFormatException e) {
          System.err.println("Error: string given instead of a number");
        }
      }
    } finally {
      System.out.println("");
    }
  }

}
