package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;

import java.util.logging.Logger;

public class ClassServerServiceImpl extends ClassServerServiceGrpc.ClassServerServiceImplBase {

    private static final Logger LOGGER = Logger.getLogger(ClassServerServiceImpl.class.getName());

    private ServerInstance server;

    private ClassState _class;

    private final boolean DEBUG_VALUE;

    public ClassServerServiceImpl(ServerInstance server,ClassState _class, boolean debugValue,String type,String host, String port) {
        this._class = _class;
        this.server = server;
        server.setHost(host);
        server.setPort(port);
        server.setTurmasRep(_class);
        server.setType(type);
        server.setActivityStatus(true);
        this.DEBUG_VALUE = debugValue;

    }


    @Override
    public void propagateState(ClassServerClassServer.PropagateStateRequest propagateStateRequest,
                               StreamObserver<ClassServerClassServer.PropagateStateResponse> responseObserver) {

        debug("propagateState...");
        ClassesDefinitions.ClassState classState = propagateStateRequest.getClassState();

        //-propagateState -- um servidor envia o seu estado a outra réplica.


        // como aceder a outra replica ?
        // como é que funciona o envio ?

        // alterar o estado atual da replica e mandar confirmaçã́o
        //





    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}