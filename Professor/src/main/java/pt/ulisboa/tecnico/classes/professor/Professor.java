package pt.ulisboa.tecnico.classes.professor;

import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import java.util.Scanner;

public class Professor {

  private static final String EXIT_CMD = "exit";
  private static final String LIST_CMD = "list";
  private static final String OPEN_CMD = "openEnrollments";
  private static final String CLOSE_CMD = "closeEnrollments";
  private static final String CANCEL_CMD = "cancelEnrollment";

  public static void main(String[] args) {
    final String host = "localhost";
    final int port = 5000;

    try (ProfessorFrontend frontend = new ProfessorFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
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
              ProfessorClassServer.ListClassRequest listRequest = ProfessorClassServer.ListClassRequest.newBuilder().build();
              ProfessorClassServer.ListClassResponse listResponse = frontend.list(listRequest);

              ResponseCode responseCode = ResponseCode.forNumber(frontend.getCodeList(listResponse));
              if (responseCode == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(listResponse)));
              else
                System.out.println(Stringify.format(responseCode));

              break;

            case OPEN_CMD:
              ProfessorClassServer.OpenEnrollmentsRequest openRequest = ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().build();
              ProfessorClassServer.OpenEnrollmentsResponse openResponse = frontend.openEnrollments(openRequest);

              ResponseCode code = ResponseCode.forNumber(frontend.getCodeOpen(openResponse));
              System.out.println(Stringify.format(code));
              break;

            case CLOSE_CMD:
              ProfessorClassServer.CloseEnrollmentsRequest closeRequest = ProfessorClassServer.CloseEnrollmentsRequest.newBuilder().build();
              ProfessorClassServer.CloseEnrollmentsResponse closeResponse = frontend.closeEnrollments(closeRequest);

              ResponseCode closeCode = ResponseCode.forNumber(frontend.getCodeClose(closeResponse));
              System.out.println(Stringify.format(closeCode));
              break;

            case CANCEL_CMD:
              ProfessorClassServer.CancelEnrollmentRequest cancelRequest = ProfessorClassServer.CancelEnrollmentRequest.newBuilder().build();
              ProfessorClassServer.CancelEnrollmentResponse cancelResponse = frontend.cancelEnrollment(cancelRequest);

              ResponseCode cancelCode = ResponseCode.forNumber(frontend.getCodeCancel(cancelResponse));
              System.out.println(Stringify.format(cancelCode));

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