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

public class Utils {

    public static List<ClassesDefinitions.Student> StudentWrapper(List<Student> studentList) {

        List<ClassesDefinitions.Student> toSend = new ArrayList<>();

        for(Student student: studentList) {
            toSend.add(WrapTo(student));
        }

        return toSend;
    }


    public static ClassesDefinitions.Student WrapTo(Student student) {

        ClassesDefinitions.Student studentToSend =
                ClassesDefinitions.Student.newBuilder().setStudentId(
                        student.getStudentId()).setStudentName(student.getStudentName()).build();

        return studentToSend;

    }

    public static boolean CheckForUserExistence(String studentId, ClassState state) {
        return state.getRegistered().containsKey(studentId);

    }

}