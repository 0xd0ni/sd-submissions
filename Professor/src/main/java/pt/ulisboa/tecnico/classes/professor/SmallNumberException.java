package pt.ulisboa.tecnico.classes.professor;

import java.io.Serial;
import java.io.Serializable;

public class SmallNumberException extends Exception implements Serializable {

    @Serial
    private static final long serialVersionUID = 7917080165785805792L;

    public SmallNumberException() {
        super();
    }
}
