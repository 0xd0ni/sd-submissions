package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import java.util.logging.Logger;


public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private static final Logger LOGGER = Logger.getLogger(StudentServiceImpl.class.getName());

    private ServerInstance server;

    private ClassState _class;

    private final boolean DEBUG_VALUE;

    public StudentServiceImpl(ServerInstance server,ClassState _class, boolean debugValue,String type,String host, String port) {
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
    public void listClass(ListClassRequest listClassRequest,
                          StreamObserver<ListClassResponse> responseObserver) {
        debug("listClass...");

        debug(" 'listClass' checking for server Activity status");
        if(!server.getActivityStatus()) {

            ListClassResponse response = ListClassResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.INACTIVE_SERVER).build();

            debug(" 'listClass' responding to the request");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'listClass' completed");

        }

        debug(" 'listClass' building the response");
        ListClassResponse response = ListClassResponse.newBuilder().setCode(ResponseCode.OK).setClassState(
                ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).setOpenEnrollments(
                        _class.getOpenEnrollments()).addAllEnrolled(Utils.StudentWrapper(
                        _class.getEnrolled())).addAllDiscarded(Utils.StudentWrapper(
                        _class.getDiscarded()))).build();

        debug(" 'listClass' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        debug(" 'listClass' completed");

    }


    @Override
    public  void enroll(EnrollRequest enrollRequest,
                        StreamObserver<EnrollResponse> responseObserver) {

        debug("enroll...");

        debug(" 'enroll' checking for server Activity status");
        if(!server.getActivityStatus()) {

            EnrollResponse response = EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.INACTIVE_SERVER).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'enroll' completed");

        }
        if(server.getType().equals(Utils.ServerSpecification(Server.SECONDARY))) {
            debug(" 'enroll' checking for secondary server");

            EnrollResponse response = EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.WRITING_NOT_SUPPORTED).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'enroll' completed");

        }

        ClassesDefinitions.Student toEnroll = enrollRequest.getStudent();
        String studentName = toEnroll.getStudentName();
        String studentId = toEnroll.getStudentId();

        debug(" 'enroll' creating a new student");
        Student student = new Student(studentId,studentName);

        debug(" 'enroll' performing validations");
        if(!_class.getOpenEnrollments()) {
            debug(" 'enroll' checking for open Enrollments");

            debug(" 'enroll' responding to the request [due to validation]");
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build());
            responseObserver.onCompleted();
            debug(" 'enroll' completed");


        }
        else if(Utils.CheckForUserExistence(studentId,_class)) {
            debug(" 'enroll' checking for user Existence");

            debug(" 'enroll' responding to the request [due to validation]");
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.STUDENT_ALREADY_ENROLLED).build());
            responseObserver.onCompleted();
            debug(" 'enroll' completed");


        }
        else if(_class.checkForFullCapacity()) {
            debug(" 'enroll' checking for full capacity");

            debug(" 'enroll' responding to the request [due to validation]");
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.FULL_CLASS).build());
            responseObserver.onCompleted();
            debug(" 'enroll' completed");
        }
        else {

            debug(" 'enroll' enrolling the student");
            _class.addEnroll(student);
            debug(" 'enroll' adding the student to the system registry");
            _class.addToRegistry(studentId, student);
            debug(" 'enroll' increasing the number of enrolled students");
            _class.upEnrolled();


            debug(" 'enroll' responding to the request");
            responseObserver.onNext(EnrollResponse.newBuilder().setCode(ClassesDefinitions.
                    ResponseCode.OK).build());
            responseObserver.onCompleted();
            debug(" 'enroll' completed");
        }

    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}