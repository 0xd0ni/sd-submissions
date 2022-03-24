package pt.ulisboa.tecnico.classes.student;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

import java.util.Scanner;

public class Student {

  private static String name;
  private static String id;
  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String E_CMD = "enroll";


  public static void main(String[] args) {

    final String host = "localhost";
    final int port = 5000;

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
    System.out.printf("Student's id %s, and name %s\n", id, name);

    try (StudentFrontend frontend = new StudentFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");
        try {
          String[] line = scanner.nextLine().split(" ");
          switch (line[0]) {
            case EXIT_CMD -> System.exit(0);

            case LIST_CMD -> {
              ListClassRequest list_req = ListClassRequest.newBuilder().build();
              ListClassResponse list_res = frontend.setListClass(list_req);
              if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(list_res)));
              else if (ResponseCode.forNumber(frontend.getCode(list_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
            }
            case E_CMD -> {
              EnrollRequest e_req = EnrollRequest.newBuilder().setStudent(ClassesDefinitions.Student.newBuilder().setStudentId(id).setStudentName(name).build()).build();
              EnrollResponse e_res = frontend.setEnroll(e_req);
              if (ResponseCode.forNumber(frontend.getCodeE(e_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              else if (ResponseCode.forNumber(frontend.getCodeE(e_res)) == ResponseCode.INACTIVE_SERVER)
                System.out.println(Stringify.format(ResponseCode.INACTIVE_SERVER));
              else if (ResponseCode.forNumber(frontend.getCodeE(e_res)) == ResponseCode.NON_EXISTING_STUDENT)
                System.out.println(Stringify.format(ResponseCode.NON_EXISTING_STUDENT));
              else if (ResponseCode.forNumber(frontend.getCodeE(e_res)) == ResponseCode.STUDENT_ALREADY_ENROLLED)
                System.out.println(Stringify.format(ResponseCode.STUDENT_ALREADY_ENROLLED));
              else if (ResponseCode.forNumber(frontend.getCodeE(e_res)) == ResponseCode.FULL_CLASS)
                System.out.println(Stringify.format(ResponseCode.FULL_CLASS));
            }
            default -> System.out.println(Stringify.format(ResponseCode.UNRECOGNIZED));
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
