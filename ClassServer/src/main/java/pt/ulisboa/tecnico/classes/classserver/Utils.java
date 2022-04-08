/**
 * This file contains some utils methods used in different files and also
 * wrappers class definitions and its protobuf messages
 *
 *
 */

package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.classserver.domain.ClassState;
import pt.ulisboa.tecnico.classes.classserver.domain.Student;
import pt.ulisboa.tecnico.classes.classserver.exception.SmallNumberException;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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


        ClassesDefinitions.Student studentToSend =
                ClassesDefinitions.Student.newBuilder().setStudentId(
                        student.getStudentId()).setStudentName(student.getStudentName()).build();

        return studentToSend;

    }

    public static boolean CheckForUserExistence(String studentId, ClassState state) {
        return state.getRegistered().containsKey(studentId);

    }

    public static boolean CheckStudentId(String Id){
        try {
            if (!Id.substring(0, 4).equals("aluno"))
            {
                System.err.println("Error: wrong format for student ID, write aluno + 4 digit number instead of " + Id);
                return false;
            }

            if (Id.substring(5).length() != 4)
                throw new SmallNumberException();

            Integer.parseInt(Id.substring(5));
            return true;

        } catch (NumberFormatException e) {
            System.err.println("Error: wrong format for student ID, write a number with 4 digits");
            return false;
        }
        catch (StringIndexOutOfBoundsException e)
        {
            System.err.println("Error: wrong format for student ID, write aluno + 4 digit number instead of "+Id);
            return false;
        }
        catch (SmallNumberException e)
        {
            System.err.println("Error: wrong format for student ID, number must have 4 digits");
            return false;
        }
    }

    public static boolean CheckStudentName(String studentName){
        return studentName.matches("/^[A-Z]+[a-z]*{3,30}$/");

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