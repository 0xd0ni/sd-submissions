package pt.ulisboa.tecnico.classes.classserver.domain;


import java.util.ArrayList;
import java.util.List;


public class ClassState {

    private int capacity;

    private boolean openEnrollments;

    private List<Student> enrolled;

    private List<Student> discarded;



    public ClassState() {

    }

    public ClassState(int capacity, boolean status) {
        this.capacity = capacity;
        this.openEnrollments = status;
        enrolled = new ArrayList<Student>();
        discarded = new ArrayList<Student>();

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

    public void removeEnroll(Student student) {
        enrolled.remove(student);
    }

    public void removeDiscard(Student student) {
        discarded.remove(student);
    }
}



