package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;


public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private ClassState _class;

    public StudentServiceImpl(ClassState _class) {
        this._class = _class;

    }


    @Override
    public void listClass(StudentClassServer.ListClassRequest listClassRequest,
                          StreamObserver<StudentClassServer.ListClassResponse> responseObserver) {

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

        ClassesDefinitions.Student toEnroll = enrollRequest.getStudent();
        String studentName = toEnroll.getStudentName();
        String studentId = toEnroll.getStudentId();


        Student student = new Student(studentId,studentName);

        // verifies if the student is already enrolled
        // and informs the client
        if(Utils.CheckForUserExistence(studentId,_class)) {
            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.STUDENT_ALREADY_ENROLLED).build());
            responseObserver.onCompleted();


        }

        _class.addEnroll(student);
        _class.addToRegistry(studentId,student);

        responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                ClassesDefinitions.ResponseCode.OK).build());
        responseObserver.onCompleted();

    }

}