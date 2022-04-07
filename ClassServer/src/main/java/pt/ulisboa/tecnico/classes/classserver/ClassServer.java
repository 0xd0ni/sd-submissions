package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;
import java.io.IOException;

public class ClassServer {

  private static String port;
  private static String host;
  private static String serverFlag;
  private static String debugInput;
  private static final String debug = "-debug";

      public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println(ClassServer.class.getSimpleName());
        System.out.printf("Received %d Argument(s)%n", args.length);
        for (int i = 0; i < args.length; i++) {
          System.out.printf("args[%d] = %s%n", i, args[i]);

        }

        if (args.length < 3) {
          System.err.println("Argument(s) missing!");
          System.err.printf("Usage: java %s port%n", ClassServer.class.getName());
          return;
        }

        boolean debugFlag = false;
        if(args.length == 3) {
            host = args[0];
            port = args[1];;
            serverFlag = args[2];

        } else if( args.length == 4) {
            host = args[0];
            port = args[1];
            serverFlag = args[2];
            debugInput = args[3];
            if(debugInput.equals(debug)) {
              debugFlag = true;
            }

        }


        // we have to register the server in the NamingServer


        ServerInstance serverInstance = new ServerInstance();
        ClassState _class = new ClassState();

        // Create a new server with multiple services to listen on port.
        Server server = ServerBuilder.forPort(Integer.parseInt(port)).addService(
                new AdminServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).addService(
                new ProfessorServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).addService(
                        new StudentServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).build();

        // Start the server.
        server.start();

        System.out.println("Server started");

        // Wait for server termination.
        server.awaitTermination();


  }

}
