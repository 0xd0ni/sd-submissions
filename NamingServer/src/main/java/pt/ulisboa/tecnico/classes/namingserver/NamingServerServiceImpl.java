package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.classes.namingserver.domain.ServiceEntry;
import pt.ulisboa.tecnico.classes.namingserver.domain.ServiceEntry;
import pt.ulisboa.tecnico.classes.namingserver.domain.NamingServices;
import java.util.logging.Logger;

public class NamingServerServiceImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase {


    private static final Logger LOGGER = Logger.getLogger(NamingServerServiceImpl.class.getName());

    private final boolean DEBUG_VALUE;

    public NamingServerServiceImpl(boolean debugValue) {

        this.DEBUG_VALUE = debugValue;
    }


    public void register(ClassServerNamingServer.RegisterRequest registerRequest,
                         StreamObserver<ClassServerNamingServer.RegisterResponse>  responseObserver) {

        debug( "register...");
        // TODO !!


    }


    public void lookup(ClassServerNamingServer.LookupRequest lookupRequest,
                       StreamObserver<ClassServerNamingServer.LookupResponse> responseObserver) {

        debug( "lookup...");
        // TODO !!
    }

    public void delete(ClassServerNamingServer.DeleteRequest deleteRequest,
                       StreamObserver<ClassServerNamingServer.DeleteResponse> responseObserver) {
        debug( "delete...");
        // TODO !!
    }

    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}
