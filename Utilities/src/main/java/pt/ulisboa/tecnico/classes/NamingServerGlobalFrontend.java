package pt.ulisboa.tecnico.classes;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.RegisterRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.RegisterResponse;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.DeleteRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.DeleteResponse;


public abstract class NamingServerGlobalFrontend {

    private final ManagedChannel channel;
    private final NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;



    public NamingServerGlobalFrontend(String host, int port) {
        // Channel is the abstraction to connect to a service endpoint.
        // Let us use plaintext communication because we do not have certificates.
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        // Create a blocking stub.
        this.stub = NamingServerServiceGrpc.newBlockingStub(channel);
    }

    public abstract RegisterResponse register(RegisterRequest request);

    public LookupResponse lookup(LookupRequest request) {
        return stub.lookup(request);

    }
    public abstract DeleteResponse delete(DeleteRequest request);

    public ManagedChannel getChannel() {
        return channel;
    }

    public NamingServerServiceGrpc.NamingServerServiceBlockingStub getStub() {
        return stub;
    }
}
