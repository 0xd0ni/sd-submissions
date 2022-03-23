package pt.ulisboa.tecnico.classes.admin;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;

import java.util.Scanner;

public class Admin {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    AdminFrontend frontend = new AdminFrontend("localhost", 5000);

    while(input.hasNextLine()){
      System.out.print("> ");
      System.out.flush();
      String[] line = input.nextLine().split(" ");
      String command = line[0];

      System.out.println(command);

      if (command.equals("dump")){
        AdminClassServer.DumpResponse dumpResponse = frontend.dump();
        System.out.println(Stringify.format(dumpResponse.getClassState()));
      }

      else if (command.equals("activate")){
        AdminClassServer.ActivateResponse activateResponse = frontend.activate();
        if (activateResponse.getCode().getNumber() == 0) {
          System.out.println("The action completed successfully.");
        } else {
          System.out.println("Some error");
        }
      }
      else if (command.equals("deactivate")){
        AdminClassServer.DeactivateResponse deactivateResponse = frontend.deactivate();
        if (deactivateResponse.getCode().getNumber() == 0) {
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
