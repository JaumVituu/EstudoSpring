package dev.jhonjhonnys.estudos_spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_usuarios")
@NoArgsConstructor
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    
    public Usuario(String nome, String email){
        this.nome = nome;
        this.email = email;
    }
    public Usuario(Long id, String nome, String email){
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
