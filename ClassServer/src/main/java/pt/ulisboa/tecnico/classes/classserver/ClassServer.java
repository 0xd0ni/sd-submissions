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
import java.util.logging.Logger;

import static pt.ulisboa.tecnico.classes.Utilities.*;

public class ClassServer {

    private static final Logger LOGGER = Logger.getLogger(ClassServer.class.getName());
    private static String port;
    private static String host;
    private static String serverFlag;
    private static String debugInput;
    static boolean debugFlag = false;

    public static void debug(String msg) {
        if(debugFlag) {
            LOGGER.info(msg);
        }
    }

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
      Server server = ServerBuilder.forPort(Integer.parseInt(port))
              .addService(new AdminServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port))
              .addService(new ProfessorServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port))
              .addService(new StudentServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port))
              .addService(new ClassServerServiceImpl(serverInstance,_class,debugFlag,serverFlag,host,port)).build();


      // Initialize timer for propagating replica
      Timer timer = new Timer();
      // Start the server.
      server.start();

      System.out.println("Server started");

      debug("Creating server communication frontend");
      String flag = "";

      if(serverFlag.equals(SECONDARY))
          flag = PRIMARY;
      else if(serverFlag.equals(PRIMARY))
          flag = SECONDARY;

      debug("Creating server lookup request");
      List<ClassesDefinitions.ServerEntry> lst = new ArrayList<>();
      ClassServerNamingServer.LookupResponse response = null;
      while (lst.isEmpty())
      {
          ClassServerNamingServer.LookupRequest requestServer = ClassServerNamingServer.LookupRequest.newBuilder()
              .setServiceName(SERVICE)
              .addQualifiers(flag)
              .build();
          response = namingServerFrontend.lookup(requestServer);
          lst = response.getServerList();
      }
      debug("Got server lookup response");

      ClassesDefinitions.ServerEntry entry = response.getServer(0);

      String host = entry.getHostPort().split(":")[0];
      int port = Integer.parseInt(entry.getHostPort().split(":")[1]);

      String third_host = null;
      int third_port = 0;
      if (serverFlag.equals(PRIMARY))
      {
          ClassesDefinitions.ServerEntry new_entry = response.getServer(1);
          third_host = entry.getHostPort().split(":")[0];
          third_port = Integer.parseInt(entry.getHostPort().split(":")[1]);
      }

      lst.stream().forEach(serverEntry -> debug("Server found: "+serverEntry.getHostPort()));

      debug("Saving server info");
      debug("Address: "+host+":"+port);
      serverInstance.getServersCommunication().setupServer(host,port);
      debug("Server communication server set up");

      if(serverFlag.equals(SECONDARY)){
      timer.scheduleAtFixedRate(
          new TimerTask() {
            @Override
            public void run() {
              if (!serverInstance.getGossipFlag()) {
              } else {
                if (!serverInstance.getActivityStatus()) {
                } else {
                  debug("Creating Propagate state");
                  debug("Creating copy of ClassState");
                  ClassesDefinitions.ClassState classState =
                      ClassesDefinitions.ClassState.newBuilder()
                          .setCapacity(serverInstance.getTurmasRep().getCapacity())
                          .setOpenEnrollments(serverInstance.getTurmasRep().getOpenEnrollments())
                          .addAllEnrolled(Utils.StudentWrapper(serverInstance.getTurmasRep().getEnrolled()))
                          .addAllDiscarded(Utils.StudentWrapper(serverInstance.getTurmasRep().getDiscarded()))
                          .build();

                  debug("Copy of ClassState created");
                  debug("Creating PropagateState Request");
                  ClassServerClassServer.PropagateStateRequest requestPropagate =
                      ClassServerClassServer.PropagateStateRequest.newBuilder()
                          .setClassState(classState)
                          .build();
                  debug("PropagateState Request created");

                  debug("Calling PropagateState");
                  serverInstance.getServersCommunication().setPropagate(requestPropagate);
                  debug("State successfully propagated");
                }
              }
            }
          },
          10 * 1000,
          10 * 1000);
      }else if(serverFlag.equals(PRIMARY)){
          String finalThird_host = third_host;
          int finalThird_port = third_port;
          timer.scheduleAtFixedRate(
                  new TimerTask() {
                      @Override
                      public void run() {
                          if (!serverInstance.getGossipFlag()) {
                          } else {
                              if (!serverInstance.getActivityStatus()) {
                              } else {
                                  debug("Creating Propagate state");
                                  debug("Creating copy of ClassState");
                                  ClassesDefinitions.ClassState classState =
                                          ClassesDefinitions.ClassState.newBuilder()
                                                  .setCapacity(serverInstance.getTurmasRep().getCapacity())
                                                  .setOpenEnrollments(serverInstance.getTurmasRep().getOpenEnrollments())
                                                  .addAllEnrolled(Utils.StudentWrapper(serverInstance.getTurmasRep().getEnrolled()))
                                                  .addAllDiscarded(Utils.StudentWrapper(serverInstance.getTurmasRep().getDiscarded()))
                                                  .build();

                                  debug("Copy of ClassState created");
                                  debug("Creating PropagateState Request");
                                  ClassServerClassServer.PropagateStateRequest requestPropagate =
                                          ClassServerClassServer.PropagateStateRequest.newBuilder()
                                                  .setClassState(classState)
                                                  .build();
                                  debug("PropagateState Request created");

                                  debug("Calling PropagateState");
                                  serverInstance.getServersCommunication().setPropagate(requestPropagate);
                                  serverInstance.getServersCommunication().setupServer(finalThird_host, finalThird_port);
                                  serverInstance.getServersCommunication().setPropagate(requestPropagate);
                                  debug("State successfully propagated");
                              }
                          }
                      }
                  },
                  10 * 1000,
                  10 * 1000);
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
