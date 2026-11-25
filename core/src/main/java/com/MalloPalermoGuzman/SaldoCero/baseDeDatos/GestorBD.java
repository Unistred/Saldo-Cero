package com.MalloPalermoGuzman.SaldoCero.baseDeDatos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class GestorBD {

    private static final String RUTA_USUARIO = System.getProperty("user.home");
    private static final String URL = "jdbc:sqlite:" + RUTA_USUARIO + File.separator + "CasinoSurvival_saldocero.db";

    private GestorBD() { }

    public static Connection conectar() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("[BD] Driver SQLite no disponible: " + e.getMessage());
            return null;
        }
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println("[BD] Error de conexion: " + e.getMessage());
            return null;
        }
    }

    private static boolean disponible(Connection conn) {
        if (conn != null) return true;
        System.out.println("[BD] Sin conexion: operacion omitida");
        return false;
    }

    private static void ejecutar(String sql, String descripcion) {
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("[BD] Error al " + descripcion + ": " + e.getMessage());
        }
    }

    /** Crea el esquema ejecutando cada sentencia por separado (SQLite no acepta un script completo). */
    public static void inicializarBaseDeDatos() {
        String[] sentencias = {
            "CREATE TABLE IF NOT EXISTS Jugador (" +
                " id_jugador INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nombre_usuario TEXT NOT NULL," +
                " fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP)",

            "CREATE TABLE IF NOT EXISTS Registro_Jornadas (" +
                " id_jornada INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_jugador INTEGER NOT NULL," +
                " numero_dia INTEGER NOT NULL," +
                " saldo_inicial REAL NOT NULL," +
                " saldo_final REAL NOT NULL," +
                " cordura_final INTEGER NOT NULL," +
                " resultado TEXT NOT NULL," +
                " deuda_final REAL NOT NULL DEFAULT 0," +
                " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE)",

            "CREATE TABLE IF NOT EXISTS Historial_Apuestas (" +
                " id_apuesta INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_jugador INTEGER NOT NULL," +
                " minijuego TEXT NOT NULL," +
                " monto_apostado REAL NOT NULL," +
                " monto_ganado REAL NOT NULL," +
                " saldo_resultante REAL NOT NULL," +
                " cordura_resultante INTEGER NOT NULL," +
                " timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE)",

            "CREATE TABLE IF NOT EXISTS Logros_Desbloqueados (" +
                " id_logro INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_jugador INTEGER NOT NULL," +
                " tag_logro TEXT NOT NULL," +
                " fecha_desbloqueo DATETIME DEFAULT CURRENT_TIMESTAMP," +
                " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE)",

            // Un evento por partida: permite contar cuantas veces se desbloqueo cada logro.
            "CREATE TABLE IF NOT EXISTS Historial_Logros (" +
                " id_evento INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_jugador INTEGER NOT NULL," +
                " tag_logro TEXT NOT NULL," +
                " fecha_desbloqueo DATETIME DEFAULT CURRENT_TIMESTAMP," +
                " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE)",

            "CREATE UNIQUE INDEX IF NOT EXISTS IDX_Logros_Unicos " +
                " ON Logros_Desbloqueados(id_jugador, tag_logro)",

            // Bases que ya tenian el logro antes de existir el historial: se siembra una sola vez por tag.
            "INSERT INTO Historial_Logros (id_jugador, tag_logro, fecha_desbloqueo) " +
                "SELECT d.id_jugador, d.tag_logro, d.fecha_desbloqueo FROM Logros_Desbloqueados d " +
                "WHERE NOT EXISTS (SELECT 1 FROM Historial_Logros h " +
                " WHERE h.id_jugador = d.id_jugador AND h.tag_logro = d.tag_logro)",

            "CREATE VIEW IF NOT EXISTS VW_JornadasExitosas AS " +
                " SELECT * FROM Registro_Jornadas WHERE saldo_final > saldo_inicial",

            "CREATE TRIGGER IF NOT EXISTS TRG_ValidaSaldoApuesta " +
                " BEFORE INSERT ON Historial_Apuestas " +
                " BEGIN " +
                "  SELECT RAISE(ABORT, 'Monto apostado no puede ser negativo') WHERE NEW.monto_apostado < 0;" +
                " END"
        };

        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn) {
            for (String sql : sentencias) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.out.println("[BD] Error al crear estructura: " + e.getMessage());
                }
            }
            migrarEsquema(conn);
            System.out.println("[BD] Base de datos inicializada");
        } catch (SQLException e) {
            System.out.println("[BD] Error al cerrar la conexion: " + e.getMessage());
        }
    }

    /** Agrega columnas nuevas a bases creadas por versiones anteriores del juego. */
    private static void migrarEsquema(Connection conn) {
        try {
            if (!tieneColumna(conn, "Registro_Jornadas", "deuda_final")) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("ALTER TABLE Registro_Jornadas ADD COLUMN deuda_final REAL NOT NULL DEFAULT 0");
                    System.out.println("[BD] Migracion: agregada columna deuda_final");
                }
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error en la migracion: " + e.getMessage());
        }
    }

    private static boolean tieneColumna(Connection conn, String tabla, String columna) throws SQLException {
        String sql = "PRAGMA table_info(" + tabla + ")";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                if (columna.equalsIgnoreCase(rs.getString("name"))) return true;
            }
        }
        return false;
    }

    public static void actualizarSaldoJornada(int idJornada, double nuevoSaldo, String resultado) {
        String sql = "UPDATE Registro_Jornadas SET saldo_final = ?, resultado = ? WHERE id_jornada = ?;";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, resultado);
            ps.setInt(3, idJornada);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[BD] Error al actualizar la jornada: " + e.getMessage());
        }
    }

    public static void eliminarApuestaErronea(int idApuesta) {
        String sql = "DELETE FROM Historial_Apuestas WHERE id_apuesta = ?;";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idApuesta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[BD] Error al eliminar la apuesta: " + e.getMessage());
        }
    }

    public static double obtenerTotalApostado(int idJugador) {
        String sql = "SELECT SUM(monto_apostado) AS total FROM Historial_Apuestas WHERE id_jugador = ?;";
        Connection conn = conectar();
        if (!disponible(conn)) return 0.0;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al consultar el total apostado: " + e.getMessage());
        }
        return 0.0;
    }

    public static void registrarApuesta(int idJugador, String minijuego, double apostado,
                                        double ganado, double saldoRes, int cordura) {
        String sql = "INSERT INTO Historial_Apuestas " +
                "(id_jugador, minijuego, monto_apostado, monto_ganado, saldo_resultante, cordura_resultante) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            ps.setString(2, minijuego);
            ps.setDouble(3, apostado);
            ps.setDouble(4, ganado);
            ps.setDouble(5, saldoRes);
            ps.setInt(6, cordura);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[BD] Error al registrar la apuesta: " + e.getMessage());
        }
    }

    public static int guardarNombreJugador(String nombreUsuario) {
        String buscar = "SELECT id_jugador FROM Jugador WHERE nombre_usuario = ?";
        String insertar = "INSERT INTO Jugador (nombre_usuario) VALUES (?)";
        Connection conn = conectar();
        if (!disponible(conn)) return -1;
        try (conn) {
            try (PreparedStatement ps = conn.prepareStatement(buscar)) {
                ps.setString(1, nombreUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_jugador");
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(insertar, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nombreUsuario);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al guardar el nombre: " + e.getMessage());
        }
        return -1;
    }

    public static void insertarJugador(String nombreUsuario) {
        String sql = "INSERT INTO Jugador (nombre_usuario) VALUES (?);";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.executeUpdate();
            System.out.println("[BD] Jugador guardado: " + nombreUsuario);
        } catch (SQLException e) {
            System.out.println("[BD] Error al insertar el jugador: " + e.getMessage());
        }
    }

    public static void insertarApuesta(int idJugador, String minijuego, double montoApostado,
                                       double montoGanado, double saldoResultante, int corduraResultante) {
        registrarApuesta(idJugador, minijuego, montoApostado, montoGanado, saldoResultante, corduraResultante);
    }

    /** Consolida la jornada: si ya existe para ese dia actualiza la fila, si no la inserta. */
    public static void registrarJornada(int idJugador, int numeroDia, double saldoInicial,
                                        double saldoFinal, int corduraFinal, String resultado, double deudaFinal) {
        if (idJugador < 0) return;
        String actualizar = "UPDATE Registro_Jornadas SET saldo_inicial = ?, saldo_final = ?, " +
                "cordura_final = ?, resultado = ?, deuda_final = ? WHERE id_jugador = ? AND numero_dia = ?;";
        String insertar = "INSERT INTO Registro_Jornadas " +
                "(id_jugador, numero_dia, saldo_inicial, saldo_final, cordura_final, resultado, deuda_final) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn) {
            try (PreparedStatement ps = conn.prepareStatement(actualizar)) {
                ps.setDouble(1, saldoInicial);
                ps.setDouble(2, saldoFinal);
                ps.setInt(3, corduraFinal);
                ps.setString(4, resultado);
                ps.setDouble(5, deudaFinal);
                ps.setInt(6, idJugador);
                ps.setInt(7, numeroDia);
                if (ps.executeUpdate() > 0) {
                    System.out.println("[BD] Jornada del dia " + numeroDia + " consolidada");
                    return;
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(insertar)) {
                ps.setInt(1, idJugador);
                ps.setInt(2, numeroDia);
                ps.setDouble(3, saldoInicial);
                ps.setDouble(4, saldoFinal);
                ps.setInt(5, corduraFinal);
                ps.setString(6, resultado);
                ps.setDouble(7, deudaFinal);
                ps.executeUpdate();
                System.out.println("[BD] Jornada del dia " + numeroDia + " registrada");
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al registrar la jornada: " + e.getMessage());
        }
    }

    /** SELECT previo + INSERT OR IGNORE + indice unico: nunca se desbloquea dos veces el mismo logro. */
    public static boolean desbloquearLogro(int idJugador, String tag) {
        if (idJugador < 0 || tag == null) return false;
        String existe = "SELECT 1 FROM Logros_Desbloqueados WHERE id_jugador = ? AND tag_logro = ? LIMIT 1;";
        String insertar = "INSERT OR IGNORE INTO Logros_Desbloqueados (id_jugador, tag_logro) VALUES (?, ?);";
        Connection conn = conectar();
        if (!disponible(conn)) return false;
        try (conn) {
            try (PreparedStatement ps = conn.prepareStatement(existe)) {
                ps.setInt(1, idJugador);
                ps.setString(2, tag);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return false;
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(insertar)) {
                ps.setInt(1, idJugador);
                ps.setString(2, tag);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al desbloquear el logro: " + e.getMessage());
            return false;
        }
    }

    /** Un evento por partida en la que se desbloqueo el logro; alimenta el historial acumulado. */
    public static void registrarEventoLogro(int idJugador, String tag) {
        if (idJugador < 0 || tag == null) return;
        String sql = "INSERT INTO Historial_Logros (id_jugador, tag_logro) VALUES (?, ?);";
        Connection conn = conectar();
        if (!disponible(conn)) return;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            ps.setString(2, tag);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[BD] Error al registrar el evento de logro: " + e.getMessage());
        }
    }

    public static Set<String> obtenerLogrosDesbloqueados(int idJugador) {
        Set<String> tags = new LinkedHashSet<>();
        if (idJugador < 0) return tags;
        String sql = "SELECT tag_logro FROM Logros_Desbloqueados WHERE id_jugador = ? " +
                "ORDER BY fecha_desbloqueo, id_logro;";
        Connection conn = conectar();
        if (!disponible(conn)) return tags;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tags.add(rs.getString("tag_logro"));
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al consultar los logros: " + e.getMessage());
        }
        return tags;
    }

    /** Historial acumulado de logros (veces y fechas) leido con SELECT/GROUP BY sobre los eventos por partida. */
    public static List<String> obtenerHistorialLogros(int idJugador) {
        List<String> filas = new ArrayList<>();
        if (idJugador < 0) return filas;
        String sql = "SELECT tag_logro, COUNT(*) AS veces, MIN(fecha_desbloqueo) AS primera, " +
                "MAX(fecha_desbloqueo) AS ultima FROM Historial_Logros " +
                "WHERE id_jugador = ? GROUP BY tag_logro ORDER BY tag_logro;";
        Connection conn = conectar();
        if (!disponible(conn)) return filas;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(rs.getString("tag_logro") + " x" + rs.getInt("veces") +
                            "  (" + rs.getString("primera") + " -> " + rs.getString("ultima") + ")");
                }
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al consultar el historial de logros: " + e.getMessage());
        }
        return filas;
    }

    /** Resumen acumulado de apuestas, tambien leido con SELECT. */
    public static String obtenerResumenApuestas(int idJugador) {
        String sinDatos = "Apuestas: 0  |  Apostado: $0  |  Ganado: $0";
        if (idJugador < 0) return sinDatos;
        String sql = "SELECT COUNT(*) AS giros, COALESCE(SUM(monto_apostado), 0) AS apostado, " +
                "COALESCE(SUM(monto_ganado), 0) AS ganado FROM Historial_Apuestas WHERE id_jugador = ?;";
        Connection conn = conectar();
        if (!disponible(conn)) return sinDatos;
        try (conn; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idJugador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Apuestas: " + rs.getInt("giros") +
                            "  |  Apostado: $" + Math.round(rs.getDouble("apostado")) +
                            "  |  Ganado: $" + Math.round(rs.getDouble("ganado"));
                }
            }
        } catch (SQLException e) {
            System.out.println("[BD] Error al consultar el resumen de apuestas: " + e.getMessage());
        }
        return sinDatos;
    }
}
