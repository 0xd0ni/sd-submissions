package pt.ulisboa.tecnico.classes.classserver.domain;


import pt.ulisboa.tecnico.classes.classserver.ClassServerToServerFrontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static pt.ulisboa.tecnico.classes.Utilities.PRIMARY;
import static pt.ulisboa.tecnico.classes.Utilities.SECONDARY;

// This class represents a server instance
public class ServerInstance {

    private ClassState classRep;

    private String type;

    private String host;

    private String port;

    private boolean activityStatus;

    private boolean gossipFlag = false;

    ClassServerToServerFrontend serversCommunication  = new ClassServerToServerFrontend(type);

    public ClassServerToServerFrontend getServersCommunication() {
        return serversCommunication;
    }

    public boolean getGossipFlag() {
        return gossipFlag;
    }

    public void setGossipFlag(boolean gossipFlag) {
        this.gossipFlag = gossipFlag;
    }

    // ClassState classState, boolean debugValue,String type,String host, String port


    // we need to define a variable that lets the replica communicate with the naming server
    //private NamingServer namingServer;



    public ServerInstance() {

    }

    public void setTurmasRep(ClassState classState) {
        this.classRep = classState;

    }

    public void setType(String type) {
        this.type = type;

    }

    public void setHost(String host) {
        this.host = host;

    }

    public void setPort(String port) {
        this.port = port;

    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public ClassState getTurmasRep() {
        return classRep;

    }

    public String getType() {
        return type;

    }

    public String getHost() {
        return host;

    }

    public String getPort() {
        return port;

    }

    public boolean getActivityStatus() {
        return activityStatus;
    }

    public ClassState getCoherentState(ClassState updated) {
        ClassState CoherentState = new ClassState();
        ClassState currentState = this.classRep;
        ClassState n_updated = updated;

        try
        {
            //chooses state with the biggest capacity
            if (n_updated.getCapacity() > currentState.getCapacity())
                currentState.setCapacity(n_updated.getCapacity());
            else if (n_updated.getCapacity() < currentState.getCapacity())
                n_updated.setCapacity(currentState.getCapacity());


            if (n_updated.getCapacity() == currentState.getCapacity())
            {
                //if receives an openEnrollments == true then it empties both discard lists
                if ( type.equals(SECONDARY) && !currentState.getOpenEnrollments() && updated.getOpenEnrollments())
                {
                    CoherentState.setOpenEnrollments(true);
                    n_updated.setDiscarded(new ArrayList<>());
                    currentState.setDiscarded(new ArrayList<>());
                }
                else if ( type.equals(SECONDARY) && currentState.getOpenEnrollments() == true && updated.getOpenEnrollments() == true)
                    CoherentState.setOpenEnrollments(true);
                else if ( type.equals(SECONDARY) && currentState.getOpenEnrollments() == true && updated.getOpenEnrollments() == false)
                    CoherentState.setOpenEnrollments(false);
                else if ( type.equals(PRIMARY))
                    CoherentState.setOpenEnrollments(currentState.getOpenEnrollments());

                CoherentState.setCapacity(updated.getCapacity());
                List<Student> enrolledStudents = Stream.concat(n_updated.getEnrolled().stream(),currentState.getEnrolled().stream()).distinct().toList();
                List<Student> discardedStudents = Stream.concat(n_updated.getDiscarded().stream(),currentState.getDiscarded().stream()).distinct().toList();

                //removes enrolled students if they are in any of the discarded lists
                for (Student student : enrolledStudents)
                {
                    if (discardedStudents.contains(student))
                    {
                        CoherentState.addDiscard(student);
                        n_updated.getEnrolled().remove(student);
                        currentState.getEnrolled().remove(student);
                        enrolledStudents.remove(student);

                        //removes duplicates in discarded students
                        discardedStudents.remove(student);
                    }
                }
                //adds the remaining discarded students
                discardedStudents.stream().forEach(CoherentState::addDiscard);

                if (n_updated.getEnrolled().size() == currentState.getEnrolled().size())
                {
                    //picks random elements from enrolledstudents list and adds them to the new
                    //coherent students list, the students that arent picked are discarded
                    Random rand = new Random();
                    List<Student> newList = new ArrayList<>();

                    for (int i = 0; i < n_updated.getCapacity(); i++) {
                        // take a random index between 0 to size
                        // of given List
                        int randomIndex = rand.nextInt(enrolledStudents.size());

                        // add element in temporary list
                        newList.add(enrolledStudents.get(randomIndex));

                        // Remove selected element from original list
                        enrolledStudents.remove(randomIndex);
                    }
                    CoherentState.setEnrolled(newList);

                    //adds the students that werent picked to the discarded list
                    enrolledStudents.stream().forEach(student -> CoherentState.getDiscarded().add(student));
                }
                else if (n_updated.getEnrolled().size() != currentState.getEnrolled().size())
                {
                    if (n_updated.getEnrolled().size() > currentState.getEnrolled().size())
                    {
                        n_updated.getEnrolled().stream().forEach(student -> CoherentState.getEnrolled().add(student));
                        int remaining_size = n_updated.getCapacity() - n_updated.getEnrolled().size();

                        if (remaining_size > 0)
                        {
                                for (int i = 0; i < remaining_size && i < currentState.getEnrolled().size(); i++) {
                                    CoherentState.getEnrolled().add(currentState.getEnrolled().get(i));

                                    // Remove selected element from original list
                                    currentState.getEnrolled().remove(i);
                                }
                                // adds remaining students to discard list
                                currentState.getEnrolled().stream().forEach(student -> CoherentState.getDiscarded().add(student));
                        }
                    }
                    else if (n_updated.getEnrolled().size() < currentState.getEnrolled().size())
                    {
                        currentState.getEnrolled().stream().forEach(student -> CoherentState.getEnrolled().add(student));
                        int remaining_size = n_updated.getCapacity() - currentState.getEnrolled().size();

                        if (remaining_size > 0)
                        {
                            for (int i = 0; i < remaining_size && i < n_updated.getEnrolled().size(); i++) {
                                CoherentState.getEnrolled().add(n_updated.getEnrolled().get(i));

                                // Remove selected element from original list
                                n_updated.getEnrolled().remove(i);
                            }
                            // adds remaining students to discard list
                            n_updated.getEnrolled().stream().forEach(student -> CoherentState.getDiscarded().add(student));
                        }
                    }
                }

            }
            CoherentState.setCurrentCapacity(CoherentState.getEnrolled().size());
            CoherentState.getEnrolled().stream().forEach(student -> CoherentState.addToRegistry(student.getStudentId(),student));
            return CoherentState;
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("Error: No students enrolled");
        }
        return CoherentState;
    }
}


