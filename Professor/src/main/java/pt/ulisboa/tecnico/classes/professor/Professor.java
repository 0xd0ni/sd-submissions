package pt.ulisboa.tecnico.classes.professor;

import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import java.util.Scanner;

public class Professor {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    ProfessorFrontend frontend = new ProfessorFrontend("localhost", 5000);

    while(input.hasNextLine()){
      System.out.print("> ");
      System.out.flush();
      String[] line = input.nextLine().split(" ");
      String command = line[0];

      System.out.println(command);

      if (command.equals("list")){
        ProfessorClassServer.ListClassResponse listResponse = frontend.list();
        System.out.println(listResponse);
      }

      else if (command.equals("openEnrollments")) {
        Integer numStudents = Integer.parseInt(line[1]);
        ProfessorClassServer.OpenEnrollmentsResponse openEnrollmentsResponse = frontend.openEnrollments(numStudents);
        System.out.println(openEnrollmentsResponse);
      }
      else if (command.equals("closeEnrollments")){
        ProfessorClassServer.CloseEnrollmentsResponse closeEnrollmentsResponse = frontend.closeEnrollments();
        System.out.println(closeEnrollmentsResponse);
      }
      else if (command.equals("cancelEnrollment")){
        ProfessorClassServer.CancelEnrollmentResponse cancelEnrollmentResponse = frontend.cancelEnrollment(line[1]);
        System.out.println(cancelEnrollmentResponse);
      }
      else if (command.equals("exit")) {
        frontend.close();
        System.exit(0);
      }
      else {
        System.out.println("Invalid command");
      }
    }
  }
}

