/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.wizard.superwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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
public class ScoresScreen extends ScreenAdapter {
    final Juego game;
    Texture background, back;

    OrthographicCamera camera;
    
    Rectangle goback;
    Vector3 touchPoint;
    
    ArrayList<Resultado> results = new ArrayList();

    escribirXML escribir = new escribirXML();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document documento = null;
    
    int difficulty;
    
    boolean sound;
    
    Sound clickSound;

    public ScoresScreen(final Juego game, int difficulty, boolean sound) {
        this.game = game;
        this.difficulty = difficulty;
        this.sound = sound;

        camera = new OrthographicCamera(800, 480);
        camera.position.set(800 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        
        background = new Texture(Gdx.files.internal("background.png"));
        back = new Texture(Gdx.files.internal("back.png"));
        
        goback = new Rectangle(25, 75 - 110 / 2, 100, 20);
        
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
        
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File("C:\\Users\\raulr\\Documents\\NetBeansProjects\\the-wizard\\core\\src\\main\\java\\net\\wizard\\superwizard\\archivo.xml"));
        } catch (ParserConfigurationException | IOException | SAXException pce) {
            pce.printStackTrace();
        }
        
        leerResultados();
    }
    
    public void update() {
        if(results != null) {
            for (int i = 0; i < results.size(); i++) {
                for (int j = results.size() - 1; j > i; j--) {
                    if (results.get(j).getResultado() > results.get(i).getResultado()) {
                        Resultado aux = results.get(i);
                        results.set(i, results.get(j));
                        results.set(j, aux);
                    }
                }
            }
        }
        
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (goback.contains(touchPoint.x, touchPoint.y)) {
                if(sound)
                    clickSound.play();
                game.setScreen(new MainScreen(game, difficulty, sound));
                return;
            }
        }
    }
    
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(background, 0, 0, 800, 480);
        game.batch.end();
        
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(back, 20, 20, 110, 20);
        game.font.draw(game.batch, "Mejores puntuaciones: ", 360, 420);
        if(results != null) {
            if(results.size() <= 0)
                game.font.draw(game.batch, "No hay puntuaciones disponibles", 325, 400);
            if(results.size() >= 1)
                game.font.draw(game.batch, "Puntuaci??n 1: " + results.get(0).getResultado(), 370, 400);
            if(results.size() >= 2)
                game.font.draw(game.batch, "Puntuaci??n 2: " + results.get(1).getResultado(), 370, 380);
            if(results.size() >= 3)
                game.font.draw(game.batch, "Puntuaci??n 3: " + results.get(2).getResultado(), 370, 360);
            if(results.size() >= 4)
                game.font.draw(game.batch, "Puntuaci??n 4: " + results.get(3).getResultado(), 370, 340);
            if(results.size() >= 5)
                game.font.draw(game.batch, "Puntuaci??n 5: " + results.get(4).getResultado(), 370, 320);
            if(results.size() >= 6)
                game.font.draw(game.batch, "Puntuaci??n 6: " + results.get(5).getResultado(), 370, 300);
            if(results.size() >= 7)
                game.font.draw(game.batch, "Puntuaci??n 7: " + results.get(6).getResultado(), 370, 280);
            if(results.size() >= 8)
                game.font.draw(game.batch, "Puntuaci??n 8: " + results.get(7).getResultado(), 370, 260);
            if(results.size() >= 9)
                game.font.draw(game.batch, "Puntuaci??n 9: " + results.get(8).getResultado(), 370, 240);
            if(results.size() >= 10)
                game.font.draw(game.batch, "Puntuaci??n 10: " + results.get(9).getResultado(), 370, 220);
        }
        game.batch.end();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
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
            resultado.setResultado(Integer.parseInt(elemento.getElementsByTagName("resultado").item(0).getChildNodes().item(0).getNodeValue()));

            this.results.add(resultado);
        }
        
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
