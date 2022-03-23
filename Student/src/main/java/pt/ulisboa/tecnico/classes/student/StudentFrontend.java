package pt.ulisboa.tecnico.classes.student;

import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class StudentFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private final StudentServiceGrpc.StudentServiceBlockingStub stub;

    public StudentFrontend(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        this.stub = StudentServiceGrpc.newBlockingStub(channel);
    }

    public StudentClassServer.ListClassResponse list() {
        StudentClassServer.ListClassRequest listRequest = StudentClassServer.ListClassRequest.newBuilder().build();
        StudentClassServer.ListClassResponse listResponse = stub.listClass(listRequest);
        return listResponse;
    }

    public StudentClassServer.EnrollResponse enroll(String id, String name) {
        ClassesDefinitions.Student student = ClassesDefinitions.Student.newBuilder().setStudentId(id).setStudentName(name).build();
        StudentClassServer.EnrollRequest enrollRequest = StudentClassServer.EnrollRequest.newBuilder().setStudent(student).build();
        StudentClassServer.EnrollResponse enrollResponse = stub.enroll(enrollRequest);
        return enrollResponse;
    }
    @Override
    public final void close() {
        channel.shutdown();
    }
}