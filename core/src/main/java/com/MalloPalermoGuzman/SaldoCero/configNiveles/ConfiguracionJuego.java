package com.MalloPalermoGuzman.SaldoCero.configNiveles;

import java.util.ArrayList;
import java.util.List;

/**
 * Todos los datos de los niveles, dialogos y reglas, definidos directamente en Java.
 * Para cambiar un numero o un texto se edita aca y se recompila.
 */
public class ConfiguracionJuego {
    public static int maxCaracteresPagina = 150;

    private final int saldoInicial;
    private final ArrayList<ConfigNivel> niveles;
    private final String[] dialogoFinal;
    private final String fraseFinal;
    private final String textoSinMeta;

    private ConfiguracionJuego(int saldoInicial, ArrayList<ConfigNivel> niveles, String[] dialogoFinal, String fraseFinal, String textoSinMeta) {
        this.saldoInicial = saldoInicial;
        this.niveles = niveles;
        this.dialogoFinal = dialogoFinal;
        this.fraseFinal = fraseFinal;
        this.textoSinMeta = textoSinMeta;
    }

    /** Arma la configuracion completa del juego (3 niveles + dialogo final). */
    public static ConfiguracionJuego crear() {
        int[] apuestas = {100, 500, 1000, 2500, 5000};
        ArrayList<ConfigNivel> niveles = new ArrayList<>();

        //nivel 1: viernes
        niveles.add(new ConfigNivel(
                "Viernes - Introduccion",
                180f,100, 2,15000,5,0.35f, apuestas,
                paginas("Viernes - dialogoInicio",
                        "Viernes. En el colegio, tus amigos hablan de su juntada de ayer: fueron al casino y ganaron plata.",
                        "Te dicen que solo te cuentan como entrar si vas una vez. Que seguro nunca lo haces, que sos poco hombre.",
                        "Al llegar a casa, mama llora: la echaron del trabajo. Va a ser dificil mantener los gastos.",
                        "Pensas que podes ayudar. Agarras tus ahorros, 10000 pesos, y vas al casino."),
                paginas("Viernes - dialogoCierre",
                        "Ganaste mucho mas de lo que esperabas. Tus amigos te felicitan y dicen que sos un genio.",
                        "Volves a casa. Mama pregunta donde estabas. Le mentis: en lo de Mati.",
                        "Con tanta plata, planeas redoblarla para ayudar a mama.")));

        //nivel 2: sabado
        niveles.add(new ConfigNivel(
                "Sabado - Caida",
                180f, 80, 3, 25000, 8, 0.60f,
                apuestas,
                paginas("Sabado - dialogoInicio",
                        "Sabado. Tus amigos te esperan: hoy es el gran dia, a la noche vuelven al casino.",
                        "Queres seguir ayudando a mama y seguir sintiendote bien con vos mismo."),
                paginas("Sabado - dialogoCierre",
                        "Perdiste por primera vez y sentiste que le fallaste a todos. Seguiste jugando hasta quedarte con poco.",
                        "A la salida, un hombre te ofrece un prestamo. Hay que devolverlo la semana proxima o habra consecuencias.",
                        "Aceptas. En casa, mama te grita por irte. Mentis que te dormiste. Quedas castigado.")));

        //nivel 3: domingo
        niveles.add(new ConfigNivel(
                "Domingo - Saldo Cero",
                180f, 60, 4, 40000, 10, 0.80f,
                apuestas,
                paginas("Domingo - dialogoInicio",
                        "Domingo. Te despertas con la presion de la deuda. En la mesa hay un aviso de corte de luz.",
                        "Mama salio a trabajar. Viajas al casino con lo ultimo que queda en la SUBE."),
                paginas("Domingo - dialogoCierre",
                        "Entras en un ciclo de ganar y perder. Acorralado, te jugas todo en un All-In.",
                        "Perdes todo. Volves caminando bajo la lluvia, aterrado por las amenazas del prestamista.")));

        String[] dialogoFinal = paginas("dialogoFinal",
                "Al entrar, la puerta esta forzada y el living destrozado. Mama no esta.",
                "Hay un papel clavado: lo dejaron los cobradores del prestamista.");

        return new ConfiguracionJuego(
                10000, niveles, dialogoFinal,
                "Perdiste tu dinero, tu casa y tu familia. La casa siempre gana.",
                "No llegaste a tu meta. Te vas insatisfecho y con ganas de volver.");
    }

    /** Devuelve las paginas tal cual, avisando por consola si alguna supera el maximo recomendado. */
    private static String[] paginas(String contexto, String... textos) {
        for (int i = 0; i < textos.length; i++) {
            if (textos[i].length() > maxCaracteresPagina) {
                System.out.println("[AVISO] " + contexto + " pagina " + (i + 1) + " tiene "
                        + textos[i].length() + " caracteres (maximo recomendado " + maxCaracteresPagina + ")");
            }
        }
        return textos;
    }

    public int getSaldoInicial() { return saldoInicial; }
    public ArrayList<ConfigNivel> getNiveles() { return niveles; }
    public int getCantidadNiveles() { return niveles.size(); }
    public ConfigNivel getNivel(int indice) { return niveles.get(indice); }
    public String[] getDialogoFinal() { return dialogoFinal; }
    public String getFraseFinal() { return fraseFinal; }
    public String getTextoSinMeta() { return textoSinMeta; }
}
