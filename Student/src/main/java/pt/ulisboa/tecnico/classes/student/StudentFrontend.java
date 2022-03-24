package pt.ulisboa.tecnico.classes.student;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;

public class StudentFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private final StudentServiceGrpc.StudentServiceBlockingStub stub;

    public StudentFrontend(String host, int port) {
        // Channel is the abstraction to connect to a service endpoint.
        // Let us use plaintext communication because we do not have certificates.
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        // Create a blocking stub.
        stub = StudentServiceGrpc.newBlockingStub(channel);
    }

    public int getCode(ListClassResponse response) {
        return response.getCodeValue();
    }

    public int getCodeE(EnrollResponse response) { return response.getCodeValue(); }

    public ClassState getClassState(ListClassResponse response) {
        return response.getClassState();
    }

    public ListClassResponse setListClass(ListClassRequest request)
    {
        return stub.listClass(request);
    }

    public EnrollResponse setEnroll(EnrollRequest request) { return stub.enroll(request); }

    @Override
    public final void close() {
        channel.shutdown();
    }

}