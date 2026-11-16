package com.MalloPalermoGuzman.SaldoCero.baseDeDatos;

import java.io.File;
import java.sql.*;

public class GestorBD {

    private static final String RUTA_USUARIO = System.getProperty("user.home");
    private static final String URL = "jdbc:sqlite:" + RUTA_USUARIO + File.separator + "CasinoSurvival_saldocero.db";

    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;"); // Integridad referencial
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }


    public static void inicializarBaseDeDatos() {
        String sqlEsquema =

                "CREATE TABLE IF NOT EXISTS Jugador (" +
                        " id_jugador INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " nombre_usuario TEXT NOT NULL," +
                        " fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP);" +

                        "CREATE TABLE IF NOT EXISTS Registro_Jornadas (" +
                        " id_jornada INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " id_jugador INTEGER NOT NULL," +
                        " numero_dia INTEGER NOT NULL," +
                        " saldo_inicial REAL NOT NULL," +
                        " saldo_final REAL NOT NULL," +
                        " cordura_final INTEGER NOT NULL," +
                        " resultado TEXT NOT NULL," +
                        " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE);" +

                        "CREATE TABLE IF NOT EXISTS Historial_Apuestas (" +
                        " id_apuesta INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " id_jugador INTEGER NOT NULL," +
                        " minijuego TEXT NOT NULL," +
                        " monto_apostado REAL NOT NULL," +
                        " monto_ganado REAL NOT NULL," +
                        " saldo_resultante REAL NOT NULL," +
                        " cordura_resultante INTEGER NOT NULL," +
                        " timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE);" +

                        "CREATE TABLE IF NOT EXISTS Logros_Desbloqueados (" +
                        " id_logro INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " id_jugador INTEGER NOT NULL," +
                        " tag_logro TEXT NOT NULL," +
                        " fecha_desbloqueo DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        " FOREIGN KEY (id_jugador) REFERENCES Jugador(id_jugador) ON DELETE CASCADE);" +


                        "CREATE VIEW IF NOT EXISTS VW_JornadasExitosas AS " +
                        "SELECT * FROM Registro_Jornadas WHERE saldo_final > saldo_inicial;" +


                        "CREATE TRIGGER IF NOT EXISTS TRG_ValidaSaldoApuesta " +
                        "BEFORE INSERT ON Historial_Apuestas " +
                        "BEGIN " +
                        "  SELECT RAISE(ABORT, 'Monto apostado no puede ser negativo') WHERE NEW.monto_apostado < 0;" +
                        "END;";

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlEsquema);
            System.out.println("¡Base de datos y estructuras creadas con éxito en Java!");
        } catch (SQLException e) {
            System.out.println("Error al inicializar la BD: " + e.getMessage());
        }
    }


    public static void actualizarSaldoJornada(int idJornada, double nuevoSaldo, String resultado) {
        String sql = "UPDATE Registro_Jornadas SET saldo_final = ?, resultado = ? WHERE id_jornada = ?;";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setString(2, resultado);
            pstmt.setInt(3, idJornada);
            pstmt.executeUpdate();
            System.out.println("UPDATE ejecutado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void eliminarApuestaErronea(int idApuesta) {
        String sql = "DELETE FROM Historial_Apuestas WHERE id_apuesta = ?;";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idApuesta);
            pstmt.executeUpdate();
            System.out.println("DELETE ejecutado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static double obtenerTotalApostado(int idJugador) {
        String sql = "SELECT SUM(monto_apostado) AS total FROM Historial_Apuestas WHERE id_jugador = ?;";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    public static void registrarApuesta(int idJugador, String minijuego, double apostado, double ganado, double saldoRes, int cordura) {
        String sql = "INSERT INTO Historial_Apuestas (id_jugador, minijuego, monto_apostado, monto_ganado, saldo_resultante, cordura_resultante) VALUES (?, ?, ?, ?, ?, ?);";
        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJugador);
            pstmt.setString(2, minijuego);
            pstmt.setDouble(3, apostado);
            pstmt.setDouble(4, ganado);
            pstmt.setDouble(5, saldoRes);
            pstmt.setInt(6, cordura);
            pstmt.executeUpdate();
            System.out.println("Procedimiento (método Java) ejecutado: Apuesta registrada.");
        } catch (SQLException e) {
            System.out.println("Error en procedimiento de apuesta: " + e.getMessage());
        }
    }
    public static void insertarJugador(String nombreUsuario) {
        String sql = "INSERT INTO Jugador (nombre_usuario) VALUES (?);";


        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, nombreUsuario);


            pstmt.executeUpdate();
            System.out.println("¡Jugador '" + nombreUsuario + "' guardado con éxito en la base de datos!");

        } catch (SQLException e) {
            System.out.println("Error al insertar el jugador: " + e.getMessage());
        }
    }
    public static void insertarApuesta(int idJugador, String minijuego, double montoApostado, double montoGanado, double saldoResultante, int corduraResultante) {
        String sql = "INSERT INTO Historial_Apuestas (id_jugador, minijuego, monto_apostado, monto_ganado, saldo_resultante, cordura_resultante) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setInt(1, idJugador);
            pstmt.setString(2, minijuego);
            pstmt.setDouble(3, montoApostado);
            pstmt.setDouble(4, montoGanado);
            pstmt.setDouble(5, saldoResultante);
            pstmt.setInt(6, corduraResultante);

            pstmt.executeUpdate();
            System.out.println("¡Apuesta registrada correctamente!");

        } catch (SQLException e) {
            System.out.println("Error al registrar la apuesta: " + e.getMessage());
        }
    }
}