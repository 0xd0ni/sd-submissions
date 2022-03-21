package pt.ulisboa.tecnico.classes.student;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class Student {

  static private String name;
  static private String id;

  public static void main(String[] args) throws Exception {

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
      name.concat(args[j]);
      if (j == args.length - 1)
        name.concat(" ");
    }
    System.out.printf("Student's id %s, and name %s\n", id, name);

    Scanner input = new Scanner(System.in);

    final String serverAddress = "localhost:5000";

    final ManagedChannel channel = ManagedChannelBuilder.forTarget(serverAddress).usePlaintext().build();
    StudentServiceGrpc.StudentServiceBlockingStub stub = StudentServiceGrpc.newBlockingStub(channel);


    while(input.hasNextLine()){
      System.out.print("> ");
      System.out.flush();
      String command = input.nextLine();
      System.out.println(command);

      if (command.equals("list")){
        StudentClassServer.ListClassRequest listRequest = StudentClassServer.ListClassRequest.newBuilder().build();
        StudentClassServer.ListClassResponse listResponse = stub.listClass(listRequest);

        System.out.println(listResponse);
      }

      else if (command.equals("enroll")) {
        ClassesDefinitions.Student student = ClassesDefinitions.Student.newBuilder().setStudentId(id).setStudentName(name).build();
        StudentClassServer.EnrollRequest enrollRequest = StudentClassServer.EnrollRequest.newBuilder().setStudent(student).build();
        StudentClassServer.EnrollResponse enrollResponse = stub.enroll(enrollRequest);

        System.out.println(enrollResponse);
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
