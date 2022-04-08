package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.ServerInstance;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerClassServer;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer;
import sun.misc.Signal;
import java.util.*;
import java.io.IOException;
import java.util.TimerTask;

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


      // register the server @ NamingServer
      NamingServerFrontend namingServerFrontend = new NamingServerFrontend(host,Integer.parseInt(port));

      ClassServerNamingServer.RegisterRequest request =
              ClassServerNamingServer.
                      RegisterRequest.
                      newBuilder().
                      setServiceName("turmas").setHostPort(host + ":" + port).setQualifiers(0,serverFlag).
                      build();

      namingServerFrontend.register(request);

      ServerInstance serverInstance = new ServerInstance();
      ClassState _class = new ClassState();


      // Create a new server with multiple services to listen on port.
      Server server = ServerBuilder.forPort(Integer.parseInt(port)).addService(
              new AdminServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).addService(
                      new ProfessorServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).addService(
                              new StudentServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).build();


      // Initialize timer for propagating replica
      Timer timer = new Timer();
      // Start the server.
      server.start();

      System.out.println("Server started");

      if(!serverFlag.equals("S")) {
          ClassServerToServerFrontend serversCommunication  = new ClassServerToServerFrontend(serverFlag);

          ClassServerNamingServer.LookupRequest requestServer =
                  ClassServerNamingServer.
                          LookupRequest.
                          newBuilder().
                          setServiceName("turmas").
                          addQualifiers("S").
                          build();

          ClassServerNamingServer.LookupResponse response = namingServerFrontend.lookup(requestServer);

          ClassesDefinitions.ServerEntry entry = response.getServer(0);

          String host = entry.getHostPort().split(":")[0];
          int port = Integer.parseInt(entry.getHostPort().split(":")[1]);

          serversCommunication.setupServer(host,port);

          timer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  ClassesDefinitions.ClassState classState =
                          ClassesDefinitions.ClassState.newBuilder().
                                  setCapacity(_class.getCapacity()).setOpenEnrollments(_class.getOpenEnrollments()).
                                  addAllEnrolled(Utils.StudentWrapper(_class.getEnrolled())).
                                  addAllDiscarded(Utils.StudentWrapper(_class.getDiscarded())).build();

                  ClassServerClassServer.PropagateStateRequest requestPropagate =
                          ClassServerClassServer.
                                  PropagateStateRequest.
                                  newBuilder().setClassState(classState).build();


                  serversCommunication.setPropagate(requestPropagate);
              }
          }, 1*60*1000, 1*60*1000);

      }

      Signal.handle(new Signal("INT"), sig -> {
          System.out.println("\nShutting down the server");
          server.shutdown();
          System.exit(0);
      });

      // Wait for server termination.
      server.awaitTermination();


  }

}
