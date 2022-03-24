package pt.ulisboa.tecnico.classes.student;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import java.util.Scanner;

public class Student {

  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String ENROLL_CMD = "enroll";

  public static void main(String[] args) {

    final String host = "localhost";
    final int port = 5000;

    if (args.length < 2) {
      System.err.println("Argument(s) missing!");
      System.err.printf("Usage: java %s <id> <name>\n", Student.class.getName());
      return;
    }

    String studentId = args[0];
    String studentName = "";
    for (int j = 1; j < args.length; j++) {
      studentName += args[j];
      if (j != args.length - 1)
        studentName += " ";
    }

    try (StudentFrontend frontend = new StudentFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("> ");
        try {
          String line = scanner.nextLine();
          switch (line)
          {
            case EXIT_CMD:
              System.exit(0);

              break;

            case LIST_CMD:
              StudentClassServer.ListClassRequest listRequest = StudentClassServer.ListClassRequest.newBuilder().build();
              StudentClassServer.ListClassResponse listResponse = frontend.list(listRequest);

              ResponseCode responseCode = ResponseCode.forNumber(frontend.getCodeList(listResponse));
              if (responseCode == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(listResponse)));
              else
                System.out.println(Stringify.format(responseCode));

              break;

            case ENROLL_CMD:
              StudentClassServer.EnrollRequest enrollRequest = StudentClassServer.EnrollRequest.newBuilder().build();
              StudentClassServer.EnrollResponse enrollResponse = frontend.enroll(enrollRequest);

              ResponseCode code = ResponseCode.forNumber(frontend.getCodeEnroll(enrollResponse));
              System.out.println(Stringify.format(code));
              break;
          }
        } catch (NullPointerException e) {
          System.out.println("NULL");
        }

        System.out.printf("%n");
      }
    }
  }
}
