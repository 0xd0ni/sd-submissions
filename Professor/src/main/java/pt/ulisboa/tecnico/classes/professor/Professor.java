package pt.ulisboa.tecnico.classes.professor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;

import java.util.Scanner;

public class Professor {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    final String serverAddress = "localhost:5000";

    final ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
    ProfessorServiceGrpc.ProfessorServiceBlockingStub stub = ProfessorServiceGrpc.newBlockingStub(channel);


    while(input.hasNextLine()){
      System.out.print("> ");
      System.out.flush();
      String[] line = input.nextLine().split(" ");
      String command = line[0];

      System.out.println(command);

      if (command.equals("list")){
        ProfessorClassServer.ListClassRequest listRequest = ProfessorClassServer.ListClassRequest.newBuilder().build();
        ProfessorClassServer.ListClassResponse listResponse = stub.listClass(listRequest);

        //System.out.println(listResponse.getCode());
        //System.out.println(Stringfy.format(listResponse.getClassState()));

        System.out.println(listResponse);
      }

      else if (command.equals("openEnrollments")) {
        Integer numStudents = Integer.parseInt(line[1]);

        ProfessorClassServer.OpenEnrollmentsRequest openEnrollmentsRequest = ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().setCapacity(numStudents).build();
        ProfessorClassServer.OpenEnrollmentsResponse openEnrollmentsResponse = stub.openEnrollments(openEnrollmentsRequest);

        System.out.println(openEnrollmentsResponse);

      }
      else if (command.equals("exit")) {
        channel.shutdownNow();
        System.exit(0);
      }
      else {
        System.out.println("Invalid command");
      }
    }
  }
}

