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
