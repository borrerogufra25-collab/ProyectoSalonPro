# SalonPro - App Web para gestionar citas en peluquería

SalonPro es una aplicación web para la gestión de citas de una peluquería de forma fácil y óptima.  
El sistema permite, entre otras funcionalidades, la gestión de reservas, control de solapamientos y cálculo de costes.

## Datos para demo

- **Rol Cliente**
  - Email: `usuario@usuario.com`
  - Contraseña: `user`
- **Rol Administrador**
  - Email: `admin@admin.com`
  - Contraseña: `admin`

## Contexto de la aplicación

Esta aplicación web fue desarrollada para gestionar citas en una peluquería, calculando dinámicamente:

- La duración total de cada cita según los servicios seleccionados
- La ocupación del calendario evitando solapamientos entre citas
- El precio final aplicando descuentos por cumpleaños y cupones de fidelización

## Funcionalidades

### Usuario (cliente)

- **Reservar citas** seleccionando fecha, hora y servicios
- **Editar y cancelar** sus propias citas futuras
- **Acumular puntos de fidelización** por cada cita realizada
- **Uso de cupones** de descuento cuando están disponibles
- **Visualizar historial de citas** y próximas reservas

### Administrador

- **Gestionar citas** de todos los clientes (crear, editar, borrar)
- **Gestionar clientes** (alta, edición, baja)
- **Gestionar servicios** (nombre, precio, duración)
- **Gestionar cupones y descuentos**:
  - Configuración de cupones de fidelización
  - Aplicación automática de descuentos por cumpleaños
  - Control de cupones usados y disponibles
- **Consultar estadísticas**:
  - Servicios más populares
  - Clientes con más visitas
  - Próximas citas ordenadas por fecha

## Tecnologías utilizadas
![Java](https://img.shields.io/badge/Java-21-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-Active-6DB33F?logo=springsecurity&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-005F0F?logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?logo=bootstrap&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-Database-004088?logo=h2&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?logo=apachemaven&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?logo=hibernate&logoColor=white)
![Validation](https://img.shields.io/badge/Bean%20Validation-Jakarta-007396)
![Lombok](https://img.shields.io/badge/Lombok-Enabled-BC2C1A?logo=lombok&logoColor=white)
