package pt.ulisboa.tecnico.classes.student;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupRequest;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.LookupResponse;


public class StudentFrontend implements AutoCloseable {
    private final ManagedChannel channel;
    private ManagedChannel channel_specific;
    private final StudentServiceGrpc.StudentServiceBlockingStub stub;
    private StudentServiceGrpc.StudentServiceBlockingStub stub_specific;

    public StudentFrontend(String host, int port) {
        // Channel is the abstraction to connect to a service endpoint.
        // Let us use plaintext communication because we do not have certificates.
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

        // Create a blocking stub.
        stub = StudentServiceGrpc.newBlockingStub(channel);
    }

    public int getCode(ListClassResponse response) {
        return response.getCodeValue();
    }

    public int getCodeE(EnrollResponse response) { return response.getCodeValue(); }

    public ClassState getClassState(ListClassResponse response) {
        return response.getClassState();
    }

    public ListClassResponse setListClass(ListClassRequest request)
    {
        return stub_specific.listClass(request);
    }

    public EnrollResponse setEnroll(EnrollRequest request) { return stub_specific.enroll(request); }

    public boolean checkStudentId(String Id){
        try {
            if (!Id.substring(0, 4).equals("aluno"))
            {
                System.err.println("Error: wrong format for student ID, write aluno + 4 digit number instead of " + Id);
                return false;
            }

            if (Id.substring(5).length() != 4)
                throw new SmallNumberException();

            Integer.parseInt(Id.substring(5));
            return true;

        } catch (NumberFormatException e) {
            System.err.println("Error: wrong format for student ID, write a number with 4 digits");
            return false;
        }
        catch (StringIndexOutOfBoundsException e)
        {
            System.err.println("Error: wrong format for student ID, write aluno + 4 digit number instead of "+Id);
            return false;
        }
        catch (SmallNumberException e)
        {
            System.err.println("Error: wrong format for student ID, number must have 4 digits");
            return false;
        }
    }

    @Override
    public final void close() {
        channel.shutdown();
        channel_specific.shutdown();
    }

    public LookupResponse setLookup(LookupRequest request)
    {
        return stub.lookup(request);
    }

    public void setupSpecificServer(String host,int port) {
        this.channel_specific = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub_specific = StudentServiceGrpc.newBlockingStub(channel_specific);
    }
}