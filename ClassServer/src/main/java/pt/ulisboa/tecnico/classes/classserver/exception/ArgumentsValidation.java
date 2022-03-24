package pt.ulisboa.tecnico.classes.classserver.exception;

public enum ArgumentsValidation {

    INVALID_CAPACITY("Invalid capacity");


    public final String label;

    ArgumentsValidation(String label) {
        this.label = label;
    }

}

