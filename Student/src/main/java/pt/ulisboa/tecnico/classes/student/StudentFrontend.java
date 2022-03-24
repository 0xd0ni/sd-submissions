package pt.ulisboa.tecnico.classes.student;

import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class StudentFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private final StudentServiceGrpc.StudentServiceBlockingStub stub;

    public StudentFrontend(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        this.stub = StudentServiceGrpc.newBlockingStub(channel);
    }
    public int getCodeList(StudentClassServer.ListClassResponse response) { return response.getCodeValue(); }

    public int getCodeEnroll(StudentClassServer.EnrollResponse response) { return response.getCodeValue(); }

    public StudentClassServer.ListClassResponse list(StudentClassServer.ListClassRequest request) { return stub.listClass(request); }

    public StudentClassServer.EnrollResponse enroll(StudentClassServer.EnrollRequest request) { return stub.enroll(request); }

    public ClassState getClassState(StudentClassServer.ListClassResponse response) { return response.getClassState(); }
    @Override
    public final void close() { channel.shutdown(); }
}