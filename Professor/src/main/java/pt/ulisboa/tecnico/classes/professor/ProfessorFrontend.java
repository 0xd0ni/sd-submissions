package pt.ulisboa.tecnico.classes.professor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;


public class ProfessorFrontend implements AutoCloseable{
    private final ManagedChannel channel;
    private final ProfessorServiceGrpc.ProfessorServiceBlockingStub stub;

    public ProfessorFrontend(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        this.stub = ProfessorServiceGrpc.newBlockingStub(channel);
    }

    public int getCodeList(ProfessorClassServer.ListClassResponse response) { return response.getCodeValue(); }

    public int getCodeOpen(ProfessorClassServer.OpenEnrollmentsResponse response) { return response.getCodeValue(); }

    public int getCodeClose(ProfessorClassServer.CloseEnrollmentsResponse response) { return response.getCodeValue(); }

    public int getCodeCancel(ProfessorClassServer.CancelEnrollmentResponse response) { return response.getCodeValue(); }

    public ProfessorClassServer.ListClassResponse list(ProfessorClassServer.ListClassRequest request) { return stub.listClass(request); }

    public ProfessorClassServer.OpenEnrollmentsResponse openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest request) { return stub.openEnrollments(request); }

    public ProfessorClassServer.CloseEnrollmentsResponse closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest request) { return stub.closeEnrollments(request); }

    public ProfessorClassServer.CancelEnrollmentResponse cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest request) { return stub.cancelEnrollment(request); }

    public ClassState getClassState(ProfessorClassServer.ListClassResponse response) { return response.getClassState(); }

    @Override
    public final void close() {
        channel.shutdown();
    }
}