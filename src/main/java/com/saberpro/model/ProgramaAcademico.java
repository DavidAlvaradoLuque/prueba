package com.saberpro.model;

/**
 * Catálogo oficial de programas UTS.
 * Cada entrada define: facultad, nivel (TyT o Profesional) y la contraparte profesional o tecnológica.
 * Regla estricta: el programa TyT y el Saber Pro de un alumno DEBEN pertenecer a la misma facultad
 * y seguir la ruta tecnológico → profesional según esta tabla.
 */
public enum ProgramaAcademico {

    // ══ FCSE — Tecnológicos ══
    FCSE_TYT_ENTRENAMIENTO(
        "Tecnología en Entrenamiento Deportivo",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_CULTURA_FISICA"),

    FCSE_TYT_CONTABLE(
        "Tecnología en Manejo de la Información Contable",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_CONTADURIA"),

    FCSE_TYT_MODA(
        "Tecnología en Gestión de la Moda",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_DISENO_MODAS"),

    FCSE_TYT_BANCARIA(
        "Tecnología en Gestión Bancaria y Financiera",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_ADMIN_FINANCIERA"),

    FCSE_TYT_MERCADEO(
        "Tecnología en Mercadeo y Gestión Comercial",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_MERCADEO"),

    FCSE_TYT_EMPRESARIAL(
        "Tecnología en Gestión Empresarial",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        "FCSE_PRO_ADMIN_EMPRESAS"),

    FCSE_TYT_AGROINDUSTRIAL(
        "Tecnología en Gestión Agroindustrial",
        Facultad.CodigoFacultad.FCSE, Nivel.TYT,
        null), // Sin contraparte profesional

    // ══ FCSE — Profesionales ══
    FCSE_PRO_CULTURA_FISICA(
        "Profesional en Cultura Física y Deporte",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_ENTRENAMIENTO"),

    FCSE_PRO_CONTADURIA(
        "Contaduría Pública",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_CONTABLE"),

    FCSE_PRO_DISENO_MODAS(
        "Profesional en Diseño de Modas",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_MODA"),

    FCSE_PRO_ADMIN_FINANCIERA(
        "Administración Financiera",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_BANCARIA"),

    FCSE_PRO_MERCADEO(
        "Mercadeo",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_MERCADEO"),

    FCSE_PRO_ADMIN_EMPRESAS(
        "Administración de Empresas",
        Facultad.CodigoFacultad.FCSE, Nivel.PROFESIONAL,
        "FCSE_TYT_EMPRESARIAL"),

    // ══ FCNI — Tecnológicos ══
    FCNI_TYT_ELECTRONICA(
        "Tecnología en Implementación de Sistemas Electrónicos Industriales",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_ELECTRONICA"),

    FCNI_TYT_ELECTROMECANICA(
        "Tecnología en Operación y Mantenimiento Electromecánico",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_ELECTROMECANICA"),

    FCNI_TYT_TOPOGRAFIA(
        "Tecnología en Levantamientos Topográficos",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_TOPOGRAFIA"),

    FCNI_TYT_AMBIENTAL(
        "Tecnología en Manejo de Recursos Ambientales",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_AMBIENTAL"),

    FCNI_TYT_TELECOMUNICACIONES(
        "Tecnología en Gestión de Sistemas de Telecomunicaciones",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_TELECOMUNICACIONES"),

    FCNI_TYT_SISTEMAS(
        "Tecnología en Desarrollo de Sistemas Informáticos",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_SISTEMAS"),

    FCNI_TYT_ENERGIAS(
        "Tecnología en Gestión de Recursos Energéticos",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_ENERGIAS"),

    FCNI_TYT_TRANSPORTE(
        "Tecnología en Sistemas de Transporte",
        Facultad.CodigoFacultad.FCNI, Nivel.TYT,
        "FCNI_PRO_ING_TRANSPORTE"),

    // ══ FCNI — Profesionales ══
    FCNI_PRO_ING_ELECTRONICA(
        "Ingeniería Electrónica",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_ELECTRONICA"),

    FCNI_PRO_ING_ELECTROMECANICA(
        "Ingeniería Electromecánica",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_ELECTROMECANICA"),

    FCNI_PRO_ING_TOPOGRAFIA(
        "Ingeniería en Topografía",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_TOPOGRAFIA"),

    FCNI_PRO_ING_AMBIENTAL(
        "Ingeniería Ambiental",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_AMBIENTAL"),

    FCNI_PRO_ING_TELECOMUNICACIONES(
        "Ingeniería de Telecomunicaciones",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_TELECOMUNICACIONES"),

    FCNI_PRO_ING_SISTEMAS(
        "Ingeniería de Sistemas",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_SISTEMAS"),

    FCNI_PRO_ING_ENERGIAS(
        "Ingeniería en Energías",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_ENERGIAS"),

    FCNI_PRO_ING_TRANSPORTE(
        "Ingeniería en Sistemas de Transporte",
        Facultad.CodigoFacultad.FCNI, Nivel.PROFESIONAL,
        "FCNI_TYT_TRANSPORTE");

    public enum Nivel { TYT, PROFESIONAL }

    private final String nombre;
    private final Facultad.CodigoFacultad codigoFacultad;
    private final Nivel nivel;
    private final String contraparteKey; // null si no tiene contraparte

    ProgramaAcademico(String nombre, Facultad.CodigoFacultad cf, Nivel nivel, String contraparteKey) {
        this.nombre = nombre;
        this.codigoFacultad = cf;
        this.nivel = nivel;
        this.contraparteKey = contraparteKey;
    }

    public String getNombre()                            { return nombre; }
    public Facultad.CodigoFacultad getCodigoFacultad()  { return codigoFacultad; }
    public Nivel getNivel()                              { return nivel; }

    /** Devuelve el programa Saber Pro correspondiente a este TyT, o null. */
    public ProgramaAcademico getContraparte() {
        if (contraparteKey == null) return null;
        try { return ProgramaAcademico.valueOf(contraparteKey); } catch (Exception e) { return null; }
    }

    /** Verifica que un par (tyt, saberPro) sea válido: misma facultad y ruta correcta. */
    public static boolean sonCompatibles(ProgramaAcademico tyt, ProgramaAcademico pro) {
        if (tyt == null || pro == null) return false;
        if (tyt.getNivel() != Nivel.TYT || pro.getNivel() != Nivel.PROFESIONAL) return false;
        if (tyt.codigoFacultad != pro.codigoFacultad) return false;
        // La contraparte del TyT debe ser exactamente el pro elegido
        return pro.equals(tyt.getContraparte());
    }

    /** Todos los TyT de una facultad. */
    public static java.util.List<ProgramaAcademico> getTytDeFacultad(Facultad.CodigoFacultad cf) {
        return java.util.Arrays.stream(values())
            .filter(p -> p.nivel == Nivel.TYT && p.codigoFacultad == cf)
            .toList();
    }

    /** Todos los Profesionales de una facultad. */
    public static java.util.List<ProgramaAcademico> getProDeFacultad(Facultad.CodigoFacultad cf) {
        return java.util.Arrays.stream(values())
            .filter(p -> p.nivel == Nivel.PROFESIONAL && p.codigoFacultad == cf)
            .toList();
    }
}
