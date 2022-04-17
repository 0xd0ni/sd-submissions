/**
 * This file contains some utils methods used in different files and also
 * wrappers class definitions and its protobuf messages
 *
 *
 */

package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

import java.util.ArrayList;
import java.util.List;

enum Server{
    PRIMARY,
    SECONDARY
}

public class Utils {

    public static List<Student> studentAll(List<ClassesDefinitions.Student> list ) {

        List<Student> students = new ArrayList<>();

        if(!list.isEmpty()) {

            for(ClassesDefinitions.Student student: list) {
                if(student == null) {

                } else {
                    Student studentInstance = new Student();
                    studentInstance.setStudentId(student.getStudentId());
                    studentInstance.setStudentName(student.getStudentName());

                    students.add(studentInstance);
                }
            }
        }

        return students;
    }

    public static List<ClassesDefinitions.Student> StudentWrapper(List<Student> studentList) {

        List<ClassesDefinitions.Student> toSend = new ArrayList<>();

        for(Student student: studentList) {
            if(student == null){

            } else {
                toSend.add(WrapTo(student));
            }
        }

        return toSend;
    }

    public static ClassesDefinitions.Student WrapTo(Student student) {

        return ClassesDefinitions.Student.newBuilder().setStudentId(
                student.getStudentId()).setStudentName(student.getStudentName()).build();

    }

    public static boolean CheckForUserExistence(String studentId, ClassState state) {
        return state.getRegistered().containsKey(studentId);

    }

    public static String ServerSpecification(Server option) {
        if(option == Server.PRIMARY) {
            return "P";
        }
        else if(option == Server.SECONDARY) {
            return "S";
        }

        return "INVALID";

    }

}