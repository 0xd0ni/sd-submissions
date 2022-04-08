package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;


public class ClassServerToServerFrontend {

    private final String serverFlag;

    private ManagedChannel channel;


    private  ClassServerServiceGrpc.ClassServerServiceBlockingStub stub;


    public ClassServerToServerFrontend(String serverFlag) {
        this.serverFlag = serverFlag;


    }

    public String getServerFlag() {
        return serverFlag;

    }

    public void setupServer(String host,int port) {

        this.channel= ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = ClassServerServiceGrpc.newBlockingStub(channel);
    }

    public ClassServerClassServer.PropagateStateResponse setPropagate(ClassServerClassServer.PropagateStateRequest
                                                                              request) {
        return  stub.propagateState(request);

    }

}