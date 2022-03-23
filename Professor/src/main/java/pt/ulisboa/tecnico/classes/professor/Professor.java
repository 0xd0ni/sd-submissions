package pt.ulisboa.tecnico.classes.professor;

import pt.ulisboa.tecnico.classes.Stringify;
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
        System.out.println(Stringify.format(listResponse.getClassState()));
      }

      else if (command.equals("openEnrollments")) {
        Integer numStudents = Integer.parseInt(line[1]);
        ProfessorClassServer.OpenEnrollmentsResponse openEnrollmentsResponse = frontend.openEnrollments(numStudents);
        if (openEnrollmentsResponse.getCode().getNumber() == 0) {
          System.out.println("The action completed successfully.");
        } else {
          System.out.println("Some error");
        }
      }
      else if (command.equals("closeEnrollments")){
        ProfessorClassServer.CloseEnrollmentsResponse closeEnrollmentsResponse = frontend.closeEnrollments();
        if (closeEnrollmentsResponse.getCode().getNumber() == 0) {
          System.out.println("The action completed successfully.");
        } else {
          System.out.println("Some error");
        }
      }
      else if (command.equals("cancelEnrollment")){
        ProfessorClassServer.CancelEnrollmentResponse cancelEnrollmentResponse = frontend.cancelEnrollment(line[1]);
        if (cancelEnrollmentResponse.getCode().getNumber() == 0) {
          System.out.println("The action completed successfully.");
        } else {
          System.out.println("Some error");
        }
      }
      else if (command.equals("exit")) {
        frontend.close();
        System.exit(0);
      }
      else {
        System.out.println("Invalid command");
      }

      System.out.println("");
    }
  }
}

