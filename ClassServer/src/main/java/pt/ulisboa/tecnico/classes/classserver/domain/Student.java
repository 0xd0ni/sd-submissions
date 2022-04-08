package pt.ulisboa.tecnico.classes.classserver.domain;




public class Student {


    private String studentId;
    private String studentName;
    
    public Student() {

    }

    public Student(String id, String name) {
        this.studentId = id;
        this.studentName = name;

    }


    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId()  {
        return studentId;

    }

    public String getStudentName() {
        return studentName;

    }


    @Override
    public boolean equals(Object o) {
        return o instanceof Student && this.studentName.equals(((Student) o).getStudentName()) &&
                this.studentId.equals(((Student) o).getStudentId());

    }

}
