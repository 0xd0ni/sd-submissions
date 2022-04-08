package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.NamingServerGlobalFrontend;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;

public class NamingServerFrontend extends NamingServerGlobalFrontend {


    public NamingServerFrontend(String host, int port) {
        super(host,port);
    }


    @Override
    public ClassServerNamingServer.RegisterResponse register(ClassServerNamingServer.RegisterRequest request) {
        return super.getStub().register(request);

    }

    @Override
    public  ClassServerNamingServer.LookupResponse lookup(ClassServerNamingServer.LookupRequest request) {
        return super.lookup(request);

    }

    @Override
    public ClassServerNamingServer.DeleteResponse delete(ClassServerNamingServer.DeleteRequest request) {
        return super.getStub().delete(request);
    }
}