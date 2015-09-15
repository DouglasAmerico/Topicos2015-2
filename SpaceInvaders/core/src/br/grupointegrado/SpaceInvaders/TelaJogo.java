package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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
    private Stage palcoInformacoes;
    private BitmapFont fonte;
    private Label lbPontuacao;
    private Label lbgameOver;
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

    private Array<Texture> texturasExplosao = new Array<Texture>();
    private Array<Explosao> explosoes = new Array<Explosao>();

    private Sound sonTiro;
    private Sound sonExplosao;
    private Sound sonGameOver;
    private Music somMusica;


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
        palcoInformacoes = new Stage(new FillViewport(camera.viewportWidth,camera.viewportHeight,camera));

        initTexturas();
        initfonte();
        initinformacoes();
        intijogador();
        initSons();
    }

    private void initSons() {
        sonTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        sonExplosao = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        sonGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.mp3"));
        somMusica = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
        somMusica.setLooping(true);
    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");
        for (int i = 1; i<= 17;i++) {
            Texture text = new Texture("sprites/explosion-"+i+".png");
            texturasExplosao.add(text);
        }
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

        lbgameOver = new Label("Game Over!",lbStilo);
        lbgameOver.setVisible(false);
        palcoInformacoes.addActor(lbgameOver);
    }

    /**
     * instacia os objetos de fonte
     */
    private void initfonte(){
        FreeTypeFontGenerator gerador = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.size = 24;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        param.shadowColor = Color.BLACK;


        //fonte = new BitmapFont();
        fonte = gerador.generateFont(param);
        gerador.dispose();
    }
    /**
     * Chamado a todo quadro de atuaçização do jogo, ou famoso fps;
     * @param delta tempo entre um quado e outro em segundos;
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.15f, .15f, 25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        lbPontuacao.setPosition(10, camera.viewportHeight - lbPontuacao.getPrefHeight() - 20);
        lbPontuacao.setText(pontuacao + " Pontos");

        lbgameOver.setPosition(camera.viewportWidth / 2 - (lbgameOver.getPrefHeight() / 2), camera.viewportHeight / 2);
        lbgameOver.setVisible(gameOver == true);

        atualizarExplosoes(delta);

        if (gameOver == false) {
            if (!somMusica.isPlaying()) {
                sonExplosao.play();
            }

            detectaColisoes(meteoro1, 5);
            detectaColisoes(meteoro2, 5);
            atualizarMeteoros(delta);
            capiturasTeclas();
            atualizaJogador(delta);
            atualizarTiros(delta);
        } else {
            if (somMusica.isPlaying()) {
                somMusica.stop();
            }
        }

        // atuaiza a situação do palco
        palco.act(delta);
        //desenha o palco na tela
        palco.draw();

        //desenhar o palco de informações
        palcoInformacoes.act(delta);
        palcoInformacoes.draw();
    }

    private void atualizarExplosoes(float delta) {
        for (Explosao explosao : explosoes){
            if (explosao.getEstagio() >= 16){ // verifica se a explosão chegou ao fim
                explosoes.removeValue(explosao,true); //remove a explosão do array
                explosao.getAtor().remove();// remove o ator do palco
            } else { // ainda não chegou ao fim
                explosao.atualizar(delta);
            }
        }
    }

    private Rectangle recJogador = new Rectangle();
    private Rectangle recTiro = new Rectangle();
    private Rectangle recMeteoro = new Rectangle();
    private int pontuacao = 0;
    private boolean gameOver = false;

    private void detectaColisoes(Array<Image> meteoros, int valePonto) {
        recJogador.set(jogador.getX(),jogador.getY(),jogador.getWidth(),jogador.getHeight());
        for (Image meteoro : meteoros){
            recMeteoro.set(meteoro.getX(),meteoro.getY(),meteoro.getWidth(),meteoro.getHeight());
            // detecta colisoes com tiros
            for (Image tiro : tiros) {
                recTiro.set(tiro.getX(),tiro.getY(),tiro.getWidth(),tiro.getHeight());
                if (recMeteoro.overlaps(recTiro)){
                    // aqui ocorre uma colisão do tiro com o meteoro 1
                    pontuacao += valePonto;
                    tiro.remove(); // remove do palco
                    tiros.removeValue(tiro, true); // remove da lista
                    meteoro.remove(); // remove do palco
                    meteoros.removeValue(meteoro, true); // remove da lista
                    criarExplosao(meteoro.getX() + meteoro.getWidth() /2 ,meteoro.getY() + meteoro.getHeight()/2);

                }

            }

            // detecta colisão com player
            if (recJogador.overlaps(recMeteoro)) {
                gameOver = true;
                sonGameOver.play();
            }
        }
    }

    /**
     * cria a explosão na posição x e y
     * @param x
     * @param y
     */
    private void criarExplosao(float x, float y) {
        Image ator = new Image(texturasExplosao.get(0));
        ator.setPosition(x - ator.getWidth(), y - ator.getPrefHeight());
        palco.addActor(ator);

        Explosao explosao = new Explosao(ator,texturasExplosao);
        explosoes.add(explosao);
        sonExplosao.play();
    }

    private void atualizarMeteoros(float delta) {
        int qtdaMeteoros = meteoro1.size + meteoro2.size; // retorna a quantidade de meteoros criados

        if (qtdaMeteoros <8) {

            int tipo = MathUtils.random(1, 5); // retorna 1 ou 2 aleatoriamente
            if (tipo == 1) {
                // cria meteoro 1
                Image meteoro = new Image(texturaMeteoro1);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getImageWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportWidth * 2);
                meteoro.setPosition(x, y);
                meteoro1.add(meteoro);
                palco.addActor(meteoro);
            } else if (tipo == 2){
                // cria meteoro 2
                Image meteoro = new Image(texturaMeteoro2);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getImageWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportWidth * 2);
                meteoro.setPosition(x, y);
                meteoro2.add(meteoro);
                palco.addActor(meteoro);
            }
        }

        float velocidade1 = 100; // pixels por segundos
        for (Image meteoro : meteoro1) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade1 *delta;
            meteoro.setPosition(x,y); // atualiza a posição do meteoro
            if ((meteoro.getY() + meteoro.getImageHeight()) < 0) {
                meteoro.remove(); // remove do palco
                meteoro1.removeValue(meteoro, true); // remove da linha
            }
        }

        float velocidade2 = 160; // pixels por segundos
        for (Image meteoro : meteoro2) {
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade2 *delta;
            meteoro.setPosition(x,y); // atualiza a posição do meteoro
            if ((meteoro.getY() + meteoro.getImageHeight()) < 0) {
                meteoro.remove(); // remove do palco
                meteoro2.removeValue(meteoro, true); // remove da linha
            }
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
                sonTiro.play();
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
        palcoInformacoes.dispose();
        fonte.dispose();
        texturajogador.dispose();
        texturajogadordireita.dispose();
        texturajogadoresquerda.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();
        for (Texture text : texturasExplosao){
            text.dispose();
        }
        sonTiro.dispose();
        sonExplosao.dispose();
    }
}
