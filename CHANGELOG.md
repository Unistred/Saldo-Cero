a### Añadido
- Configuración de navegación entre pantallas principales (MainMenuScreen y GameScreen) usando la clase Game de LibGDX.
- Creación del enum Simbolo para representar las figuras y multiplicadores de la tragamonedas.
- Implementación del enum EstadoJugador para el control de movimiento/interacción.
- Creación de la clase Jugador con gestión de saldo y cordura.
- Implementación de la clase Slots con lógica de tiro, aleatorización de rodillos y evaluación de premios.
- Organización del proyecto en paquetes (enums, juegos, screens, usuarios).


## [0.1.2] - 03-09-2026

### Añadido
Implementación de StretchViewport y suavizado visual
- Se añadieron StretchViewport en pantallas principales
- Se activó filtrado lineal en fuentes/texturas para reducir pixelación

## [0.1.3] - 08-09-2026

### Añadido
Mejoras visuales y renderizado de slots
- Suavizado de texturas en el viewport
- Incorporación de texturas para jugador, máquina de slots y símbolos
- Implementación del renderizado y animación del juego de slots

## [0.1.4] - 17-09-2026

### Añadido

- Eliminar la clase Usuarios y crear el paquete/clase Entidades.
- Mover la antigua clase Jugador (antes dentro de Usuarios) a Entidades.
- Reorganizar colisiones para mantener únicamente ManejarColisiones.
- Integrar ObjetoJuego y Obstaculo dentro de Entidades.
- Corregir bug en el minijuego de slots/ruleta que permitía volver a girar mientras la animación de giro seguía activa.

## [0.2.0] - 19-10-2026
### Añadido
- Integración completa de sistemas de audio, slots y corrección de movimiento en el mapa
- Implementación de GestorMusica y GestorSonido para la gestión centralizada de pistas de fondo y efectos especiales.
- Incorporación de archivos multimedia de audio (.mp3 y .wav) y conexión del sonido de giro en las máquinas tragamonedas.
- Creación de la clase RenderSlots con soporte para animaciones de rodillos mediante Animation<TextureRegion>.
- Integración de la estructura del mapa de Tiled (casino_saldo_cero.tmx) con OrthogonalTiledMapRenderer.
- Corrección de la velocidad del jugador (fijada en 150f) para solucionar el bloqueo de movimiento en el mapa.
- Configuración del sistema de colisiones con paredes y slots utilizando Intersector y ManejarColisiones.
- Implementación de la cámara seguidora con ExtendViewport y el HUD dinámico de saldo, cordura y avisos en pantalla.

## [0.2.1] - 09-11-2026
### Añadido
- Corrección de la textura de la animacion del jugador y ajuste de hitbox para mejorar la interacción con el entorno y la detección de colisiones. La textura del jugador ahora es de 25x28 píxeles, y la hitbox se ajusta a 25x28 píxeles para reflejar con precisión la forma del personaje y permitir un movimiento más fluido y realista dentro del mapa.

## [0.2.2] - 16-11-2026
### Añadido
- Creacion de la base de datos con sqlLite

## [0.2.3] - 17-11-2026
### Añadido
## Arreglo de bugs
- Corregida la velocidad del movimiento diagonal del jugador, evitando que sea superior a la del movimiento horizontal o vertical.
- Corregida la gestión del volumen para evitar que los valores superen los límites establecidos.
- Corregido el control del volumen inicial de los efectos de sonido, estableciéndolo en un 50%.
- Corregida la administración de recursos para evitar liberar gestores de música que siguen siendo utilizados.
- Mejorada la liberación de recursos al cerrar el juego, incluyendo la pantalla activa y los elementos compartidos.

## Mejoras de estructura del codigo

- Incorporado ControladorEntrada para centralizar la lectura del teclado y separar los controles de la lógica del juego.
- Modificada la clase Jugador para recibir la dirección de movimiento mediante Vector2, desacoplando el movimiento de la lectura directa del teclado.
- Normalizada la dirección de movimiento para mantener una velocidad uniforme en todas las direcciones.
- Modificada ObjetoJuego para permitir objetos sin textura, conservando únicamente su hitbox cuando corresponde.
- Optimizada la clase Obstaculo para utilizar solamente su hitbox, evitando cargar texturas innecesarias.
- Centralizada en JuegoPrincipal la gestión global del volumen y el silencio de música y efectos de sonido.
- Incorporados controles independientes para subir, bajar y silenciar el volumen mediante el teclado (+, - y N).
- Mejorados GestorMusica y GestorSonido para conservar el volumen configurado y aplicar correctamente el estado de silencio.
- Mejorada la administración de la música del menú, incorporando su detención al iniciar el juego.
- Incorporada la visualización del volumen y del estado de silencio en el menú principal y en el HUD del mapa.
- Modificado Mapa para delegar el movimiento, la interacción y la pausa mediante el controlador de entrada, manteniendo la navegación del menú de pausa con su implementación actual.

