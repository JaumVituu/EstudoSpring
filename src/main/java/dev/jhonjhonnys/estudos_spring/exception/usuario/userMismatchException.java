package dev.jhonjhonnys.estudos_spring.exception.usuario;

public class userMismatchException extends RuntimeException{
    public userMismatchException(String message){
        super(message);
    }
}
