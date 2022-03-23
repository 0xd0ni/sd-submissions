package pt.ulisboa.tecnico.classes.student;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import java.util.Scanner;

public class Student {

  static private String name;
  static private String id;

  public static void main(String[] args) {

    //name = "João";
    //id = "Aluno001";

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

    Scanner input = new Scanner(System.in);

    StudentFrontend frontend = new StudentFrontend("localhost", 5000);

    while(input.hasNextLine()){
      System.out.print("> ");
      System.out.flush();
      String command = input.nextLine();
      System.out.println(command);

      if (command.equals("list")){
        StudentClassServer.ListClassResponse listResponse = frontend.list();
        System.out.println(Stringify.format(listResponse.getClassState()));
      }

      else if (command.equals("enroll")) {
        StudentClassServer.EnrollResponse enrollResponse = frontend.enroll(id, name);
        if (enrollResponse.getCode().getNumber() == 0) {
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
        System.out.println("Invalid command.");
      }

      System.out.println("");
    }
  }
}
