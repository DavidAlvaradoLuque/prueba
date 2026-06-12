package com.saberpro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultados")
public class Resultado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_examen", nullable = false, length = 12)
    private TipoExamen tipoExamen;

    @DecimalMin(value = "0.0", message = "Puntaje no puede ser negativo")
    @DecimalMax(value = "300.0", message = "Puntaje máximo es 300")
    private Double puntajeGlobal;

    @DecimalMin("0.0") @DecimalMax("300.0")
    private Double lecturaEscritura;
    @DecimalMin("0.0") @DecimalMax("300.0")
    private Double razonamientoCuantitativo;
    @DecimalMin("0.0") @DecimalMax("300.0")
    private Double competenciasCiudadanas;
    @DecimalMin("0.0") @DecimalMax("300.0")
    private Double ingles;
    @DecimalMin("0.0") @DecimalMax("300.0")
    private Double componenteEspecifico;

    @Column(length = 12)
    private String periodo;  // formato YYYY-N, ej: 2024-1

    private LocalDate fechaExamen;

    public enum TipoExamen { TYT, SABER_PRO }

    // ── Lógica de negocio ────────────────────────────────────────
    public String getNivel() {
        if (puntajeGlobal == null) return "Sin puntaje";
        if (puntajeGlobal >= 180) return "Superior";
        if (puntajeGlobal >= 160) return "Alto";
        if (puntajeGlobal >= 140) return "Medio";
        return "Bajo";
    }

    public boolean tieneBeneficio() {
        return TipoExamen.SABER_PRO.equals(tipoExamen)
            && puntajeGlobal != null && puntajeGlobal >= 160;
    }

    public String getDescripcionBeneficio() {
        if (!tieneBeneficio()) return "Sin beneficio";
        if (puntajeGlobal >= 180) return "Exoneración Trabajo de Grado (nota 5.0) + Beca 100% Derechos de Grado";
        if (puntajeGlobal >= 170) return "Exoneración Trabajo de Grado (nota 5.0) + Beca 50% Derechos de Grado";
        return "Exoneración Trabajo de Grado (nota 4.5)";
    }

    // ── Getters / Setters ────────────────────────────────────────
    public Long getId()                                      { return id; }
    public void setId(Long id)                               { this.id = id; }
    public Alumno getAlumno()                                { return alumno; }
    public void setAlumno(Alumno a)                          { this.alumno = a; }
    public TipoExamen getTipoExamen()                        { return tipoExamen; }
    public void setTipoExamen(TipoExamen t)                  { this.tipoExamen = t; }
    public Double getPuntajeGlobal()                         { return puntajeGlobal; }
    public void setPuntajeGlobal(Double v)                   { this.puntajeGlobal = v; }
    public Double getLecturaEscritura()                      { return lecturaEscritura; }
    public void setLecturaEscritura(Double v)                { this.lecturaEscritura = v; }
    public Double getRazonamientoCuantitativo()              { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double v)        { this.razonamientoCuantitativo = v; }
    public Double getCompetenciasCiudadanas()                { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double v)          { this.competenciasCiudadanas = v; }
    public Double getIngles()                                { return ingles; }
    public void setIngles(Double v)                          { this.ingles = v; }
    public Double getComponenteEspecifico()                  { return componenteEspecifico; }
    public void setComponenteEspecifico(Double v)            { this.componenteEspecifico = v; }
    public String getPeriodo()                               { return periodo; }
    public void setPeriodo(String p)                         { this.periodo = p; }
    public LocalDate getFechaExamen()                        { return fechaExamen; }
    public void setFechaExamen(LocalDate d)                  { this.fechaExamen = d; }
}
