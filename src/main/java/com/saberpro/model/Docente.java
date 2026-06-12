package com.saberpro.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
@Entity @Table(name = "docentes",
    uniqueConstraints = @UniqueConstraint(columnNames = "cedula", name = "uk_docente_cedula"))
public class Docente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Column(nullable=false,length=80) private String nombre;
    @NotBlank @Column(nullable=false,length=80) private String apellido;
    @NotBlank @Pattern(regexp="^[0-9]{5,12}$") @Column(nullable=false,length=12,unique=true) private String cedula;
    @Email @Column(length=120) private String email;
    @Column(length=20) private String telefono;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="facultad_id") private Facultad facultad;
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getNombre(){return nombre;} public void setNombre(String n){this.nombre=n;}
    public String getApellido(){return apellido;} public void setApellido(String a){this.apellido=a;}
    public String getCedula(){return cedula;} public void setCedula(String c){this.cedula=c;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getTelefono(){return telefono;} public void setTelefono(String t){this.telefono=t;}
    public Facultad getFacultad(){return facultad;} public void setFacultad(Facultad f){this.facultad=f;}
    public String getNombreCompleto(){return nombre+" "+apellido;}
}
