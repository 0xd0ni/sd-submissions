package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;


public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    private ClassState _class;

    public StudentServiceImpl(ClassState _class) {
        this._class = _class;
    }

    @Override
    public void listClass(StudentClassServer.ListClassRequest listClassRequest,
                          StreamObserver<StudentClassServer.ListClassResponse> listClassResponse) {

        // TODO !!

    }

    @Override
    public  void enroll(StudentClassServer.EnrollRequest enrollRequest,
                        StreamObserver<StudentClassServer.EnrollResponse> enrollResponse) {

        // TODO !!
    }

}