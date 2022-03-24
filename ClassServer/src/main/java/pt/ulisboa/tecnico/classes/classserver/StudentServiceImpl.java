package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;
import static io.grpc.Status.INVALID_ARGUMENT;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.exception.ClassesException;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;




public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private ClassState _class;

    public StudentServiceImpl(ClassState _class) {
        this._class = _class;
    }

    @Override
    public void listClass(StudentClassServer.ListClassRequest listClassRequest,
                          StreamObserver<StudentClassServer.ListClassResponse> responseObserver) {

        // TODO !!

    }

    @Override
    public  void enroll(StudentClassServer.EnrollRequest enrollRequest,
                        StreamObserver<StudentClassServer.EnrollResponse> responseObserver) {

        ClassesDefinitions.Student toEnroll = enrollRequest.getStudent();
        String studentName = toEnroll.getStudentName();
        String studentId = toEnroll.getStudentId();

        
        try { 
            Student student = new Student(studentId,studentName);

            // verifies if the student is already enrolled
            _class.containsStudent(student);

            _class.addEnroll(student);

            responseObserver.onNext(StudentClassServer.EnrollResponse.newBuilder().setCode(
                    ClassesDefinitions.ResponseCode.OK).build());
            responseObserver.onCompleted();

        } catch (ClassesException e) {
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }

    }

}