## [0.2.4] - 21-11-2026
### Añadido
- Se agrega la estructura de datos y el estado de partida para manejar 3 niveles con saldo compartido, cordura por nivel y tiempo total. Los niveles se definen directamente en Java .
- niveles/ConfigNivel: datos fijos de un nivel (tiempo, cordura inicial, cordura por apuesta, meta de saldo, penalizacion, probabilidad de perder, apuestas y dialogos de inicio/cierre).
- niveles/ConfiguracionJuego: arma los 3 niveles, el dialogo final y la frase final en el metodo crear(). Avisa por consola si una pagina de dialogo supera los 150 caracteres.
- niveles/Partida: estado de la partida en curso (nivel actual, saldo, cordura, tiempo restante y total). Incluye terminarNivel() con penalizacion de cordura si no se llega a la meta, avanzarNivel() y formato de tiempo mm:ss para el HUD y la pantalla final.

## Modificado 
- JuegoPrincipal: se crean configuracion y partida en create() y se agrega nuevaPartida() para reiniciar al empezar desde el menu.
- ControladorEntrada: nuevos metodos quiereAvanzar(), quiereSalirDelNivel().

## [1.0.0] - 24-11-2026

### Añadido
- Degradacion visual del jugador por dia: nuevos spritesheets jugadorAnimacionWASD_cansado.png y jugadorAnimacionWASD_muy_cansado.png, seleccion automatica segun el nivel en Jugador.actualizarDia(int) y liberacion de la textura anterior en ObjetoJuego.cambiarTextura(String).
- Sistema de deuda acumulativa por dia (+5000 por cierre de jornada), acreditada al saldo disponible del jugador y protegida contra duplicados con deudaAplicada.
- Logros internos: enum Logro (Primera deuda, Saldo cero), marcado desde Partida y persistido en SQLite sin duplicados.
- Menu de Logros: boton en MainMenuScreen (reutiliza startMenu.png con etiqueta "LOGROS") y PantallaLogros con desbloqueados, historial acumulado y resumen de apuestas leidos con SELECT.
- Base de datos: inicializacion al arrancar, DDL sentencia por sentencia, migracion automatica de deuda_final, consolidacion de jornadas por jugador/dia, registro de apuestas y tabla Historial_Logros con un evento por partida.
- Pantallas de flujo: PantallaDialogo (dialogos paginados de inicio y cierre por nivel) y PantallaFinal (frase final y tiempo total de la partida).
- Volumen del efecto del slot en tiempo real: GestorSonido guarda el id de reproduccion y aplica setVolume al sonido en curso; el mute y los proximos giros respetan el nivel configurado.
- Flujo completo de partida en JuegoPrincipal: dialogo de inicio por nivel, cierre con registro de jornada en BD (EXITOSA/FALLIDA y deuda final), dialogo final y PantallaFinal. Guardar el nombre reinicia la partida desde el primer nivel.
- Confirmacion para terminar el dia antes de tiempo: X abre el aviso, ENTER confirma y ESC sigue jugando. Si se acaba el tiempo durante un giro, se espera 1,5 s a que termine la tirada antes de cerrar el nivel.
- Tragamonedas parametrizada por nivel: apuestas y probabilidad de perder leidas de ConfigNivel, ajuste automatico de la apuesta al saldo disponible y seguimiento de la ultima apuesta/premio para la BD.
- ConfiguracionJuego: duracion de los niveles 1 y 2 ajustada a 180 s y nuevo texto de aviso cuando no se alcanza la meta.
- ControladorEntrada: nuevo quiereConfirmar() (ENTER/ESPACIO) y teclas alternativas para subir/bajar volumen (=, ], /).

### Modificado
- Deuda a 5000 por dia; se conserva el credito al saldo y el logro Primera deuda.
- HUD: se quito el indicador separado de deuda en Mapa y RenderSlots, conservando saldo, cordura, volumen, meta y tiempos.
- RenderSlots y Mapa: la apuesta se registra al terminar el giro; ESC queda bloqueado mientras los rodillos giran y se muestra un aviso.
- PantallaFinal: guardia primerFrame para no consumir el ENTER que cerro el ultimo dialogo.
- ConfigNivel: el comentario ahora indica que los datos estan definidos en ConfiguracionJuego.crear() y no en un JSON.
- PantallaGuardarNombre: se limpia el procesador de entrada antes de iniciar la partida para no volver a disparar guardar(), y ahora arranca con nuevaPartida() en vez de entrar directo al mapa.
- assets.txt: incluye los dos sprites nuevos y deja fuera el mapa obsoleto casino saldo ceri.tmx, que tambien se elimino del proyecto.

### Arreglo de bugs
- El premio de la tragamonedas nunca se guardaba en la BD: se registraba la apuesta al iniciar el giro con ganado 0 y la bandera se limpiaba antes del resultado. Ahora se guarda al finalizar con el monto real y se usa apuestaGirada para calcular el premio.
- La pantalla final podia saltarse sola por el ENTER pendiente del dialogo anterior.
- Se podia volver al mapa a mitad de un giro, descartando el resultado de la apuesta.
- El historial de logros mostraba siempre x1 por el indice UNIQUE; ahora los eventos por partida permiten acumular veces.
- GestorBD ya no asume conexion disponible: todas las operaciones omiten la accion y avisan por consola si falla la conexion.
- El volumen y el silencio del efecto de giro ahora afectan al sonido que esta sonando y al siguiente.
