# Saldo-Cero

---

--Integrantes del Grupo
*   **Francisco Mallo** - *Backend y Persistencia*
*   **Joaquin Ivan Guzman Dubickas** - *Gráficos y Pantallas*
*   **Ignacio Palermo** - *Mecánicas e Interacción*

---

--Descripción del Videojuego
**"Saldo Cero"** es un *Serious Game* interactivo de un solo jugador en dos dimensiones (2D) con perspectiva Top-Down. El proyecto está enfocado en visibilizar y concientizar sobre la ludopatía en adolescentes, exponiendo de forma cruda cómo esta adicción afecta progresivamente la conducta, el carácter y el entorno socioemocional de las personas. 

Ambientado en un casino y entornos urbanos de clase media-baja, el jugador controla a un joven de 16 años cuyas decisiones e impactos en los juegos de azar afectarán directamente su saldo económico, su estabilidad mental y el trágico destino de su familia.

---

--Tecnologías Principales y Plataformas
*   **Framework Principal:** LibGDX (Paradigma POO)
*   **Lenguaje de Programación:** Java 17 LTS
*   **Gestor de Dependencias:** Gradle
*   **Motor de Persistencia:** SQLite 
*   **Plataforma Objetivo:** Escritorio (Desktop - LWJGL3)
*   **Diseño de Mapas:** Tiled Map Editor (Formatos `.tmx`)
---

--Documentación Completa (Wiki)
La propuesta formal y detallada del videojuego, que incluye el alcance mínimo, el alcance deseado, el esquema relacional de la base de datos y el desglose de la historia por semanas, se encuentra disponible en la documentación oficial del repositorio:


**[Ver la Propuesta Detallada en la Wiki de GitHub](https://github.com/Unistred/Saldo-Cero/wiki/Propuesta-del-Proyecto-%5BSaldo-Cero%5D)**
---

### Prerrequisitos Técnicos
Para poder compilar y ejecutar este proyecto, necesita tener instaladas únicamente dos cosas en su computadora:
1. **Java Development Kit (JDK) 17**: Es la versión de Java que requiere el proyecto. 
2. **IntelliJ IDEA (Community o Ultimate)**: Es el entorno de desarrollo (IDE) recomendado por la mayoria para abrir y trabajar con el código del juego.

Para clonar el repositorio
1. Abre una terminal o consola de comandos y ejecuta el comando para clonar el proyecto en tu equipo:
   git clone https://github.com/Unistred/Saldo-Cero.git
2. Luego, navega a la carpeta que se acaba de crear:
   cd Saldo-Cero
3. Abrir el proyecto en IntelliJ IDEA
   Abre IntelliJ IDEA.En la pantalla de bienvenida, haz clic en Open (o ve a File > Open...).
   Selecciona la carpeta donde clonaste el proyecto (Saldo-Cero) y presiona OK
4. Configurar el SDK de Java (JDK 17)
   Ve al menú superior y selecciona File > Project Structure(o presiona Ctrl + Alt + Shift + S).
   En la pestaña Project, busca la opción SDK.
   Selecciona 17 (JDK 17) en la lista desplegable.
   Nota: Si no aparece JDK 17 instalado, haz clic en Download JDK, selecciona la versión 17 y presiona Download.
   Haz clic en Apply y luego en OK.
5. Descargar e importar dependencias
   Si tu proyecto utiliza un gestor de dependencias (como Maven o Gradle):
   Para Maven: Haz clic en el ícono de Maven en la barra lateral derecha de IntelliJ y luego en el botón Reload All Maven Projects (icono de dos flechas en círculo).
   Para Gradle: Haz clic en el ícono de Gradle en la barra lateral derecha y presiona Reload All Gradle Projects.
   Si es un proyecto Java puro sin gestor, IntelliJ indexará automáticamente los archivos de la carpeta src.
6. Ejecutar el proyecto:
   En el panel izquierdo (Project Explorer), despliega la carpeta src y localiza el archivo principal que contiene el método public static void main(String[] args).
   Abre dicho archivo.
   Haz clic en el botón verde de reproducción (Play) que aparece al lado de la línea del método main o presiona Shift + F10.
   El juego se compilará y ejecutará en la parte inferior dentro de la ventana de la consola o en una nueva ventana gráfica.

