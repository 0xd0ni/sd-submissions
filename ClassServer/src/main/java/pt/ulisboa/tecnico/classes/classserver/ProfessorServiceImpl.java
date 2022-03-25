package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;


public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {

    private ClassState _class;

    public ProfessorServiceImpl(ClassState _class) {
        this._class = _class;

    }

    @Override
    public void openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest request,
                                StreamObserver<ProfessorClassServer.OpenEnrollmentsResponse> responseObserver) {

        Integer capacity = request.getCapacity();



        if(_class.getOpenEnrollments()) {
            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_OPENED).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        if(capacity <= 0) {
            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.UNRECOGNIZED).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }


        _class.setCapacity(capacity.intValue());
        _class.setOpenEnrollments(true);
        _class.setCurrentCapacity(0);
        ProfessorClassServer.OpenEnrollmentsResponse response =
                ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }

    @Override
    public synchronized void closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest closeRequest,
                                 StreamObserver<ProfessorClassServer.CloseEnrollmentsResponse> responseObserver) {

        if(!_class.getOpenEnrollments()) {

            ProfessorClassServer.CloseEnrollmentsResponse response =
                    ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }

        _class.setOpenEnrollments(false);

        ProfessorClassServer.CloseEnrollmentsResponse response =
                ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        

    }


    @Override
    public synchronized void listClass(ProfessorClassServer.ListClassRequest listRequest,
                          StreamObserver<ProfessorClassServer.ListClassResponse> responseObserver) {


        ProfessorClassServer.ListClassResponse response = ProfessorClassServer.ListClassResponse.newBuilder().setCode(
              ClassesDefinitions.ResponseCode.OK).setClassState(
                      ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).setOpenEnrollments(
                              _class.getOpenEnrollments()).addAllEnrolled(Utils.StudentWrapper(
                                      _class.getEnrolled())).addAllDiscarded(Utils.StudentWrapper(
                                              _class.getDiscarded()))).build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public synchronized void cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest cancelRequest,
                                  StreamObserver<ProfessorClassServer.CancelEnrollmentResponse> responseObserver) {

        try {

            String studentId = cancelRequest.getStudentId();
            if(!Utils.CheckForUserExistence(studentId,_class)) {

                ProfessorClassServer.CancelEnrollmentResponse response =
                        ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(
                                ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT).build();


                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }

            Student student = _class.getRegistered().get(studentId);
            _class.getEnrolled().remove(student);
            _class.getRegistered().remove(studentId);
            _class.addDiscard(student);
            _class.downEnrolled();


            ProfessorClassServer.CancelEnrollmentResponse response =
                    ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();


        }catch(NullPointerException e) {

            ProfessorClassServer.CancelEnrollmentResponse response =
                    ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT).build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch(IllegalStateException e){
            System.out.printf("");
        }


    }

}