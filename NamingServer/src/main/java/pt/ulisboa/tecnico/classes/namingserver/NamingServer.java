package pt.ulisboa.tecnico.classes.namingserver;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.BindableService;

import java.io.IOException;


public class NamingServer {

  private static String port;
  private static String host;
  private static String debugInput;
  private static final String debug = "-debug";

  public static void main(String[] args) throws InterruptedException, IOException {

    System.out.println(NamingServer.class.getSimpleName());
    System.out.printf("Received %d Argument(s)%n", args.length);
    for (int i = 0; i < args.length; i++) {
      System.out.printf("args[%d] = %s%n", i, args[i]);

    }

    if (args.length < 2) {
      System.err.println("Argument(s) missing!");
      System.err.printf("Usage: java %s port%n",NamingServer.class.getName());
      return;
    }

    boolean debugFlag = false;
    if(args.length == 2) {
      host = args[0];
      port = args[1];

    } else if( args.length == 3) {
      host = args[0];
      port = args[1];
      debugInput = args[2];
      if (debugInput.equals(debug)) {
        debugFlag = true;
      }
    }

    // we're assuming that the NamingServer never fails
    final BindableService impl = (new NamingServerServiceImpl(debugFlag));
    // Create a new server with NamingServer service to listen on port.
    Server server = ServerBuilder.forPort(Integer.parseInt(port)).addService(impl).build();

    // Start the server.
    server.start();

    System.out.println("Server started");

    // Wait for server termination.
    server.awaitTermination();

  }
}
