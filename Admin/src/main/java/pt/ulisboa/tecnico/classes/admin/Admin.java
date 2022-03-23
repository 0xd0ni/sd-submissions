package pt.ulisboa.tecnico.classes.admin;
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
        System.out.println(dumpResponse);
      }

      else if (command.equals("activate")){
        AdminClassServer.ActivateResponse activateResponse = frontend.activate();
        System.out.println(activateResponse);
      }
      else if (command.equals("deactivate")){
        AdminClassServer.DeactivateResponse deactivateResponse = frontend.deactivate();
        System.out.println(deactivateResponse);
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
