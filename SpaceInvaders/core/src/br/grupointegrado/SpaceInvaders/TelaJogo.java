package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;


/**
 * Created by Douglas on 03/08/2015.
 */
public class TelaJogo extends TelaBase {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Image jogador;
    private Texture texturajogador;
    private Texture texturajogadordireita;
    private Texture texturajogadoresquerda;
    private boolean indodireita;
    private boolean indoesquerda;
    private boolean atirando;
    private Array<Image> tiros = new Array<Image>();
    private Texture texturaTiro;
    private Texture texturaMeteoro1;
    private Texture texturaMeteoro2;
    private Array<Image> meteoro1= new Array<Image>();
    private Array<Image> meteoro2= new Array<Image>();

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
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth,camera.viewportHeight,camera));

        initTexturas();
        initfonte();
        initinformacoes();
        intijogador();
    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");
    }

    /**
     * instancia os objetos do jogador e adiciona ao palco
     */
    private void intijogador() {
        texturajogador = new Texture("sprites/player.png");
        texturajogadordireita = new Texture("sprites/player-right.png");
        texturajogadoresquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturajogador);
        float x = (camera.viewportWidth/2) - (jogador.getWidth()/2);
        float y = 15;
        jogador.setPosition(x, y);
        palco.addActor(jogador);
    }

    /**
     * instacia as informações escritas na tela
     */
    private void initinformacoes(){
        Label.LabelStyle lbStilo = new Label.LabelStyle();
        lbStilo.font = fonte;
        lbStilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos", lbStilo);
        palco.addActor(lbPontuacao);
    }

    /**
     * instacia os objetos de fonte
     */
    private void initfonte(){
        fonte = new BitmapFont();
    }
    /**
     * Chamado a todo quadro de atuaçização do jogo, ou famoso fps;
     * @param delta tempo entre um quado e outro em segundos;
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f,.15f,25f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbPontuacao.setPosition(10, camera.viewportHeight - 20);

        atualizarMeteoros(delta);
        capiturasTeclas();
        atualizaJogador(delta);
        atualizarTiros(delta);
// atuaiza a situação do palco
        palco.act(delta);
//desenha o palco na tela
        palco.draw();
    }

    private void atualizarMeteoros(float delta) {
        int tipo = MathUtils.random(1,3);
        if (tipo == 1 ) {
            // cria meteoro 1
            Image meteoro = new Image(texturaMeteoro1);
            float x = MathUtils.random(0,camera.viewportWidth - meteoro.getImageWidth());
            float y = MathUtils.random(camera.viewportHeight, camera.viewportWidth *2);
            meteoro.setPosition(x,y);
            meteoro1.add(meteoro);
            palco.addActor(meteoro);
        }
        else {
            // cria meteoro 2
        }

        float velocidade = 300;
        for (Image meteoro : meteoro1) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade *delta;
            meteoro.setPosition(x,y);
        }
    }

    private float intervaloTiros = 0; // tempo acumulado
    private final float maximoIntervaloTiros = 0.4f; // minimo tempo entre tiros

    private void atualizarTiros(float delta) {
        intervaloTiros = intervaloTiros + delta; // acumula o tempo percorrido
        // cria um novo tiro se necessario
        if (atirando) {
            // verifica se o tempo minimo foi atingido
            if (intervaloTiros >= maximoIntervaloTiros) {
                Image tiro = new Image(texturaTiro);
                float x = jogador.getX() + (jogador.getWidth()/2) - (tiro.getWidth()/2);
                float y = jogador.getY() + jogador.getImageHeight();
                tiro.setPosition(x,y);
                tiros.add(tiro);
                palco.addActor(tiro);
                intervaloTiros = 0;
            }
        }

        float velocidade = 300; // velocidade de movimentação do tiro
        // peercorre todos os tiros existentes
        for (Image tiro : tiros) {
            // movimenta o tiro em direção ao top
            float x = tiro.getX();
            float y = tiro.getY() + (velocidade * delta);
            tiro.setPosition(x,y);
            // remove os tiros que sairam da tela
            if (tiro.getY() > camera.viewportHeight){
                tiros.removeValue(tiro,true);// remove da lista
                tiro.remove(); //remove do palco
            }
        }
    }

    /**
     * atualiza a posição do jogador
     * @param delta
     */
    private void atualizaJogador(float delta) {
        float velocidade = 200; //velocidade de moviemnto do jogador;
        if ((indodireita) && (jogador.getX() < (camera.viewportWidth - jogador.getWidth()))) {
            //inpedi que o jogador saia da tela
            float x = jogador.getX() + (velocidade*delta);
            float y = jogador.getY();
            jogador.setPosition(x,y);
        }
        else if ((indoesquerda) && (jogador.getX() > 0)) {
            //inpedi que o jogador saia da tela
            float x = jogador.getX() - (velocidade*delta);
            float y = jogador.getY();
            jogador.setPosition(x,y);
        }

        if (indodireita) {
            // trocar imagem direita
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturajogadordireita)));
        } else if (indoesquerda) {
            // trocar imagem esquerda
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturajogadoresquerda)));
        }
        else {
            // trocar imagem
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturajogador)));
        }
    }

    /**
     *  verficar se as teclas estão pressionadas
     */
    private void capiturasTeclas(){
        indodireita = false;
        indoesquerda = false;
        atirando = false;

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)){
            indoesquerda = true;
        }else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            indodireita = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            atirando = true;
        }
    }
    /**
     * ele e chamado sempre que a uma alteração no tamanho da tela
     * @param width novo valor de largura da tela
     * @param height novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.update();
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
        batch.dispose();
        palco.dispose();
        fonte.dispose();
        texturajogador.dispose();
        texturajogadordireita.dispose();
        texturajogadoresquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();
    }
}
