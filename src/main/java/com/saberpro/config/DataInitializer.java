package com.saberpro.config;

import com.saberpro.model.*;
import com.saberpro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Inicializa datos de prueba SOLO si la base de datos está vacía.
 * Ejecuta automáticamente al arrancar el sistema.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired FacultadRepository  facultadRepo;
    @Autowired UsuarioRepository   usuarioRepo;
    @Autowired DirectorRepository  directorRepo;
    @Autowired DocenteRepository   docenteRepo;
    @Autowired AlumnoRepository    alumnoRepo;
    @Autowired ResultadoRepository resultadoRepo;

    @Override
    public void run(ApplicationArguments args) {
        if (facultadRepo.count() > 0) {
            System.out.println("[DataInitializer] BD ya tiene datos — omitiendo inicialización.");
            return;
        }
        System.out.println("[DataInitializer] Inicializando datos de prueba...");

        // ── FACULTADES ──────────────────────────────────────────
        Facultad fcse = new Facultad();
        fcse.setNombre("Facultad de Ciencias Socioeconómicas y Empresariales");
        fcse.setCodigo(Facultad.CodigoFacultad.FCSE);
        fcse.setDescripcion("Entrenamiento Deportivo, Contable, Moda, Bancaria, Mercadeo, Empresarial, Agroindustrial");
        facultadRepo.save(fcse);

        Facultad fcni = new Facultad();
        fcni.setNombre("Facultad de Ciencias Naturales e Ingenierías");
        fcni.setCodigo(Facultad.CodigoFacultad.FCNI);
        fcni.setDescripcion("Electrónica, Electromecánica, Topografía, Ambiental, Telecomunicaciones, Sistemas, Energías, Transporte");
        facultadRepo.save(fcni);

        // ── USUARIOS ────────────────────────────────────────────
        crearUsuario("Administrador del Sistema", "admin@saberpro.edu.co", "admin123", Usuario.Rol.ADMIN, null, null);
        crearUsuario("Coordinador FCSE",  "coordinador1@saberpro.edu.co", "admin123", Usuario.Rol.COORDINADOR, fcse, null);
        crearUsuario("Coordinador FCNI",  "coordinador2@saberpro.edu.co", "admin123", Usuario.Rol.COORDINADOR, fcni, null);
        crearUsuario("Docente María López", "docente1@saberpro.edu.co",   "admin123", Usuario.Rol.DOCENTE, fcse, null);
        crearUsuario("Docente Carlos Ruiz", "docente2@saberpro.edu.co",   "admin123", Usuario.Rol.DOCENTE, fcni, null);
        crearUsuario("Juan Ramírez",  "estudiante1@saberpro.edu.co", "1098765432", Usuario.Rol.ESTUDIANTE, null, "1098765432");
        crearUsuario("Ana Gómez",     "estudiante2@saberpro.edu.co", "1098765433", Usuario.Rol.ESTUDIANTE, null, "1098765433");

        // ── DIRECTORES ──────────────────────────────────────────
        crearDirector("Carlos",   "García Pineda",  "10000001", "cgarcia@uts.edu.co",   "3101000001", fcse);
        crearDirector("Patricia", "Morales Ruiz",   "10000002", "pmorales@uts.edu.co",  "3101000002", fcni);

        // ── DOCENTES ────────────────────────────────────────────
        crearDocente("María",   "López Díaz",     "20000001", "docente1@saberpro.edu.co", "3102000001", fcse);
        crearDocente("Carlos",  "Ruiz Gómez",     "20000002", "docente2@saberpro.edu.co", "3102000002", fcni);
        crearDocente("Sandra",  "Peña Vargas",    "20000003", "spena@uts.edu.co",         "3102000003", fcse);
        crearDocente("Miguel",  "Castro Torres",  "20000004", "mcastro@uts.edu.co",       "3102000004", fcni);

        // ── ALUMNOS — FCSE ──────────────────────────────────────
        // Ruta: Gestión Empresarial → Administración de Empresas
        Alumno a1 = crearAlumno("Juan Sebastián", "Ramírez Torres", "1098765432",
            "estudiante1@saberpro.edu.co", "3157894561", 10,
            ProgramaAcademico.FCSE_TYT_EMPRESARIAL,
            ProgramaAcademico.FCSE_PRO_ADMIN_EMPRESAS,
            true, true, "TYT-2023-001", "SP-2024-001", fcse);

        // Ruta: Contable → Contaduría Pública
        Alumno a2 = crearAlumno("Ana María", "Gómez Díaz", "1098765433",
            "estudiante2@saberpro.edu.co", "3157894562", 9,
            ProgramaAcademico.FCSE_TYT_CONTABLE,
            ProgramaAcademico.FCSE_PRO_CONTADURIA,
            true, true, "TYT-2023-002", "SP-2024-002", fcse);

        // Ruta: Mercadeo → Mercadeo (TyT aprobada, SP habilitado pero no aprobado)
        Alumno a3 = crearAlumno("Luis Fernando", "Pérez Santos", "1098765434",
            "lperez@uts.edu.co", "3157894563", 8,
            ProgramaAcademico.FCSE_TYT_MERCADEO,
            ProgramaAcademico.FCSE_PRO_MERCADEO,
            true, false, "TYT-2023-003", null, fcse);

        // Sin TyT aún (semestre 5)
        crearAlumno("Valentina", "Suárez Ruiz", "1098765435",
            "vsuarez@uts.edu.co", "3157894564", 5,
            ProgramaAcademico.FCSE_TYT_MODA, null,
            false, false, null, null, fcse);

        // ── ALUMNOS — FCNI ──────────────────────────────────────
        // Ruta: Sistemas → Ingeniería de Sistemas
        Alumno a7 = crearAlumno("Camilo", "Jiménez Rivera", "1098765438",
            "cjimenez@uts.edu.co", "3157894567", 10,
            ProgramaAcademico.FCNI_TYT_SISTEMAS,
            ProgramaAcademico.FCNI_PRO_ING_SISTEMAS,
            true, true, "TYT-2023-007", "SP-2024-007", fcni);

        // Ruta: Ambiental → Ing. Ambiental (TyT sí, SP no)
        Alumno a8 = crearAlumno("Laura", "Vargas Medina", "1098765439",
            "lvargas@uts.edu.co", "3157894568", 8,
            ProgramaAcademico.FCNI_TYT_AMBIENTAL,
            ProgramaAcademico.FCNI_PRO_ING_AMBIENTAL,
            true, false, "TYT-2023-008", null, fcni);

        // Ruta: Electromecánica → Ing. Electromecánica
        Alumno a9 = crearAlumno("Diego", "Martínez Cruz", "1098765440",
            "dmartinez@uts.edu.co", "3157894569", 10,
            ProgramaAcademico.FCNI_TYT_ELECTROMECANICA,
            ProgramaAcademico.FCNI_PRO_ING_ELECTROMECANICA,
            true, true, "TYT-2023-009", "SP-2024-009", fcni);

        // Sin TyT
        crearAlumno("Paola", "Hernández Silva", "1098765441",
            "phernandez@uts.edu.co", "3157894570", 4,
            ProgramaAcademico.FCNI_TYT_TELECOMUNICACIONES, null,
            false, false, null, null, fcni);

        // ── RESULTADOS TYT ──────────────────────────────────────
        crearResultado(a1, Resultado.TipoExamen.TYT, 165.0, 162.0, 170.0, 160.0, 155.0, null, "2023-1", LocalDate.of(2023,6,15));
        crearResultado(a2, Resultado.TipoExamen.TYT, 158.0, 155.0, 160.0, 158.0, 148.0, null, "2023-1", LocalDate.of(2023,6,15));
        crearResultado(a3, Resultado.TipoExamen.TYT, 172.0, 168.0, 178.0, 170.0, 165.0, null, "2023-2", LocalDate.of(2023,11,20));
        crearResultado(a7, Resultado.TipoExamen.TYT, 182.0, 178.0, 185.0, 180.0, 170.0, null, "2023-1", LocalDate.of(2023,6,15));
        crearResultado(a8, Resultado.TipoExamen.TYT, 155.0, 152.0, 158.0, 154.0, 145.0, null, "2023-2", LocalDate.of(2023,11,20));
        crearResultado(a9, Resultado.TipoExamen.TYT, 178.0, 175.0, 180.0, 176.0, 168.0, null, "2023-1", LocalDate.of(2023,6,15));

        // ── RESULTADOS SABER PRO ─────────────────────────────────
        crearResultado(a1, Resultado.TipoExamen.SABER_PRO, 185.0, 182.0, 190.0, 183.0, 175.0, 180.0, "2024-1", LocalDate.of(2024,5,15));
        crearResultado(a2, Resultado.TipoExamen.SABER_PRO, 170.0, 168.0, 172.0, 169.0, 160.0, 165.0, "2024-1", LocalDate.of(2024,5,15));
        crearResultado(a7, Resultado.TipoExamen.SABER_PRO, 195.0, 192.0, 198.0, 194.0, 185.0, 190.0, "2024-1", LocalDate.of(2024,5,15));
        crearResultado(a9, Resultado.TipoExamen.SABER_PRO, 163.0, 160.0, 165.0, 162.0, 155.0, 158.0, "2024-1", LocalDate.of(2024,5,15));

        System.out.println("[DataInitializer] Datos inicializados correctamente.");
    }

    private void crearUsuario(String nombre, String email, String password, Usuario.Rol rol, Facultad fac, String cedula) {
        Usuario u = new Usuario();
        u.setNombre(nombre); u.setEmail(email); u.setPassword(password);
        u.setRol(rol); u.setFacultad(fac); u.setCedulaAlumno(cedula);
        usuarioRepo.save(u);
    }

    private void crearDirector(String nombre, String apellido, String cedula, String email, String tel, Facultad fac) {
        Director d = new Director();
        d.setNombre(nombre); d.setApellido(apellido); d.setCedula(cedula);
        d.setEmail(email); d.setTelefono(tel); d.setFacultad(fac);
        directorRepo.save(d);
    }

    private void crearDocente(String nombre, String apellido, String cedula, String email, String tel, Facultad fac) {
        Docente d = new Docente();
        d.setNombre(nombre); d.setApellido(apellido); d.setCedula(cedula);
        d.setEmail(email); d.setTelefono(tel); d.setFacultad(fac);
        docenteRepo.save(d);
    }

    private Alumno crearAlumno(String nombre, String apellido, String cedula, String email, String tel,
                                int semestre, ProgramaAcademico tyt, ProgramaAcademico sp,
                                boolean apTyt, boolean apSp, String compTyt, String compSp, Facultad fac) {
        Alumno a = new Alumno();
        a.setNombre(nombre); a.setApellido(apellido); a.setCedula(cedula);
        a.setEmail(email); a.setTelefono(tel); a.setSemestre(semestre);
        a.setProgramaTyt(tyt); a.setProgramaSaberPro(sp);
        a.setAprobadoTyt(apTyt); a.setAprobadoSaberPro(apSp);
        a.setComprobantePagoTyt(compTyt); a.setComprobantePagoSaberPro(compSp);
        a.setFacultad(fac);
        return alumnoRepo.save(a);
    }

    private void crearResultado(Alumno alumno, Resultado.TipoExamen tipo,
                                 double global, double lect, double razon,
                                 double ciudad, double ingles, Double especifico,
                                 String periodo, LocalDate fecha) {
        Resultado r = new Resultado();
        r.setAlumno(alumno); r.setTipoExamen(tipo);
        r.setPuntajeGlobal(global); r.setLecturaEscritura(lect);
        r.setRazonamientoCuantitativo(razon); r.setCompetenciasCiudadanas(ciudad);
        r.setIngles(ingles); r.setComponenteEspecifico(especifico);
        r.setPeriodo(periodo); r.setFechaExamen(fecha);
        resultadoRepo.save(r);
    }
}
