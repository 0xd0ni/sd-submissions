package pt.ulisboa.tecnico.classes.admin;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.ActivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DeactivateResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;

public class AdminFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private final AdminServiceGrpc.AdminServiceBlockingStub stub;

    public AdminFrontend(String host, int port) {
        // Channel is the abstraction to connect to a service endpoint.
        // Let us use plaintext communication because we do not have certificates.
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        // Create a blocking stub.
        stub = AdminServiceGrpc.newBlockingStub(channel);
    }

    public int getCode(ActivateResponse response) {
        return response.getCodeValue();
    }

    public ClassState getClassState(DumpResponse response) {
        return response.getClassState();
    }

    public int getCodeD(DeactivateResponse response) {
        return response.getCodeValue();
    }

    public int getCodeDump(DumpResponse response) {
        return response.getCodeValue();
    }

    public ActivateResponse setActivate(ActivateRequest request)
    {
        return stub.activate(request);
    }

    public DeactivateResponse setDeactivate(DeactivateRequest request)
    {
        return stub.deactivate(request);
    }

    public DumpResponse setDump(DumpRequest request)
    {
        return stub.dump(request);
    }

    @Override
    public final void close() {
        channel.shutdown();
    }
}