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
    System.out.println(Admin.class.getSimpleName());

    final String host = "localhost";
    final int port = 5000;

    try (AdminFrontend frontend = new AdminFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");
        try {
          String line = scanner.nextLine();
          switch (line)
          {
            case EXIT_CMD:
              break;

            case DUMP_CMD:
              DumpRequest dump_req = DumpRequest.newBuilder().build();
              DumpResponse dump_res = frontend.setDump(dump_req);
              if (ResponseCode.forNumber(frontend.getCodeDump(dump_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(frontend.getClassState(dump_res)));
              break;

            case ACTIV_CMD:
              ActivateRequest req = ActivateRequest.newBuilder().build();
              ActivateResponse res = frontend.setActivate(req);
              if (ResponseCode.forNumber(frontend.getCode(res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              break;

            case DEACT_CMD:
              DeactivateRequest d_req = DeactivateRequest.newBuilder().build();
              DeactivateResponse d_res = frontend.setDeactivate(d_req);
              if (ResponseCode.forNumber(frontend.getCodeD(d_res)) == ResponseCode.OK)
                System.out.println(Stringify.format(ResponseCode.OK));
              break;
          }
        } catch (NullPointerException e) {
          System.out.println("NULL");
        }
      }
    } finally {
      System.out.println("");
    }
  }

}
