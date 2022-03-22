package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;


public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {


    @Override
    public void activate(AdminClassServer.ActivateRequest activateRequest,
                         StreamObserver<AdminClassServer.ActivateResponse> activateResponse) {

        // TODO !!

    }

    @Override
    public  void deactivate(AdminClassServer.DeactivateRequest deactivateRequest,
                            StreamObserver<AdminClassServer.DeactivateResponse> deactivateResponse) {

        // TODO !!


    }

    @Override
    public void activateGossip(AdminClassServer.ActivateGossipRequest activateGossipRequest,
                               StreamObserver<AdminClassServer.ActivateGossipResponse> activateGossipResponse) {

        // TODO !!

    }

    @Override
    public void deactivateGossip(AdminClassServer.DeactivateGossipRequest deactivateGossipRequest,
                                 StreamObserver<AdminClassServer.DeactivateGossipResponse> deactivateGossipResponse) {

        // TODO !!
    }

    @Override
    public void gossip(AdminClassServer.GossipRequest gossipRequest,
                       StreamObserver<AdminClassServer.GossipResponse> gossipResponse) {

        // TODO !!

    }

    @Override
    public void dump(AdminClassServer.DumpRequest dumpRequest, StreamObserver<AdminClassServer.DumpResponse> dumpResponse) {

        // PHASE 1

    }

}