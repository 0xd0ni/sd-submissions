package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.*;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;

import java.util.logging.Logger;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {


    private static final Logger LOGGER = Logger.getLogger(AdminServiceImpl.class.getName());

    private ClassState _class;

    private ServerInstance server;

    private final boolean DEBUG_VALUE;


    public AdminServiceImpl(ServerInstance server,ClassState classState, boolean debugValue,String type,String host, String port) {
        this._class = classState;
        this.server = server;
        server.setHost(host);
        server.setPort(port);
        server.setTurmasRep(_class);
        server.setType(type);
        server.setActivityStatus(true);
        this.DEBUG_VALUE = debugValue;

    }


    @Override
    public void activate(ActivateRequest activateRequest,
                         StreamObserver<ActivateResponse> responseObserver) {

        debug("activate...");

        if(server.getActivityStatus()) {
            debug(" 'activate' building the response [server already active]");
            ActivateResponse response = ActivateResponse.newBuilder().setCode(ResponseCode.ACTIVE_SERVER).build();

            debug(" 'activate' responding to the request");
            responseObserver.onNext(response);

        } else {
            server.setActivityStatus(true);
            ActivateResponse response  = ActivateResponse.newBuilder().setCode(ResponseCode.OK).build();

            debug(" 'activate' responding to the request");
            responseObserver.onNext(response);

        }
        responseObserver.onCompleted();
        debug(" 'activate' completed");

    }


    @Override
    public  void deactivate(DeactivateRequest deactivateRequest,
                            StreamObserver<DeactivateResponse> responseObserver) {

        debug("deactivate...");
        if(server.getActivityStatus()) {

            server.setActivityStatus(false);

            debug(" 'deactivate' building the response ");
            DeactivateResponse response = DeactivateResponse.newBuilder().setCode(ResponseCode.OK).build();


            debug(" 'deactivate' responding to the request");
            responseObserver.onNext(response);

        } else {

            DeactivateResponse response = DeactivateResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build();

            debug(" 'deactivate' responding to the request");
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
        debug(" 'deactivate' completed");

    }


    @Override
    public void activateGossip(ActivateGossipRequest activateGossipRequest,
                               StreamObserver<ActivateGossipResponse> responseObserver) {

        debug("activateGossip...");

    }


    @Override
    public void deactivateGossip(DeactivateGossipRequest deactivateGossipRequest,
                                 StreamObserver<DeactivateGossipResponse> responseObserver) {
        debug("deactivateGossip...");

    }


    @Override
    public void gossip(GossipRequest gossipRequest,
                       StreamObserver<GossipResponse> responseObserver) {

        debug("gossip...");

    }


    @Override
    public void dump(DumpRequest dumpRequest, StreamObserver<DumpResponse> responseObserver) {

        debug("dump...");

        debug(" 'dump' building the response");
        DumpResponse response = DumpResponse.newBuilder().setCode(ResponseCode.OK).
                setClassState(ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).
                        setOpenEnrollments(_class.getOpenEnrollments()).
                        addAllEnrolled(Utils.StudentWrapper(_class.getEnrolled())).
                        addAllDiscarded(Utils.StudentWrapper(_class.getDiscarded()))).build();

        debug(" 'dump' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        debug(" 'dump' completed");

    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}