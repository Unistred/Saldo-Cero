package com.MalloPalermoGuzman.SaldoCero.gestores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class GestorSonido {
    private static final float VOLUMEN_INICIAL = 0.5f;
    private static final float PASO_VOLUMEN = 0.1f;

    private float volumen = VOLUMEN_INICIAL;
    private boolean silenciado = false;
    private Sound sonidoGirar;
    // Id de la ultima reproduccion: permite aplicar cambios de volumen mientras suena.
    private long idGirar = -1L;
    //private Sound sonidoPremio;
    //private Sound sonidoChoque;

    public void cargarSonidos() {
        sonidoGirar = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/slotGirando.wav"));
        //sonidoPremio = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/premio.wav"));
        //sonidoChoque = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/choque.wav"));
    }

    public void reproducirGirar() {
        if (sonidoGirar == null) return;
        // Se reproduce ya con el volumen configurado (o 0 si esta silenciado).
        idGirar = sonidoGirar.play(volumenEfectivo());
    }

    //public void reproducirPremio() {
    //    if (sonidoPremio != null) sonidoPremio.play();
    //}

    //public void reproducirChoque() {
    //    if (sonidoChoque != null) sonidoChoque.play();
    //}

    public void fijarVolumen(float volumen) {
        this.volumen = redondear(MathUtils.clamp(volumen, 0f, 1f));
        aplicarVolumen();
    }

    public void subirVolumen() {
        silenciado = false;
        fijarVolumen(volumen + PASO_VOLUMEN);
    }

    public void bajarVolumen() {
        silenciado = false;
        fijarVolumen(volumen - PASO_VOLUMEN);
    }

    public void alternarSilencio() {
        silenciado = !silenciado;
        aplicarVolumen();
    }

    public float getVolumen() {
        return volumen;
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    private float volumenEfectivo() {
        return silenciado ? 0f : volumen;
    }

    /** Afecta al sonido que ya esta sonando; el proximo usara volumenEfectivo() al llamar a play(). */
    private void aplicarVolumen() {
        if (sonidoGirar != null && idGirar >= 0) {
            sonidoGirar.setVolume(idGirar, volumenEfectivo());
        }
    }

    private float redondear(float valor) {
        return Math.round(valor * 100f) / 100f;
    }

    public void dispose() {
        if (sonidoGirar != null) sonidoGirar.dispose();
        //if (sonidoPremio != null) sonidoPremio.dispose();
        //if (sonidoChoque != null) sonidoChoque.dispose();
    }
}
