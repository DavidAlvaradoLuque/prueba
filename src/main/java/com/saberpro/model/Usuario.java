package com.saberpro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuarios",
    uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "uk_usuario_email"))
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    /** Para rol ESTUDIANTE: cédula del alumno vinculado */
    @Column(length = 12)
    private String cedulaAlumno;

    public enum Rol { ADMIN, COORDINADOR, DOCENTE, ESTUDIANTE }

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }
    public String getNombre()              { return nombre; }
    public void setNombre(String n)        { this.nombre = n; }
    public String getEmail()               { return email; }
    public void setEmail(String e)         { this.email = e; }
    public String getPassword()            { return password; }
    public void setPassword(String p)      { this.password = p; }
    public Rol getRol()                    { return rol; }
    public void setRol(Rol r)              { this.rol = r; }
    public Facultad getFacultad()          { return facultad; }
    public void setFacultad(Facultad f)    { this.facultad = f; }
    public String getCedulaAlumno()        { return cedulaAlumno; }
    public void setCedulaAlumno(String c)  { this.cedulaAlumno = c; }
}
