package pt.ulisboa.tecnico.classes.student;

import java.io.Serial;
import java.io.Serializable;

public class SmallNumberException extends Exception implements Serializable {

    private static final long serialVersionUID = -5059331198796556899L;

    public SmallNumberException() {
        super();
    }
}
