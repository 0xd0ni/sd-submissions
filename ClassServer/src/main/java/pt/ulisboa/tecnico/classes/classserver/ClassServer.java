package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;

import java.io.IOException;


public class ClassServer {

  private static int port;
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

        if(args.length == 3) {
          try {
            host = args[0];
            port = Integer.parseInt(args[1]);;
            serverFlag = args[2];

          }catch(NumberFormatException e) {
            System.err.println("Error: string given instead of a number");
            System.exit(-1);

          }

        } else if( args.length == 4) {
          try {
            host = args[0];
            port = Integer.parseInt(args[1]);;
            serverFlag = args[2];
            debugInput = args[3];

          }catch(NumberFormatException e) {
            System.err.println("Error: string given instead of a number");
            System.exit(-1);

          }

        }
        
        boolean debugFlag = false;
        ClassState _class = new ClassState();
        if(args.length == 4 && debugInput.equals(debug)) {
            debugFlag = true;
        }

        // Create a new server with multiple services to listen on port.
        Server server = ServerBuilder.forPort(port).addService(new AdminServiceImpl(_class,debugFlag)).addService(
                new ProfessorServiceImpl(_class,debugFlag)).addService(new StudentServiceImpl(_class,debugFlag)).build();

        // Start the server.
        server.start();

        System.out.println("Server started");

        // Wait for server termination.
        server.awaitTermination();

  }

}
