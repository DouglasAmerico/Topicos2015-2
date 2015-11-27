package br.grupointegrado.ads.flappybird;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Douglas Americo on 26/10/2015.
 */
public class Obstaculo {
    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posX;
    private float posYCima, posYBaixo;
    private float largura, altura;
    private boolean passou;

    private Obstaculo ultimoObstaculo; // Ultimo antes do atual
    private final Texture textureCima;
    private final Texture textureBaixo;

    public Obstaculo(World mundo, OrthographicCamera camera, Obstaculo ultimoObstaculo, Texture textureCima, Texture textureBaixo) {
        this.mundo = mundo;
        this.camera = camera;
        this.ultimoObstaculo = ultimoObstaculo;
        this.textureCima = textureCima;
        this.textureBaixo = textureBaixo;

        initPosicao();
        initCorpoCima();
        initCorpoBaixo();
    }

    private void initCorpoBaixo(){
        corpoBaixo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYBaixo);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);
        Util.criarForma(corpoBaixo, shape, "OBSTACULO_BAIXO");

        shape.dispose();
    }

    public void remove(){
        mundo.destroyBody(corpoCima);
        mundo.destroyBody(corpoBaixo);
    }

    private void initCorpoCima(){
        corpoCima = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX, posYCima);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2, altura / 2);
        Util.criarForma(corpoCima, shape, "OBSTACULO_CIMA");

        shape.dispose();
    }

    private void initPosicao() {
        largura = 40 / Util.PIXEL_METRO;
        altura = camera.viewportHeight / Util.PIXEL_METRO;

        float xInicial = largura + (camera.viewportWidth / 2 / Util.PIXEL_METRO);

        if (ultimoObstaculo != null) {
            xInicial = ultimoObstaculo.getX();
        }
        posX = xInicial + 4; // 4 e o espaço entre os obstaculos

        // Parcela e o tamanho de tela dividido por 6, para encontrar a
        // posicao Y do obstaculo
        float parcela = (altura - Util.ALTURA_CHAO) / 6;

        int multiplicador = MathUtils.random(1, 3);

        posYBaixo = Util.ALTURA_CHAO + (parcela * multiplicador) - (altura / 2);

        posYCima = posYBaixo * altura + 2f;
    }

    public float getX(){
        return this.posX;
    }

    public boolean isPassou() {
        return passou;
    }

    public void setPassou(boolean passou) {
        this.passou = passou;
    }

    public float getLargura() {
        return largura;
    }

    public void setLargura(float largura) {
        this.largura = largura;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void renderisar(SpriteBatch pincel) {
        float x = (corpoCima.getPosition().x - (largura/2)) * Util.PIXEL_METRO;
        float y = (corpoCima.getPosition().y - (altura/2)) * Util.PIXEL_METRO;

        pincel.draw(textureCima, x,y, largura* Util.PIXEL_METRO, altura * Util.PIXEL_METRO);


        x = (corpoBaixo.getPosition().x - (largura/2) * Util.PIXEL_METRO);
        y = (corpoBaixo.getPosition().y - (altura/2) * Util.PIXEL_METRO);
        pincel.draw(textureBaixo,x,y,largura * Util.PIXEL_METRO, altura * Util.PIXEL_METRO);
    }
}
