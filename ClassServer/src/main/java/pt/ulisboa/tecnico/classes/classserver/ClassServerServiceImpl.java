package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;

import java.util.List;
import java.util.logging.Logger;

public class ClassServerServiceImpl extends ClassServerServiceGrpc.ClassServerServiceImplBase {

    private static final Logger LOGGER = Logger.getLogger(ClassServerServiceImpl.class.getName());

    private ServerInstance server;

    private ClassState _class;

    private final boolean DEBUG_VALUE;

    public ClassServerServiceImpl(ServerInstance server, ClassState _class, boolean debugValue, String type, String host, String port) {
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
    public void propagateState(ClassServerClassServer.PropagateStateRequest propagateStateRequest,
                               StreamObserver<ClassServerClassServer.PropagateStateResponse> responseObserver) {

        debug("propagateState...");

        debug(" 'propagateState' checking for server Activity Status");
        if(!server.getActivityStatus()) {

            debug(" 'propagateState' building the response");
            ClassServerClassServer.PropagateStateResponse response =
                    ClassServerClassServer.PropagateStateResponse.newBuilder().setCode(
                            ClassesDefinitions.ResponseCode.INACTIVE_SERVER).build();

            debug(" 'propagateState' responding to the request [failed]");
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            debug(" 'propagateState' completed");

        }

        debug(" 'propagateState' building the Class State");
        ClassesDefinitions.ClassState classState = propagateStateRequest.getClassState();
        ClassState updated = new ClassState();

        List<Student> studentList = Utils.studentAll(classState.getEnrolledList());
        updated.setCapacity(classState.getCapacity());
        updated.setOpenEnrollments(classState.getOpenEnrollments());
        updated.setEnrolled(Utils.studentAll(classState.getEnrolledList()));
        updated.setDiscarded(Utils.studentAll(classState.getDiscardedList()));
        updated.setCurrentCapacity(studentList.size());

        server.setTurmasRep(updated);

        debug(" 'propagateState' building the response");
        ClassServerClassServer.PropagateStateResponse response =
                ClassServerClassServer.PropagateStateResponse.newBuilder().setCode(
                        ClassesDefinitions.ResponseCode.OK).build();

        debug(" 'propagateState' responding to the request");
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        debug(" 'propagateState' completed");

    }


    public void debug(String msg) {
        if(DEBUG_VALUE) {
            LOGGER.info(msg);
        }
    }

}