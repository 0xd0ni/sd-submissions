package pt.ulisboa.tecnico.classes.student;

import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import pt.ulisboa.tecnico.classes.LookupUtils;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;
import sun.misc.Signal;

public class Student {

  private static String name;
  private static String id;
  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String E_CMD = "enroll";
  private static final String LOOK_CMD = "lookup";


  public static void main(String[] args) {

    String host = "localhost";
    int port = 5000;

    HashMap<String, ArrayList<ServerEntry>> servers = new HashMap<>();
    int p_count = 0;
    int s_count = 0;
    LookupUtils look = new LookupUtils();

    if (args.length < 2) {
      System.err.println("Argument(s) missing!");
      System.err.printf("Usage: java %s <id> <name>\n", Student.class.getName());
      return;
    }

    id = args[0];
    name = "";
    for (int j = 1; j < args.length; j++) {
      name += args[j];
      if (j != args.length - 1)
        name += " ";
    }

    try (StudentFrontend frontend = new StudentFrontend(host, port); Scanner scanner = new Scanner(System.in)) {

      Signal.handle(new Signal("INT"), sig -> {
        System.out.println("\nShutting down the Student");
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

              LookupRequest req = LookupRequest.newBuilder().setServiceName(args[1]).
                      setQualifiers(0,"").addAllQualifiers(qualifiers).build();
              LookupResponse res = global_frontend.lookup(req);

              res.getServerList().stream().forEach(server -> servers.get(args[1]).add(server));
              //System.out.println(Stringify.format(res.getCode())+"\n");
            }

            case E_CMD -> {

              String address = "";
              int port_server = 0;

              look.set_address_server("turmas",p_count,s_count,address,port_server,servers,"P");
              frontend.setupSpecificServer(address,port_server);

              EnrollRequest e_req = EnrollRequest.newBuilder().setStudent(
                      ClassesDefinitions.Student.newBuilder().setStudentId(id).setStudentName(name).build()).build();
              EnrollResponse e_res = frontend.setEnroll(e_req);
              System.out.println(Stringify.format(e_res.getCode())+"\n");
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
