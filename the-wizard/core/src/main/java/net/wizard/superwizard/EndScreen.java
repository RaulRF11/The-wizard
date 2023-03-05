/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.wizard.superwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author raulr
 */
public class EndScreen implements Screen {
    final Juego game;
    Texture background, Tmenu;
    Rectangle playAgain, backToMenu;

    OrthographicCamera camera;
    Vector3 touchPoint;
    
    boolean win;
    int score, difficulty;
    
    ArrayList<Resultado> results = new ArrayList();
    
    escribirXML escribir = new escribirXML();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document documento = null;

    public EndScreen(final Juego game, boolean win, int score, int difficulty) {
        this.game = game;
        this.win = win;
        this.score = score;
        this.difficulty = difficulty;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchPoint = new Vector3();
        
        background = new Texture(Gdx.files.internal("background.png"));
        Tmenu = new Texture(Gdx.files.internal("endMenu.png"));
        
        playAgain = new Rectangle(257, 270 - 110 / 2, 285, 30);
        backToMenu = new Rectangle(257, 205 - 110 / 2, 285, 34);
        
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File("C:\\Users\\raulr\\Documents\\NetBeansProjects\\the-wizard\\core\\src\\main\\java\\net\\wizard\\superwizard\\archivo.xml"));
        } catch (ParserConfigurationException | IOException | SAXException pce) {
            pce.printStackTrace();
        }
        
        leerResultados();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.font.draw(game.batch, "Tu puntuación: " + score, 370, 440);
        
        if(!win)
            game.font.draw(game.batch, "Se acabó el juego, has perdido todas las vidas!! ", 260, 460);
        else
            game.font.draw(game.batch, "ENHORABUENA, HAS GANADO! ", 320, 460);
        game.batch.draw(Tmenu, 250, 200 - 110 / 2, 300, 110);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (playAgain.contains(touchPoint.x, touchPoint.y)) {
                    //Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game, 3, 3, 0, false, difficulty));
                    return;
            }
            if (backToMenu.contains(touchPoint.x, touchPoint.y)) {
                    //Assets.playSound(Assets.clickSound);
                    game.setScreen(new MainScreen(game, difficulty));
                    return;
            }
        }
    }

    @Override
    public void show() {
        
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        
    }
    
    public void leerResultados() {
        // Recorre cada uno de los nodos 'Resultado'
        NodeList resultados2 = documento.getElementsByTagName("Resultado");
        
        for (int j = 0; j < resultados2.getLength(); j++) {
            Resultado resultado = new Resultado();
            Node resultado2 = resultados2.item(j);
            Element elemento = (Element) resultado2;
            //resultado.setId(Integer.parseInt(elemento.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue()));
            resultado.setResultado(Integer.parseInt(elemento.getElementsByTagName("resultado").item(0).getChildNodes().item(0).getNodeValue()));

            this.results.add(resultado);
        }
        Resultado resultado = new Resultado();
        resultado.setResultado(score);
        this.results.add(resultado);
        
        this.escribir();
    }
    
    public void escribir() {
        try {
            escribir.escribirResultado(results);
        } catch (TransformerException ex) {
            Logger.getLogger(EndScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
