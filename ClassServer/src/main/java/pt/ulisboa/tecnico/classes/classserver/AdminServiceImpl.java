package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.*;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer.*;

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
        if(server.getActivityStatus()) {
            if (server.getGossipFlag())
            {
                debug(" 'activateGossip' building the response [gossip already active]");
                ActivateGossipResponse response = ActivateGossipResponse.newBuilder()
                        .setCode(ResponseCode.GOSSIP_ACTIVATED).build();

                debug(" 'activateGossip' responding to the request");
                responseObserver.onNext(response);
            }
            else
            {
                server.setGossipFlag(true);
                debug(" 'activateGossip' building the response");
                ActivateGossipResponse response = ActivateGossipResponse.newBuilder()
                        .setCode(ResponseCode.OK).build();

                debug(" 'activateGossip' responding to the request");
                responseObserver.onNext(response);
            }

        } else {
            debug(" 'ActivateGossip' Inactive server");
            ActivateGossipResponse response  = ActivateGossipResponse.newBuilder()
                    .setCode(ResponseCode.INACTIVE_SERVER).build();

            debug(" 'activateGossip' responding to the request");
            responseObserver.onNext(response);

        }
        responseObserver.onCompleted();
        debug(" 'activateGossip' completed");
    }


    @Override
    public void deactivateGossip(DeactivateGossipRequest deactivateGossipRequest,
                                 StreamObserver<DeactivateGossipResponse> responseObserver)
    {
        if(server.getActivityStatus()) {
            if (!server.getGossipFlag())
            {
                debug(" 'DeactivateGossip' building the response [gossip already deactivated]");
                DeactivateGossipResponse response = DeactivateGossipResponse.newBuilder()
                        .setCode(ResponseCode.GOSSIP_DEACTIVATED).build();

                debug(" 'DeactivateGossip' responding to the request");
                responseObserver.onNext(response);
            }
            else
            {
                server.setGossipFlag(false);
                debug(" 'DeactivateGossip' building the response");
                DeactivateGossipResponse response = DeactivateGossipResponse.newBuilder()
                        .setCode(ResponseCode.OK).build();

                debug(" 'DeactivateGossip' responding to the request");
                responseObserver.onNext(response);
            }

        } else {
            debug(" 'DeactivateGossip' inactive server");
            DeactivateGossipResponse response  = DeactivateGossipResponse.newBuilder()
                    .setCode(ResponseCode.INACTIVE_SERVER).build();

            debug(" 'DeactivateGossip' responding to the request");
            responseObserver.onNext(response);

        }
        responseObserver.onCompleted();
        debug(" 'DeactivateGossip' completed");
    }


    @Override
    public void gossip(GossipRequest gossipRequest,
                       StreamObserver<GossipResponse> responseObserver) {

        debug("gossip...");
        System.out.println("A COMEÇAR O GOSSIP");
        if (server.getActivityStatus()) {
            ClassesDefinitions.ClassState estado =
                    ClassesDefinitions.ClassState.newBuilder()
                            .setCapacity(server.getTurmasRep().getCapacity())
                            .setOpenEnrollments(server.getTurmasRep().getOpenEnrollments())
                            .addAllEnrolled(Utils.StudentWrapper(server.getTurmasRep().getEnrolled()))
                            .addAllDiscarded(Utils.StudentWrapper(server.getTurmasRep().getDiscarded()))
                            .build();

            debug("Copy of ClassState created 1");
            debug("Creating PropagateState Request 1");

            PropagateStateRequest requestPropagate =
                    PropagateStateRequest.newBuilder()
                            .setClassState(estado)
                            .build();

            debug("PropagateState Request created 1");
            debug("Calling PropagateState 1");
            System.out.println("A CHAMAR O PROPAGATE REQUEST");
            PropagateStateResponse res = server.getServersCommunication().setPropagate(requestPropagate);
            System.out.println("CHAMOU O PROPAGATE REQUEST");
            debug("State successfully propagated IN GOSSIP");
            GossipResponse response  = GossipResponse.newBuilder()
                    .setCode(ResponseCode.OK).build();

            debug(" 'Gossip' responding to the request");
            responseObserver.onNext(response);
        }
        else {
            debug(" 'Gossip' inactive server");
            GossipResponse response  = GossipResponse.newBuilder()
                    .setCode(ResponseCode.INACTIVE_SERVER).build();

            debug(" 'Gossip' responding to the request");
            responseObserver.onNext(response);

        }
        responseObserver.onCompleted();
        debug(" 'Gossip' completed");
    }


    @Override
    public void dump(DumpRequest dumpRequest, StreamObserver<DumpResponse> responseObserver) {

        debug("dump...");

        debug(" 'dump' building the response");
        if (server.getActivityStatus()) {
            DumpResponse response = DumpResponse.newBuilder()
              .setCode(ResponseCode.OK)
              .setClassState(
                  ClassesDefinitions.ClassState.newBuilder()
                      .setCapacity(server.getTurmasRep().getCapacity())
                      .setOpenEnrollments(server.getTurmasRep().getOpenEnrollments())
                      .addAllEnrolled(Utils.StudentWrapper(server.getTurmasRep().getEnrolled()))
                      .addAllDiscarded(Utils.StudentWrapper(server.getTurmasRep().getDiscarded())))
              .build();
            debug(" 'dump' responding to the request");
            responseObserver.onNext(response);
        }
        else
        {
            debug(" 'dump' Inactive server");
            debug(" 'dump' building the response");
            DumpResponse response  = DumpResponse.newBuilder()
                    .setCode(ResponseCode.INACTIVE_SERVER).build();

            debug(" 'DeactivateGossip' responding to the request");
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
        debug(" 'dump' completed");
    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}