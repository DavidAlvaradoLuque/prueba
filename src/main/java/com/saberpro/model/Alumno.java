package com.saberpro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "alumnos",
    uniqueConstraints = @UniqueConstraint(columnNames = "cedula", name = "uk_alumno_cedula"))
public class Alumno {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    @Column(nullable = false, length = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 80)
    @Column(nullable = false, length = 80)
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^[0-9]{5,12}$", message = "La cédula debe contener entre 5 y 12 dígitos numéricos")
    @Column(nullable = false, length = 12, unique = true)
    private String cedula;

    @Email(message = "Formato de email inválido")
    @Column(length = 120)
    private String email;

    @Pattern(regexp = "^[0-9\\+\\-\\s]{7,15}$", message = "Teléfono inválido")
    @Column(length = 20)
    private String telefono;

    /** Semestre: 1 a 10 */
    @Min(value = 1, message = "El semestre mínimo es 1")
    @Max(value = 10, message = "El semestre máximo es 10")
    @Column(nullable = false)
    private Integer semestre;

    /**
     * Programa TyT — debe pertenecer a la misma facultad y ser nivel TYT.
     * Validado en la capa de servicio con ProgramaAcademico.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "programa_tyt", length = 60)
    private ProgramaAcademico programaTyt;

    /**
     * Programa Saber Pro — debe ser la contraparte exacta del programaTyt.
     * Validado en la capa de servicio.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "programa_saber_pro", length = 60)
    private ProgramaAcademico programaSaberPro;

    // ── Estado TyT ──────────────────────────────────────────────
    @Column(nullable = false)
    private boolean aprobadoTyt = false;

    @Column(length = 60)
    private String comprobantePagoTyt;

    private LocalDate fechaPagoTyt;

    // ── Estado Saber Pro ─────────────────────────────────────────
    /** Solo puede ser true si aprobadoTyt == true */
    @Column(nullable = false)
    private boolean aprobadoSaberPro = false;

    @Column(length = 60)
    private String comprobantePagoSaberPro;

    private LocalDate fechaPagoSaberPro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facultad_id", nullable = false)
    private Facultad facultad;

    // ── Getters / Setters ────────────────────────────────────────
    public Long getId()                                    { return id; }
    public void setId(Long id)                             { this.id = id; }
    public String getNombre()                              { return nombre; }
    public void setNombre(String n)                        { this.nombre = n; }
    public String getApellido()                            { return apellido; }
    public void setApellido(String a)                      { this.apellido = a; }
    public String getCedula()                              { return cedula; }
    public void setCedula(String c)                        { this.cedula = c; }
    public String getEmail()                               { return email; }
    public void setEmail(String e)                         { this.email = e; }
    public String getTelefono()                            { return telefono; }
    public void setTelefono(String t)                      { this.telefono = t; }
    public Integer getSemestre()                           { return semestre; }
    public void setSemestre(Integer s)                     { this.semestre = s; }
    public ProgramaAcademico getProgramaTyt()              { return programaTyt; }
    public void setProgramaTyt(ProgramaAcademico p)        { this.programaTyt = p; }
    public ProgramaAcademico getProgramaSaberPro()         { return programaSaberPro; }
    public void setProgramaSaberPro(ProgramaAcademico p)   { this.programaSaberPro = p; }
    public boolean isAprobadoTyt()                         { return aprobadoTyt; }
    public void setAprobadoTyt(boolean b)                  { this.aprobadoTyt = b; }
    public String getComprobantePagoTyt()                  { return comprobantePagoTyt; }
    public void setComprobantePagoTyt(String c)            { this.comprobantePagoTyt = c; }
    public LocalDate getFechaPagoTyt()                     { return fechaPagoTyt; }
    public void setFechaPagoTyt(LocalDate d)               { this.fechaPagoTyt = d; }
    public boolean isAprobadoSaberPro()                    { return aprobadoSaberPro; }
    public void setAprobadoSaberPro(boolean b)             { this.aprobadoSaberPro = b; }
    public String getComprobantePagoSaberPro()             { return comprobantePagoSaberPro; }
    public void setComprobantePagoSaberPro(String c)       { this.comprobantePagoSaberPro = c; }
    public LocalDate getFechaPagoSaberPro()                { return fechaPagoSaberPro; }
    public void setFechaPagoSaberPro(LocalDate d)          { this.fechaPagoSaberPro = d; }
    public Facultad getFacultad()                          { return facultad; }
    public void setFacultad(Facultad f)                    { this.facultad = f; }

    public String getNombreCompleto()   { return nombre + " " + apellido; }
    public boolean puedeVerSaberPro()   { return aprobadoTyt; }

    /** El semestre TyT máximo es 6 (tecnológico). Saber Pro se presenta desde semestre 7+. */
    public boolean semestreHabilitadoParaTyt() {
        return semestre != null && semestre >= 1 && semestre <= 6;
    }
    public boolean semestreHabilitadoParaSaberPro() {
        return semestre != null && semestre >= 7 && semestre <= 10;
    }
}
