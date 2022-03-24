package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;

import java.util.stream.Stream;

public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {

    private ClassState _class;

    public ProfessorServiceImpl(ClassState _class) {
        this._class = _class;
    }

    // TODO

    @Override
    public void openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest request,
                                StreamObserver<ProfessorClassServer.OpenEnrollmentsResponse> response) {
        // TODO
    }

    @Override
    public void closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest closeRequest,
                                 StreamObserver<ProfessorClassServer.CloseEnrollmentsResponse> response) {
        // TODO
    }

    @Override
    public void listClass(ProfessorClassServer.ListClassRequest listRequest,
                          StreamObserver<ProfessorClassServer.ListClassResponse> listResponse) {
        // TODO

    }

    @Override
    public  void cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest cancelRequest,
                                  StreamObserver<ProfessorClassServer.CancelEnrollmentResponse> cancelResponse) {
        // TODO

    }

}