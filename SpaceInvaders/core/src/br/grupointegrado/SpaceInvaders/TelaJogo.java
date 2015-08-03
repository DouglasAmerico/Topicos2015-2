package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Douglas on 03/08/2015.
 */
public class TelaJogo extends TelaBase {
    /**
     * Contrutor Padrão da Tela de Jogo
     * @param game Referencia para a classe principal
     */
    public TelaJogo(MainGame game) {
        super(game);
    }

    /**
     * Chamado quando a tela é exebida
     */
    @Override
    public void show() {
    }

    /**
     * Chamado a todo quadro de atuaçização do jogo, ou famoso fps;
     * @param delta tempo entre um quado e outro em segundos;
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f,.15f,25f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * ele e chamado sempre que a uma alteração no tamanho da tela
     * @param width novo valor de largura da tela
     * @param height novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * é chamado sempre que o jogo for minimizado
     */
    @Override
    public void pause() {
    }

    /**
     * e chamado sempre que o jogo voltar  para o primeiro palano
     */
    @Override
    public void resume() {
    }

    /**
     * é chamado quando a nossa tela for destruida
     */
    @Override
    public void dispose() {
    }
}
