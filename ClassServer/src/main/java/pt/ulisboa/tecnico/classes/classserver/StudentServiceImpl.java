package pt.ulisboa.tecnico.classes.classserver;


import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer;


public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {


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