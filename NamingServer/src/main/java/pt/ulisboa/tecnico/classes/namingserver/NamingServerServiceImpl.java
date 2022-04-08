package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.namingserver.domain.ServerEntry;
import pt.ulisboa.tecnico.classes.namingserver.domain.ServiceEntry;
import pt.ulisboa.tecnico.classes.namingserver.domain.NamingServices;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NamingServerServiceImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase {


    private static final Logger LOGGER = Logger.getLogger(NamingServerServiceImpl.class.getName());

    private final boolean DEBUG_VALUE;

    private NamingServices namingServices;

    public NamingServerServiceImpl(boolean debugValue) {

        this.DEBUG_VALUE = debugValue;
        this.namingServices = new NamingServices();
    }


    public void register(ClassServerNamingServer.RegisterRequest registerRequest,
                         StreamObserver<ClassServerNamingServer.RegisterResponse>  responseObserver) {

        debug( "register...");

        debug(" 'register' obtaining the necessary data to process the request");
        String serviceName = registerRequest.getServiceName();
        String hostPort = registerRequest.getHostPort();
        ArrayList<String> qualifiers = new ArrayList<>(registerRequest.getQualifiersList());

        // general overview
        // [ NamingServices -> (map)[name ] -> ServiceEntry]
        // [ NamingServices -> (map) [ [name ] -> [ ServiceEntry -> [name ] (set )[[serverEntry]]]

        debug( " 'register' creating a new serverEntry");
        ServerEntry serverEntry = new ServerEntry();
        serverEntry.setHostPort(hostPort);
        serverEntry.addQualifier(qualifiers.get(0));

        debug(" 'register' creating a new ServiceEntry");
        // it is not necessary to always create a new ServiceEntry
        // we have to verify if there's already a ServiceEntry that satisfies our needs (same service name)
        if(namingServices.checkForExistenceOfService(serviceName)) {
            namingServices.getServiceEntry(serviceName).setEntry(serverEntry);

        } else {

            ServiceEntry serviceEntry = new ServiceEntry();
            serviceEntry.setServiceName(serviceName);
            serviceEntry.setEntry(serverEntry);
            debug(" 'register' adding a new entry to namingServices");
            namingServices.addEntry(serviceName,serviceEntry);

        }

        debug(" 'register' building the response");
        ClassServerNamingServer.RegisterResponse response = ClassServerNamingServer.
                RegisterResponse.
                newBuilder().
                setMessage(" ").
                build();

        debug(" 'register' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void lookup(ClassServerNamingServer.LookupRequest lookupRequest,
                       StreamObserver<ClassServerNamingServer.LookupResponse> responseObserver) {

        debug( "lookup...");
        String serviceName = lookupRequest.getServiceName();
        String qualifier = new ArrayList<>(lookupRequest.getQualifiersList()).get(0);

        debug(" 'lookup' checking for service existence");
        if(!namingServices.checkForExistenceOfService(serviceName)) {
            ArrayList<ServerEntry> servers = new ArrayList<>();

            debug(" 'lookup' building the response");
            ClassServerNamingServer.LookupResponse response =
                    ClassServerNamingServer.LookupResponse.newBuilder().addAllServer(
                            ServerEntry.protoList(servers)).build();

            debug(" 'lookup' responding to the request");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } else {

            debug(" 'lookup' filtering the services ");
            ArrayList<ServerEntry> servers = namingServices.getServiceEntry(serviceName).getEntries().stream()
                    .filter(server -> server.hasQualifier(qualifier))
                    .collect(Collectors.toCollection(ArrayList::new));

            debug(" 'lookup' building the response");
            ClassServerNamingServer.LookupResponse response = ClassServerNamingServer.LookupResponse.newBuilder().addAllServer(
                    ServerEntry.protoList(servers)).build();

            debug(" 'lookup' responding to the request");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public void delete(ClassServerNamingServer.DeleteRequest deleteRequest,
                       StreamObserver<ClassServerNamingServer.DeleteResponse> responseObserver) {
        debug( "delete...");


        String serviceName = deleteRequest.getServiceName();
        String server = deleteRequest.getHostPort();


        if (!namingServices.checkForExistenceOfService(serviceName) || !namingServices.getServiceEntry(serviceName).hasEntry(server)) {

                ClassServerNamingServer.DeleteResponse response =
                        ClassServerNamingServer.DeleteResponse.newBuilder()
                                .setMessage("Error: Couldn't delete server, server doesn't exist")
                                .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
        }
        else {
                namingServices.getServiceEntry(serviceName).getEntries().removeIf(entry -> entry.getHostPort().equals(server));

                ClassServerNamingServer.DeleteResponse response =
                        ClassServerNamingServer.DeleteResponse.newBuilder()
                                .setMessage("Successfully deleted Server")
                                .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }



    }

    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}
