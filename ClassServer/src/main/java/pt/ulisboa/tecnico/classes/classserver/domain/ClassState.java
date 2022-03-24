package pt.ulisboa.tecnico.classes.classserver.domain;


import pt.ulisboa.tecnico.classes.classserver.exception.ClassesException;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.Stringify;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class ClassState {

    private int capacity;

    private boolean openEnrollments;

    private List<Student> enrolled;

    private List<Student> discarded;

    private ConcurrentHashMap<String,Student> registered;


    public ClassState() {
        enrolled = new ArrayList<>();
        discarded = new ArrayList<>();
        registered = new ConcurrentHashMap<>();

    }

    public int getCapacity() {
        return capacity;

    }

    public boolean getOpenEnrollments() {
        return openEnrollments;

    }

    public void addEnroll(Student student) {
        enrolled.add(student);

    }



    public void addDiscard(Student student) {
        discarded.add(student);

    }

    public void containsStudent(Student student) throws ClassesException {
        if(enrolled.contains(student)) {
            throw new ClassesException(Stringify.format(ResponseCode.STUDENT_ALREADY_ENROLLED));
        }

    }

    public void removeEnroll(Student student) {
        enrolled.remove(student);

    }

    public void removeDiscard(Student student) {
        discarded.remove(student);

    }

    public List<Student> getDiscarded() {
        return discarded;

    }

    public List<Student> getEnrolled() {
        return enrolled;

    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;

    }

    public void setOpenEnrollments(boolean status)  {
        this.openEnrollments = status;
    }

    public void addToRegistry(String studentId, Student student) {
        this.registered.put(studentId, student);

    }

    public ConcurrentHashMap<String, Student> getRegistered() {
        return registered;
    }
}



