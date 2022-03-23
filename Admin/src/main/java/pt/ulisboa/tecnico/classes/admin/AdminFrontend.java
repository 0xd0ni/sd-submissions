package pt.ulisboa.tecnico.classes.admin;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;

public class AdminFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private final AdminServiceGrpc.AdminServiceBlockingStub stub;

    public AdminFrontend(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = AdminServiceGrpc.newBlockingStub(channel);
    }

    public AdminClassServer.DumpResponse dump() {
        AdminClassServer.DumpRequest dumpRequest = AdminClassServer.DumpRequest.newBuilder().build();
        AdminClassServer.DumpResponse dumpResponse = stub.dump(dumpRequest);
        return dumpResponse;
    }

    public AdminClassServer.ActivateResponse activate() {
        AdminClassServer.ActivateRequest activateRequest = AdminClassServer.ActivateRequest.newBuilder().build();
        AdminClassServer.ActivateResponse activateResponse = stub.activate(activateRequest);
        return activateResponse;
    }

    public AdminClassServer.DeactivateResponse deactivate() {
        AdminClassServer.DeactivateRequest deactivateRequest = AdminClassServer.DeactivateRequest.newBuilder().build();
        AdminClassServer.DeactivateResponse deactivateResponse = stub.deactivate(deactivateRequest);
        return deactivateResponse;
    }

    @Override
    public final void close() {
        channel.shutdown();
    }
}