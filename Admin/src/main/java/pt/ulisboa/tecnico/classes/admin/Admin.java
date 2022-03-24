package pt.ulisboa.tecnico.classes.admin;

import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import java.util.Scanner;

public class Admin {

  private static final String EXIT_CMD = "exit";
  private static final String ACTIV_CMD = "activate";
  private static final String DEACT_CMD = "deactivate";
  private static final String DUMP_CMD = "dump";


  public static void main(String[] args) {
    final String host = "localhost";
    final int port = 5000;

    try (AdminFrontend frontend = new AdminFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("> ");
        try {
          String line = scanner.nextLine();
          switch (line)
          {
            case EXIT_CMD:
              System.exit(0);
              break;

            case DUMP_CMD:
              DumpRequest dumpRequest = DumpRequest.newBuilder().build();
              DumpResponse dumpResponse = frontend.setDump(dumpRequest);

              ResponseCode responseCode = ResponseCode.forNumber(frontend.getCodeDump(dumpResponse));
              if (responseCode == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(dumpResponse)));
              else
                System.out.println(Stringify.format(responseCode));
              break;

            case ACTIV_CMD:
              ActivateRequest activateRequest = ActivateRequest.newBuilder().build();
              ActivateResponse activateResponse = frontend.setActivate(activateRequest);

              ResponseCode activateCode = ResponseCode.forNumber(frontend.getCodeActivate(activateResponse));
              System.out.println(Stringify.format(activateCode));
              break;

            case DEACT_CMD:
              DeactivateRequest deactivateRequest = DeactivateRequest.newBuilder().build();
              DeactivateResponse deactivateResponse = frontend.setDeactivate(deactivateRequest);

              ResponseCode deactCode = ResponseCode.forNumber(frontend.getCodeDeactivate(deactivateResponse));
              System.out.println(Stringify.format(deactCode));
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
