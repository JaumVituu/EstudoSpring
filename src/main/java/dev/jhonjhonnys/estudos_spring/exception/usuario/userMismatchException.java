package dev.jhonjhonnys.estudos_spring.exception.usuario;

public class UserMismatchException extends RuntimeException{
    public UserMismatchException(String message){
        super(message);
    }
}
