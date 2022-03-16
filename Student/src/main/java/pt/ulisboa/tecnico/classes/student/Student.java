package pt.ulisboa.tecnico.classes.student;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
public class Student {

  public static void main(String[] args) throws Exception {
    System.out.println(Student.class.getSimpleName());
    System.out.printf("Received %d Argument(s)%n", args.length);
    for (int i = 0; i < args.length; i++) {
      System.out.printf("args[%d] = %s%n", i, args[i]);}

    if (args.length < 2) {
      System.err.println("Argument(s) missing!");
      System.err.printf("Usage: java %s host port%n", Student.class.getName());
      return;
    }

    final String host = args[0];
    final int port = Integer.parseInt(args[1]);
    final String target = host + ":" + port;

    final ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

    StudentServiceGrpc.StudentServiceBlockingStub stub = StudentServiceGrpc.newBlockingStub(channel);
    StudentClassServer.ListClassRequest listRequest = StudentClassServer.ListClassRequest.newBuilder().build();
    StudentClassServer.ListClassResponse listResponse = stub.listClass(listRequest);

    System.out.println("exibindo alunos:");
    System.out.println(listResponse);

    ClassesDefinitions.Student student = ClassesDefinitions.Student.newBuilder().setStudentId("22323").setStudentName("Jose").build();
    StudentClassServer.EnrollRequest enrollRequest = StudentClassServer.EnrollRequest.newBuilder().setStudent(student).build();
    StudentClassServer.EnrollResponse enrollResponse = stub.enroll(enrollRequest);

    System.out.println("inscrevendo o aluno:");
    System.out.println(enrollResponse);

    channel.shutdownNow();
  }
}
