package pt.ulisboa.tecnico.classes.student;

import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.classes.Utilities;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ServerEntry;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;
import sun.misc.Signal;

import static pt.ulisboa.tecnico.classes.Utilities.*;

public class Student {

  private static String name;
  private static String id;
  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String ENR_CMD = "enroll";


  public static void main(String[] args) {

    String host = NAMING_HOST;
    int port = NAMING_PORT;

    HashMap<String, ArrayList<ServerEntry>> servers = new HashMap<>();
    servers.put(SERVICE,new ArrayList<>());
    int p_count = 1;
    int s_count = 1;
    Utilities look = new Utilities();

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

      Signal.handle(new Signal(SIGINT), sig -> {
        System.out.println(EXIT_STUDENT);
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
            case EXIT_CMD -> System.exit(SUCCESS);

            case LIST_CMD -> {

              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              if(result.get(2).equals(PRIMARY))
                p_count++;
              else
                s_count++;

              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));

              ListClassRequest list_req = ListClassRequest.newBuilder().build();
              ListClassResponse list_res = frontend.setListClass(list_req);
              if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(list_res))+"\n");
              else if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER)+"\n");
            }

            case LOOKUP_CMD -> {
              Arrays.stream(line[2].split(",")).
                      collect(Collectors.toCollection(ArrayList::new)).stream().forEach(qualifier -> {

                        LookupRequest req = LookupRequest.newBuilder().setServiceName(line[1]).
                                addQualifiers(qualifier).build();

                        LookupResponse res = global_frontend.lookup(req);

                        res.getServerList().stream().forEach(server -> servers.get(line[1]).add(server));
                      });
            }

            case ENR_CMD -> {

              ArrayList<String> result = look.set_address_server(SERVICE,p_count,s_count,servers,"");
              if(result.get(2).equals(PRIMARY))
                p_count++;
              else
                s_count++;

              frontend.setupSpecificServer(result.get(0),Integer.parseInt(result.get(1)));

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
