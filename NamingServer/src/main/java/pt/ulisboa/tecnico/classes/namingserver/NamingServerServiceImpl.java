package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
//import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

import java.util.logging.Logger;

public class NamingServerServiceImpl {

    private static final Logger LOGGER = Logger.getLogger(NamingServerServiceImpl.class.getName());
    private final boolean DEBUG_VALUE;


    public NamingServerServiceImpl(boolean debugValue) {
        this.DEBUG_VALUE = debugValue;

    }


    //@Override
    //public synchronized void register(ClassServerNamingServer.registerRequest request,
    //                                 StreamObserver<ClassServerNamingServer.registerResponse> responseObserver) {

    //    debug("register");


    // }






    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}
