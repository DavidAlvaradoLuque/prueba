# Saber Pro UTS — Sistema de Gestión TyT y Saber Pro

## Tecnologías
- Spring Boot 3.2.5 — Java 17
- MySQL 8.4 (Clever Cloud)
- Thymeleaf
- JPA / Hibernate

## Base de datos
Alojada en **Clever Cloud MySQL**. Las credenciales están configuradas en `application.properties`.
Al arrancar por primera vez, el sistema crea las tablas automáticamente (`ddl-auto=update`)
y ejecuta `DataInitializer.java` para poblar datos de prueba (solo si la BD está vacía).

## Cómo ejecutar
```bash
cd saberpro
mvn spring-boot:run
```
Abre: http://localhost:8080

## Credenciales de prueba

| Rol           | Email                            | Contraseña   |
|---------------|----------------------------------|--------------|
| Administrador | admin@saberpro.edu.co            | admin123     |
| Coordinador FCSE | coordinador1@saberpro.edu.co  | admin123     |
| Coordinador FCNI | coordinador2@saberpro.edu.co  | admin123     |
| Docente FCSE  | docente1@saberpro.edu.co         | admin123     |
| Docente FCNI  | docente2@saberpro.edu.co         | admin123     |
| Estudiante 1  | estudiante1@saberpro.edu.co      | 1098765432   |
| Estudiante 2  | estudiante2@saberpro.edu.co      | 1098765433   |

## Reglas de negocio
1. **TyT → Saber Pro**: Un alumno DEBE tener TyT aprobada antes de habilitar Saber Pro.
2. **Programas**: El programa Saber Pro debe ser la continuación directa del TyT (misma facultad, ruta oficial UTS).
3. **Semestre**: 1–10. TyT recomendado semestre 1–6; Saber Pro recomendado semestre 7–10.
4. **Cédula**: Única por alumno (5–12 dígitos numéricos).
5. **Puntajes**: Entre 0 y 300.
6. **Periodo**: Formato YYYY-1 o YYYY-2.
7. **Beneficios**: Saber Pro ≥ 160 pts otorga exoneración trabajo de grado; ≥ 180 pts + beca 100%.

## Facultades UTS
- **FCSE**: Ciencias Socioeconómicas y Empresariales (7 programas TyT, 6 profesionales)
- **FCNI**: Ciencias Naturales e Ingenierías (8 programas TyT, 8 profesionales)
