package com.mygdx.FormasGeometricas;

import com.badlogic.gdx.Game;

/**
 * Created by Douglas on 18/08/2015.
 */
public class MainGame extends Game{
    @Override
    public void create(){
        setScreen(new TelaJogo(this));
    }
}
