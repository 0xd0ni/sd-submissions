package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;

import java.util.logging.Logger;

public class NamingServerFrontend extends NamingServerGlobalFrontend {


    private static final Logger LOGGER = Logger.getLogger(NamingServerFrontend.class.getName());

    private final boolean DEBUG_VALUE;


    public NamingServerFrontend(String host, int port, boolean debug_value) {
        super(host,port);
        this.DEBUG_VALUE = debug_value;

    }


    @Override
    public ClassServerNamingServer.RegisterResponse register(ClassServerNamingServer.RegisterRequest request) {
        debug("NamingServerFrontend... processing the register request");
        return super.getStub().register(request);

    }

    @Override
    public  ClassServerNamingServer.LookupResponse lookup(ClassServerNamingServer.LookupRequest request) {
        debug("NamingServerFrontend... processing the lookup request");
        return super.lookup(request);

    }

    @Override
    public ClassServerNamingServer.DeleteResponse delete(ClassServerNamingServer.DeleteRequest request) {
        debug("NamingServerFrontend... processing the delete request");
        return super.getStub().delete(request);

    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }
}