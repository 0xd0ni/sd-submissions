package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;
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
    public synchronized void openEnrollments(OpenEnrollmentsRequest request,
                                StreamObserver<OpenEnrollmentsResponse> responseObserver) {

        debug("openEnrollments");

        debug(" 'openEnrollments' checking for server Activity Status");
        if(!server.getActivityStatus()) {

            OpenEnrollmentsResponse response = OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                    INACTIVE_SERVER).build();

            debug(" 'openEnrollments' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'openEnrollments' completed");



        }

        debug(" 'openEnrollments' checking for secondary server");
        if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {

            OpenEnrollmentsResponse response = OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                    WRITING_NOT_SUPPORTED).build();

            debug(" 'openEnrollments' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'openEnrollments' completed");

        }

        int capacity = request.getCapacity();

        debug(" 'openEnrollments' performing validations");
        if(server.getTurmasRep().getOpenEnrollments()) {

            debug(" 'openEnrollments' building the response");
            OpenEnrollmentsResponse response = OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                    ENROLLMENTS_ALREADY_OPENED).build();

            debug(" 'openEnrollments' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'openEnrollments' completed");

        }
        if(capacity <= 0) {
            debug(" 'openEnrollments' building the response");
            OpenEnrollmentsResponse response = OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                    UNRECOGNIZED).build();

            debug(" 'openEnrollments' responding to the request [due to validation]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'openEnrollments' completed");
        }

        debug(" 'openEnrollments' setting the new capacity ");
        server.getTurmasRep().setCapacity(capacity);
        debug(" 'openEnrollments' setting the enrollment status to opened");
        server.getTurmasRep().setOpenEnrollments(true);
        debug(" 'openEnrollments' setting the internal capacity of a class");
        server.getTurmasRep().setCurrentCapacity(0);

        debug(" 'openEnrollments' building the response");
        OpenEnrollmentsResponse response = OpenEnrollmentsResponse.newBuilder().setCode(ResponseCode.OK).build();

        debug(" 'openEnrollments' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        debug(" 'openEnrollments' completed");

    }


    @Override
    public synchronized void closeEnrollments(CloseEnrollmentsRequest closeRequest,
                                 StreamObserver<CloseEnrollmentsResponse> responseObserver) {

        debug("closeEnrollments");
        try{
            if(!server.getActivityStatus()) {
                debug(" 'closeEnrollments' checking for server Activity Status");

                CloseEnrollmentsResponse response = CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                        INACTIVE_SERVER).build();

                debug(" 'closeEnrollments' responding to the request");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                debug(" 'closeEnrollments' completed");


            }

            if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {
                debug(" 'closeEnrollments' checking for secondary server");

                CloseEnrollmentsResponse response = CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                        WRITING_NOT_SUPPORTED).build();

                debug(" 'closeEnrollments' responding to the request");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                debug(" 'closeEnrollments' completed");

            }

            if(!server.getTurmasRep().getOpenEnrollments()) {
                debug(" 'closeEnrollments' checking for enrollments status");

                debug(" 'closeEnrollments' building the response [due to validation]");
                CloseEnrollmentsResponse response = CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                        ENROLLMENTS_ALREADY_CLOSED).build();

                debug(" 'closeEnrollments' responding to the request");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                debug(" 'closeEnrollments' completed");

            }


            debug(" 'closeEnrollments'  setting enrollment status");
            server.getTurmasRep().setOpenEnrollments(false);

            debug(" 'closeEnrollments building the response");
            CloseEnrollmentsResponse response = CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.OK).build();

            debug(" 'closeEnrollments responding to the request");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'closeEnrollments' completed");

        }catch(NullPointerException e) {
            debug("Exception was thrown while executing 'closeEnrollments ");

            debug(" 'closeEnrollments' building the response ");
            CloseEnrollmentsResponse response = CloseEnrollmentsResponse.newBuilder().setCode(ResponseCode.
                    NON_EXISTING_STUDENT).build();

            debug(" 'closeEnrollments' responding to the request [failed] ");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch(IllegalStateException e){
            debug("Exception was thrown while executing 'closeEnrollments' ");
            System.out.print("");
        }

    }


    @Override
    public synchronized void listClass(ListClassRequest listRequest,
                          StreamObserver<ListClassResponse> responseObserver) {

        debug("listClass");

        if(!server.getActivityStatus()) {

            debug(" 'listClass' checking for server Activity status");

            ListClassResponse response = ListClassResponse.newBuilder().setCode(ResponseCode.INACTIVE_SERVER).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'listClass' completed");

        }

        debug(" 'listClass' performs building the response");
        ListClassResponse response = ListClassResponse.newBuilder().setCode(ResponseCode.OK).
                setClassState(ClassesDefinitions.ClassState.newBuilder().setCapacity(server.getTurmasRep().getCapacity()).
                        setOpenEnrollments(server.getTurmasRep().getOpenEnrollments()).
                        addAllEnrolled(Utils.StudentWrapper(server.getTurmasRep().getEnrolled())).
                        addAllDiscarded(Utils.StudentWrapper(server.getTurmasRep().getDiscarded()))).build();


        debug(" 'listClass' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        debug(" 'listClass' completed");

    }


    @Override
    public synchronized void cancelEnrollment(CancelEnrollmentRequest cancelRequest,
                                  StreamObserver<CancelEnrollmentResponse> responseObserver) {

        debug("cancelEnrollments...");

        try {

            debug(" 'cancelEnrollment' checking for server Activity status");
            if(!server.getActivityStatus()) {
                CancelEnrollmentResponse response = CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.
                        INACTIVE_SERVER).build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }

            String studentId = cancelRequest.getStudentId();

            if(!Utils.CheckForUserExistence(studentId,server.getTurmasRep())) {

                debug(" 'cancelEnrollments' checking for user existence");

                CancelEnrollmentResponse response = CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.
                        NON_EXISTING_STUDENT).build();

                debug(" 'cancelEnrollments' building the response ");
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                debug(" 'cancelEnrollments' completed");

            }
            debug(" 'cancelEnrollments' performing the program's logic");

            debug(" 'cancelEnrollments' obtaining the student ");
            Student student = server.getTurmasRep().getRegistered().get(studentId);
            debug(" 'cancelEnrollments' removing the student from the enrolled list");
            server.getTurmasRep().getEnrolled().remove(student);
            debug(" 'cancelEnrollments' removing the student from the system registry");
            server.getTurmasRep().getRegistered().remove(studentId);
            debug(" 'cancelEnrollments' adding the student to the discarded list ");
            server.getTurmasRep().addDiscard(student);
            debug(" 'cancelEnrollments' updating the total number of enrolled students");
            server.getTurmasRep().downEnrolled();


            debug(" 'cancelEnrollments' building the response ");
            CancelEnrollmentResponse response = CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.OK).build();

            debug(" 'cancelEnrollments' responding to the request...");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'cancelEnrollments' completed");


        }catch(NullPointerException e) {
            debug("Exception was thrown while executing 'cancelEnrollment ");

            debug(" 'cancelEnrollments' building the response ");
            CancelEnrollmentResponse response = CancelEnrollmentResponse.newBuilder().setCode(ResponseCode.
                    NON_EXISTING_STUDENT).build();

            debug(" 'cancelEnrollments' responding to the request [failed] ");
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }
        catch(IllegalStateException e){
            debug("Exception was thrown while executing 'cancelEnrollment ");
            System.out.print("");
        }

    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}