package pt.ulisboa.tecnico.classes.classserver.domain;


import pt.ulisboa.tecnico.classes.classserver.ClassServerToServerFrontend;

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

}


