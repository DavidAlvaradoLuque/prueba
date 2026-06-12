package com.saberpro.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "facultades")
public class Facultad {

    public enum CodigoFacultad { FCSE, FCNI }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, unique = true)
    private CodigoFacultad codigo;

    @Column(length = 300)
    private String descripcion;

    @OneToMany(mappedBy = "facultad")
    private List<Alumno> alumnos;

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }
    public String getNombre()              { return nombre; }
    public void setNombre(String n)        { this.nombre = n; }
    public CodigoFacultad getCodigo()      { return codigo; }
    public void setCodigo(CodigoFacultad c){ this.codigo = c; }
    public String getDescripcion()         { return descripcion; }
    public void setDescripcion(String d)   { this.descripcion = d; }
    public List<Alumno> getAlumnos()       { return alumnos; }
    public void setAlumnos(List<Alumno> a) { this.alumnos = a; }
}
