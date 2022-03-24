package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.classserver.exception.ClassesException;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.classserver.exception.ArgumentsValidation;
import pt.ulisboa.tecnico.classes.Stringify;

import java.util.Optional;

import static io.grpc.Status.INVALID_ARGUMENT;

public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {

    private ClassState _class;

    public ProfessorServiceImpl(ClassState _class) {
        this._class = _class;

    }

    @Override
    public void openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest request,
                                StreamObserver<ProfessorClassServer.OpenEnrollmentsResponse> responseObserver) {

        Integer capacity = request.getCapacity();

        try{
            alreadyOpened();
            validCapacity(capacity);
            _class.setCapacity(capacity.intValue());
            _class.setOpenEnrollments(true);
            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch(ClassesException e) {
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());

        }
    }

    @Override
    public void closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest closeRequest,
                                 StreamObserver<ProfessorClassServer.CloseEnrollmentsResponse> responseObserver) {

        try{
            alreadyClosed();
            _class.setOpenEnrollments(false);

            ProfessorClassServer.CloseEnrollmentsResponse response =
                    ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch (ClassesException e) {
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void listClass(ProfessorClassServer.ListClassRequest listRequest,
                          StreamObserver<ProfessorClassServer.ListClassResponse> responseObserver) {


    }

    @Override
    public  void cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest cancelRequest,
                                  StreamObserver<ProfessorClassServer.CancelEnrollmentResponse> responseObserver) {

        String studentId = cancelRequest.getStudentId();

        try{

            CheckForUserExistence(studentId);
            Student student = _class.getRegistered().get(studentId);
            _class.getEnrolled().remove(student);
            _class.getRegistered().remove(studentId);
            _class.addDiscard(student);


            ProfessorClassServer.CancelEnrollmentResponse response =
                    ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (ClassesException e) {
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }


    }

    // error handling methods ( // To refactor)

    public void validCapacity(int capacity) throws ClassesException {
        if(capacity <= 0) {
            throw new ClassesException(ArgumentsValidation.INVALID_CAPACITY.label);
        }
    }

    public void alreadyClosed() throws ClassesException {
        if(!_class.getOpenEnrollments()) {
            throw new ClassesException(Stringify.format(ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_CLOSED));
        }
    }

    public void alreadyOpened() throws  ClassesException {
        if(_class.getOpenEnrollments()) {
            throw new ClassesException(Stringify.format(ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_OPENED));
        }
    }

    public void CheckForUserExistence(String studentId) throws ClassesException {

        if(!_class.getRegistered().containsKey(studentId)) {
            throw new ClassesException(Stringify.format(ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT));
        }

    }

}