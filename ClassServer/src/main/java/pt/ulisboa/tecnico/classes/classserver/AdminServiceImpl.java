package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {

    private ClassState _class;

    public AdminServiceImpl(ClassState _class) {
        this._class = _class;
    }


    @Override
    public void activate(AdminClassServer.ActivateRequest activateRequest,
                         StreamObserver<AdminClassServer.ActivateResponse> responseObserver) {



    }

    @Override
    public  void deactivate(AdminClassServer.DeactivateRequest deactivateRequest,
                            StreamObserver<AdminClassServer.DeactivateResponse> responseObserver) {
        
        
    }

    @Override
    public void activateGossip(AdminClassServer.ActivateGossipRequest activateGossipRequest,
                               StreamObserver<AdminClassServer.ActivateGossipResponse> responseObserver) {

        // TODO !!

    }

    @Override
    public void deactivateGossip(AdminClassServer.DeactivateGossipRequest deactivateGossipRequest,
                                 StreamObserver<AdminClassServer.DeactivateGossipResponse> responseObserver) {

        // TODO !!
    }

    @Override
    public void gossip(AdminClassServer.GossipRequest gossipRequest,
                       StreamObserver<AdminClassServer.GossipResponse> responseObserver) {

        // TODO !!

    }

    @Override
    public void dump(AdminClassServer.DumpRequest dumpRequest, StreamObserver<AdminClassServer.DumpResponse> responseObserver) {

        AdminClassServer.DumpResponse response = AdminClassServer.DumpResponse.newBuilder().setCode(
                ClassesDefinitions.ResponseCode.OK).setClassState(
                ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).setOpenEnrollments(
                        _class.getOpenEnrollments()).addAllEnrolled(Utils.StudentWrapper(
                        _class.getEnrolled())).addAllDiscarded(Utils.StudentWrapper(
                        _class.getDiscarded()))).build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}