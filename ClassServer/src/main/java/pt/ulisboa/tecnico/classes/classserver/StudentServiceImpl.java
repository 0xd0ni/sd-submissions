package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;

import java.util.logging.Logger;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {


    private static final Logger LOGGER = Logger.getLogger(AdminServiceImpl.class.getName());
    private ClassState _class;
    private final boolean DEBUG_VALUE;

    public StudentServiceImpl(ClassState _class, boolean debugValue) {
        this._class = _class;
        this.DEBUG_VALUE = debugValue;

    }


    @Override
    public void listClass(StudentClassServer.ListClassRequest listClassRequest,
                          StreamObserver<StudentClassServer.ListClassResponse> responseObserver) {
        debug("listClass...");

        StudentClassServer.ListClassResponse response = StudentClassServer.ListClassResponse.newBuilder().setCode(
                ClassesDefinitions.ResponseCode.OK).setClassState(
                ClassesDefinitions.ClassState.newBuilder().setCapacity(_class.getCapacity()).setOpenEnrollments(
                        _class.getOpenEnrollments()).addAllEnrolled(Utils.StudentWrapper(
                        _class.getEnrolled())).addAllDiscarded(Utils.StudentWrapper(
                        _class.getDiscarded()))).build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public  void enroll(StudentClassServer.EnrollRequest enrollRequest,
                        StreamObserver<StudentClassServer.EnrollResponse> responseObserver) {

        debug("enroll...");

        ClassesDefinitions.Student toEnroll = enrollRequest.getStudent();
        String studentName = toEnroll.getStudentName();
        String studentId = toEnroll.getStudentId();

        Student student = new Student(studentId,studentName);

        if(!_class.getOpenEnrollments()) {
            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.ENROLLMENTS_ALREADY_CLOSED).build());
            responseObserver.onCompleted();


        }
        else if(Utils.CheckForUserExistence(studentId,_class)) {
            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.STUDENT_ALREADY_ENROLLED).build());
            responseObserver.onCompleted();


        }
        else if(_class.checkForFullCapacity()) {
            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.FULL_CLASS).build());
            responseObserver.onCompleted();
        }
        else {
            _class.addEnroll(student);
            _class.addToRegistry(studentId, student);
            _class.upEnrolled();

            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.OK).build());
            responseObserver.onCompleted();
        }

    }

    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}