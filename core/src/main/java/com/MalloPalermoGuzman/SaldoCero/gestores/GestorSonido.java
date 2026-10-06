package com.MalloPalermoGuzman.SaldoCero.gestores;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
public class GestorSonido {
    private Sound sonidoGirar;
    //private Sound sonidoPremio;
    //private Sound sonidoChoque;

    public void cargarSonidos() {
        sonidoGirar = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/slotGirando.wav"));
        //sonidoPremio = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/premio.wav"));
        //sonidoChoque = Gdx.audio.newSound(Gdx.files.internal("audio/efectos/choque.wav"));
    }

    public void reproducirGirar() {
        if (sonidoGirar != null) sonidoGirar.play();
    }

    //public void reproducirPremio() {
    //    if (sonidoPremio != null) sonidoPremio.play();
    //}

    //public void reproducirChoque() {
    //    if (sonidoChoque != null) sonidoChoque.play();
    //}

    public void dispose() {
        if (sonidoGirar != null) sonidoGirar.dispose();
        //if (sonidoPremio != null) sonidoPremio.dispose();
       // if (sonidoChoque != null) sonidoChoque.dispose();
    }
}
