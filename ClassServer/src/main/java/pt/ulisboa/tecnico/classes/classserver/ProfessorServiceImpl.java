package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;

import java.util.logging.Logger;

public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {


    private static final Logger LOGGER = Logger.getLogger(ProfessorServiceImpl.class.getName());

    private ServerInstance server;

    private ClassState _class;

    private final boolean DEBUG_VALUE;

    public ProfessorServiceImpl(ServerInstance server,ClassState _class, boolean debugValue,String type,String host, String port) {
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
    public synchronized void openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest request,
                                StreamObserver<ProfessorClassServer.OpenEnrollmentsResponse> responseObserver) {

        debug("openEnrollments");

        debug(" 'closeEnrollments' checking for secondary server");
        if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {

            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.WRITING_NOT_SUPPORTED).build();

        }

        Integer capacity = request.getCapacity();


        debug(" 'openEnrollment' performing validations");
        if(_class.getOpenEnrollments()) {

            debug(" 'openEnrollment' building the response");
            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_OPENED).build();
            debug(" 'openEnrollment' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        if(capacity <= 0) {
            debug(" 'openEnrollment' building the response");
            ProfessorClassServer.OpenEnrollmentsResponse response =
                    ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.UNRECOGNIZED).build();

            debug(" 'openEnrollment' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }

        debug(" 'openEnrollments' setting the new capacity ");
        _class.setCapacity(capacity.intValue());
        debug(" 'openEnrollments' setting the enrollment status to opened");
        _class.setOpenEnrollments(true);
        debug(" 'openEnrollments' setting the internal capacity of a class");
        _class.setCurrentCapacity(0);

        debug(" 'openEnrollments' building the response");
        ProfessorClassServer.OpenEnrollmentsResponse response =
                ProfessorClassServer.OpenEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

        debug(" 'openEnrollments' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }

    @Override
    public synchronized void closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest closeRequest,
                                 StreamObserver<ProfessorClassServer.CloseEnrollmentsResponse> responseObserver) {

        debug("closeEnrollments");

        debug(" 'closeEnrollments' checking for secondary server");
        if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {

            ProfessorClassServer.CloseEnrollmentsResponse response =
                    ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.WRITING_NOT_SUPPORTED).build();

        }

        debug(" 'closeEnrollments' performing validation");
        if(!_class.getOpenEnrollments()) {

            debug(" 'closeEnrollments' building the response [due to validation]");
            ProfessorClassServer.CloseEnrollmentsResponse response =
                    ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build();

            debug(" 'closeEnrollments' responding to the request");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }


        debug(" 'closeEnrollments'  setting enrollment status");
        _class.setOpenEnrollments(false);

        debug(" 'closeEnrollments building the response");
        ProfessorClassServer.CloseEnrollmentsResponse response =
                ProfessorClassServer.CloseEnrollmentsResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();


        debug(" 'closeEnrollments responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        

    }


    @Override
    public synchronized void listClass(ProfessorClassServer.ListClassRequest listRequest,
                          StreamObserver<ProfessorClassServer.ListClassResponse> responseObserver) {

        debug("listClass");


        debug(" 'listClass' performs building the response");
        ProfessorClassServer.ListClassResponse response = ProfessorClassServer.ListClassResponse.newBuilder().setCode(
              ClassesDefinitions.ResponseCode.OK).setClassState(
                      ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).setOpenEnrollments(
                              _class.getOpenEnrollments()).addAllEnrolled(Utils.StudentWrapper(
                                      _class.getEnrolled())).addAllDiscarded(Utils.StudentWrapper(
                                              _class.getDiscarded()))).build();


        debug(" 'listClass' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public synchronized void cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest cancelRequest,
                                  StreamObserver<ProfessorClassServer.CancelEnrollmentResponse> responseObserver) {

        debug("cancelEnrollments...");

        try {

            debug(" 'closeEnrollments' checking for secondary server");
            if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {

                ProfessorClassServer.CancelEnrollmentResponse response =
                        ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(
                                ClassesDefinitions.ResponseCode.WRITING_NOT_SUPPORTED).build();

            }

            debug(" 'cancelEnrollments' performs validation");
            String studentId = cancelRequest.getStudentId();
            if(!Utils.CheckForUserExistence(studentId,_class)) {

                ProfessorClassServer.CancelEnrollmentResponse response =
                        ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(
                                ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT).build();


                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }
            debug(" 'cancelEnrollments' performing the program's logic");

            debug(" 'cancelEnrollments' obtaining the student ");
            Student student = _class.getRegistered().get(studentId);
            debug(" 'cancelEnrollments' removing the student from the enrolled list");
            _class.getEnrolled().remove(student);
            debug(" 'cancelEnrollments' removing the student from the system registry");
            _class.getRegistered().remove(studentId);
            debug(" 'cancelEnrollments' adding the student to the discarded list ");
            _class.addDiscard(student);
            debug(" 'cancelEnrollments' updating the total number of enrolled students");
            _class.downEnrolled();


            debug(" 'cancelEnrollments' building the response ");
            ProfessorClassServer.CancelEnrollmentResponse response =
                    ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(ClassesDefinitions.ResponseCode.OK).build();

            debug(" 'cancelEnrollments' responding to the request...");
            responseObserver.onNext(response);
            responseObserver.onCompleted();


        }catch(NullPointerException e) {
            debug("Exception was thrown while executing 'cancelEnrollment ");

            debug(" 'cancelEnrollments' building the response ");
            ProfessorClassServer.CancelEnrollmentResponse response =
                    ProfessorClassServer.CancelEnrollmentResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT).build();

            debug(" 'cancelEnrollments' responding to the request [failed] ");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch(IllegalStateException e){
            debug("Exception was thrown while executing 'cancelEnrollment ");
            System.out.printf("");
        }


    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}