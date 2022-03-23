package pt.ulisboa.tecnico.classes.professor;


import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;

public class ProfessorFrontend implements AutoCloseable{
    private final ManagedChannel channel;
    private final ProfessorServiceGrpc.ProfessorServiceBlockingStub stub;

    public ProfessorFrontend(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        this.stub = ProfessorServiceGrpc.newBlockingStub(channel);
    }

    public ProfessorClassServer.ListClassResponse list() {
        ProfessorClassServer.ListClassRequest listRequest = ProfessorClassServer.ListClassRequest.newBuilder().build();
        ProfessorClassServer.ListClassResponse listResponse = stub.listClass(listRequest);
        return listResponse;
    }

    public ProfessorClassServer.OpenEnrollmentsResponse openEnrollments(int numStudents) {
        ProfessorClassServer.OpenEnrollmentsRequest openEnrollmentsRequest = ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().setCapacity(numStudents).build();
        ProfessorClassServer.OpenEnrollmentsResponse openEnrollmentsResponse = stub.openEnrollments(openEnrollmentsRequest);
        return openEnrollmentsResponse;
    }

    public ProfessorClassServer.CloseEnrollmentsResponse closeEnrollments() {
        ProfessorClassServer.CloseEnrollmentsRequest closeEnrollmentsRequest = ProfessorClassServer.CloseEnrollmentsRequest.newBuilder().build();
        ProfessorClassServer.CloseEnrollmentsResponse closeEnrollmentsResponse = stub.closeEnrollments(closeEnrollmentsRequest);
        return closeEnrollmentsResponse;
    }

    public ProfessorClassServer.CancelEnrollmentResponse cancelEnrollment(String sid) {
        ProfessorClassServer.CancelEnrollmentRequest cancelEnrollmentRequest = ProfessorClassServer.CancelEnrollmentRequest.newBuilder().setStudentId(sid).build();
        ProfessorClassServer.CancelEnrollmentResponse cancelEnrollmentResponse = stub.cancelEnrollment(cancelEnrollmentRequest);
        return cancelEnrollmentResponse;
    }
    @Override
    public final void close() {
        channel.shutdown();
    }
}