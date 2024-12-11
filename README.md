# Acceso a Laboratorios de la Universidad Nacional del Santa

## Descripción General
Esta aplicación permite gestionar y controlar el acceso a los laboratorios de la Universidad Nacional del Santa. Está diseñada para administradores y usuarios regulares, con funcionalidades que facilitan la gestión de accesos, laboratorios, y usuarios.

## Tabla de Contenidos
1. [Características](#características)
2. [Requisitos Previos](#requisitos-previos)
3. [Instalación](#instalación)
4. [Uso](#uso)
5. [Capturas de Pantalla](#capturas-de-pantalla)
6. [Tecnologías Utilizadas](#tecnologías-utilizadas)

## Características ✨
- *Inicio de Sesión*: Permite elegir entre rol de administrador o usuario al iniciar sesión.
- *Tarjetas de Acceso*: Gestión de tarjetas para habilitar y deshabilitar el acceso a laboratorios.
- *Asignar Laboratorios*: Configura qué usuarios tienen acceso a qué laboratorios.
- *Registro de Accesos*: Visualización y registro en tiempo real de quién ingresó a los laboratorios.
- *Historial de Accesos*: Consulta del historial de entradas y salidas con detalles.
- *Uso de Laboratorios*: Monitoreo del uso en tiempo real de los laboratorios.
- *Gestión de Usuarios*: Administración de cuentas, roles y permisos de los usuarios.

## Requisitos Previos ✅
- *Android Studio* (versión mínima: 4.x).
- *Kotlin* como lenguaje principal.
- *Base de datos Firestore Cloud* configurada.
- *SDK Android* versión 7.0 (Nougat) o superior.

## Instalación 🛠
1. Clona el repositorio:
   
   https://github.com/JoelLeonUNS/LabAccess.git
   
3. Abre Android Studio y selecciona "New - Project from version control" y clonalo.
4. Configura las dependencias en:
   
   ./gradlew build
   
5. Configura Firebase en tu proyecto:
   - Crea un proyecto en [Firebase Console](https://console.firebase.google.com/).
   - Descarga el archivo "google-services.json" y agrégalo a la carpeta "app".
6. Conecta un dispositivo físico o emulador y ejecuta la aplicación.

## Uso 📚
1. *Inicio de Sesión*: Ingresa tus credenciales y selecciona el rol (Administrador o Usuario).
2. Navega por las siguientes vistas desde el Panel de Control:
   - *Tarjetas de Acceso*: Crea, edita o deshabilita tarjetas.
   - *Asignar Laboratorios*: Asocia laboratorios a usuarios específicos.
   - *Registro de Accesos*: Monitorea los accesos en tiempo real.
   - *Historial de Accesos*: Revisa entradas y salidas históricas.
   - *Uso de Laboratorios*: Consulta estadísticas de ocupación.
   - *Gestión de Usuarios*: Administra roles y permisos.

## Capturas de Pantalla 📸
![Inicio de Sesión] ![image](https://github.com/user-attachments/assets/661253e4-068a-44d8-9bfe-2f2d7e6470d9)
![Tarjetas de acceso] ![image](https://github.com/user-attachments/assets/5e533514-9c3a-469f-9a21-7305b63cdea0)
![Usar laboratorios] ![image](https://github.com/user-attachments/assets/7b5d7a66-e489-4fa4-83d0-319d378bd7a3)
![Gestión de usuarios] ![image](https://github.com/user-attachments/assets/029fa983-5f5f-4b35-b068-20dbcb27ba71)
entre otros...

## Tecnologías Utilizadas 🛠
- *Kotlin*: Lenguaje principal de desarrollo.
- *Firestore Cloud*: Base de datos en tiempo real.
- *Firebase Authentication*: Gestión de usuarios.
- *Material Design*: Para una interfaz de usuario moderna y amigable.