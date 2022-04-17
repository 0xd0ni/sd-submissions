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

import static pt.ulisboa.tecnico.classes.Utilities.*;

public class ClassServer {

  private static String port;
  private static String host;
  private static String serverFlag;
  private static String debugInput;

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
          if(debugInput.equals(DEBUG)) {
              debugFlag = true;
            }
      }


      // register the server @ NamingServer
      NamingServerFrontend namingServerFrontend = new NamingServerFrontend(NAMING_HOST,NAMING_PORT,debugFlag);

      ClassServerNamingServer.RegisterRequest request =
              ClassServerNamingServer.
                      RegisterRequest.
                      newBuilder().
                      setServiceName(SERVICE).setHostPort(host + ":" + port).addQualifiers(serverFlag).build();


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

      if(!serverFlag.equals(SECONDARY)) {
          ClassServerToServerFrontend serversCommunication  = new ClassServerToServerFrontend(serverFlag);

          ClassServerNamingServer.LookupRequest requestServer =
                  ClassServerNamingServer.
                          LookupRequest.
                          newBuilder().
                          setServiceName(SERVICE).
                          addQualifiers(SECONDARY).
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
          }, 60*1000, 60*1000);

      }

      Signal.handle(new Signal(SIGINT), sig -> {
          System.out.println(EXIT_SERVER);
          ClassServerNamingServer.DeleteRequest requestDelete = ClassServerNamingServer.
                  DeleteRequest.
                  newBuilder().
                  setServiceName(SERVICE).
                  setHostPort(host + ":" + port).
                  build();
          namingServerFrontend.delete(requestDelete);

          server.shutdown();
          System.exit(SUCCESS);
      });

      // Wait for server termination.
      server.awaitTermination();


  }

}
