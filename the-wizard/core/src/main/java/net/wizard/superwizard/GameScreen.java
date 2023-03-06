package net.wizard.superwizard;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class GameScreen extends ScreenAdapter {
    static final int GAME_RUNNING = 0;
    static final int GAME_PAUSED = 1;
    int state;
    Juego game;
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    
    Wizard wizard;
    Fire fire;
    Skull skull;
    Live live;
    Reload reload;
    ArrayList<Enemy> enemies = new ArrayList();
    ArrayList<Coin> coins = new ArrayList();
    ArrayList<Bird> birds = new ArrayList();
    ArrayList<Mushroom> mushrooms = new ArrayList();
    
    int score = 0, numMushrooms = 0, numBirds = 0, numCoins = 0, numEnemies = 0, numAleatorio = 0, lives = 0, numFires = 0, difficulty;
    float time = 0, timeInmortal = 7;
    final float TIMETOSHOOT = 3f;
    boolean level1 = false, win = false, inmortal = false, sound;
    
    Texture Tpause, TmenuPause, fondo, Tsound;
    Vector3 touchPoint;
    
    Sound clickSound, coinSound, hitSound;
    Music music;
    
    public GameScreen(Juego game, int lives, int numFires, int score, boolean level1, int difficulty, boolean sound) {
        this.game = game;
        this.lives = lives;
        this.numFires = numFires;
        this.score = score;
        this.level1 = level1;
        this.difficulty = difficulty;
        this.sound = sound;
        
        if(this.difficulty == 1) {
            numEnemies = 2;
            numBirds = 2;
            numMushrooms = 2;
        } else if(this.difficulty == 2) {
            numEnemies = 3;
            numBirds = 3;
            numMushrooms = 3;
        } else if(this.difficulty == 3) {
            numEnemies = 5;
            numBirds = 5;
            numMushrooms = 5;
            this.numFires = 2;
        }
        state = GAME_RUNNING;
        
        numAleatorio = (int) (Math.random()* 15 + 6);
        numCoins = numAleatorio;
        
        Tpause = new Texture(Gdx.files.internal("pause.png"));
        TmenuPause = new Texture(Gdx.files.internal("menuPause.png"));
        fondo = new Texture(Gdx.files.internal("background.png"));
        Tsound = new Texture(Gdx.files.internal("sound.png"));
        
        touchPoint = new Vector3();
        
        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
    }

    public void show() {
        if(!level1)
            map = new TmxMapLoader().load("level1.tmx");
        else 
            map = new TmxMapLoader().load("level2.tmx");
        
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);

        wizard = new Wizard(sound);
        wizard.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        wizard.setPosition(10, 10);
        stage.addActor(wizard);
        
        skull = new Skull();
        skull.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        skull.setPosition(100, 10);
        stage.addActor(skull);
        
        live = new Live();
        live.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        live.setPosition(105, 10);
        stage.addActor(live);
        
        reload = new Reload();
        reload.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        reload.setPosition(42, 10);
        stage.addActor(reload);
        
        for(int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy();
            enemy.layer = (TiledMapTileLayer) map.getLayers().get("walls");
            numAleatorio = (int) (Math.random()* 150 + 35);
            enemy.setPosition(numAleatorio, 10);
            stage.addActor(enemy);
            enemies.add(enemy);
        }
        
        for(int i = 0; i < numCoins; i++) {
            Coin coin = new Coin();
            coin.layer = (TiledMapTileLayer) map.getLayers().get("walls");
            numAleatorio = (int) (Math.random()* 150 + 35);
            coin.setPosition(numAleatorio, 10);
            stage.addActor(coin);
            coins.add(coin);
        }
        
        for(int i = 0; i < numBirds; i++) {
            Bird bird = new Bird();
            bird.layer = (TiledMapTileLayer) map.getLayers().get("walls");
            numAleatorio = (int) (Math.random()* 150 + 35);
            bird.setPosition(numAleatorio, 6);
            stage.addActor(bird);
            birds.add(bird);
        }
        
        for(int i = 0; i < numMushrooms; i++) {
            Mushroom mushroom = new Mushroom();
            mushroom.layer = (TiledMapTileLayer) map.getLayers().get("walls");
            numAleatorio = (int) (Math.random()* 150 + 35);
            mushroom.setPosition(numAleatorio, 6);
            stage.addActor(mushroom);
            mushrooms.add(mushroom);
        }
    }
    
    public void update( float delta) {
        if(sound)
            music.play();
        else
            music.pause();
        if(wizard.getY() < 0 && wizard.getY() > -20) {
            lives--;
            
            if(lives < 0) {
                music.dispose();
                game.setScreen(new EndScreen(game, win, score, difficulty, sound));
            } else 
                wizard.setPosition(10, 10);
        }
        
        if(wizard.getX() >= 203 && wizard.getX() <= 204) {
            if(!level1) {
                level1 = true;
                game.setScreen(new GameScreen(game, lives, numFires, score, level1, difficulty, sound));
                JOptionPane.showMessageDialog(null, "NIVEL 1 COMPLETADO!\nA POR EL NIVEL 2!");
            } else {
                music.dispose();
                win = true;
                game.setScreen(new EndScreen(game, win, score, difficulty, sound));
            }
        }
        
        if(wizard.rectangulo().overlaps(skull.rectangulo()) && !skull.borrar){
            inmortal = true;
            skull.remove();
            skull.setVisible(false);
            skull.borrar = true;
        }
        
        if(wizard.rectangulo().overlaps(live.rectangulo()) && !live.borrar && lives < 3){
            lives++;
            live.remove();
            live.setVisible(false);
            live.borrar = true;
        }
        
        if(wizard.rectangulo().overlaps(reload.rectangulo()) && !reload.borrar) {
            if(difficulty == 1 || difficulty == 2 && numFires < 3) {
                numFires++;
                reload.remove();
                reload.setVisible(false);
                reload.borrar = true;
            }
            
            if(difficulty == 3 && numFires < 2) {
                numFires++;
                reload.remove();
                reload.setVisible(false);
                reload.borrar = true;
            }
        }
        
        if(inmortal == true){
            timeInmortal -= delta;
        }
        
        if(timeInmortal <= 0){
            inmortal = false;
        }
        
        for(Enemy enemy : enemies) {
            if(wizard.rectangulo().overlaps(enemy.rectangulo()) && !enemy.fireOverlaps && !inmortal) {
                lives--;
                
                if(lives < 0) {
                    music.dispose();
                    game.setScreen(new EndScreen(game, win, score, difficulty, sound));
                } else 
                    wizard.setPosition(10, 10);
            }
            if(fire != null) {
                if(fire.rectangulo().overlaps(enemy.rectangulo()) && !fire.enemyOverlaps) {
                    if(sound)
                        hitSound.play();
                    enemy.fireOverlaps = true;
                    fire.enemyOverlaps = true;
                    fire.remove();
                    enemy.remove();
                }
            }
        }
        
        for(Coin coin : coins) {
            if(wizard.rectangulo().overlaps(coin.rectangulo()) && !coin.wizardOverlaps) {
                if(sound)
                    coinSound.play();
                score += 10;
                coin.wizardOverlaps = true;
                coin.remove();
            }
        }
        
        for(Bird bird : birds) {
            if(wizard.rectangulo().overlaps(bird.rectangulo()) && !bird.fireOverlaps && !inmortal) {
                lives--;
                
                if(lives < 0) {
                    music.dispose();
                    game.setScreen(new EndScreen(game, win, score, difficulty, sound));
                } else 
                    wizard.setPosition(10, 10);
            }
            if(fire != null) {
                if(fire.rectangulo().overlaps(bird.rectangulo()) && !fire.enemyOverlaps) {
                    if(sound)
                        hitSound.play();
                    bird.fireOverlaps = true;
                    fire.enemyOverlaps = true;
                    fire.remove();
                    bird.remove();
                }
            }
        }
        
        for(Mushroom mushroom : mushrooms) {
            if(wizard.rectangulo().overlaps(mushroom.rectangulo()) && !mushroom.fireOverlaps && !inmortal) {
                lives--;
                
                if(lives < 0) {
                    music.dispose();
                    game.setScreen(new EndScreen(game, win, score, difficulty, sound));
                } else 
                    wizard.setPosition(10, 10);
            }
            if(fire != null) {
                if(fire.rectangulo().overlaps(mushroom.rectangulo()) && !fire.enemyOverlaps) {
                    if(sound)
                        hitSound.play();
                    mushroom.fireOverlaps = true;
                    fire.enemyOverlaps = true;
                    fire.remove();
                    mushroom.remove();
                }
            }
        }
        
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            if(sound)
                clickSound.play();
            state = GAME_PAUSED;
            music.pause();
            return;
        }
        
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            
            if (time >= TIMETOSHOOT && numFires > 0) {
                fire = new Fire(wizard.leftTouched(), wizard.rightTouched());
                fire.layer = (TiledMapTileLayer) map.getLayers().get("walls");
                fire.setPosition(wizard.getX(), wizard.getY());
                stage.addActor(fire);
                time = 0;
                numFires--;
            }
        }
    }
    
    public void draw(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = wizard.getX();
        camera.update();
        
        renderer.setView(camera);
        renderer.render();
        
        stage.act(delta);
        stage.draw();
        
        game.batch.enableBlending();
        game.batch.begin();
        game.font.draw(game.batch, "VIDAS: " + lives, 10, 470);
        game.font.draw(game.batch, "DISPAROS: " + numFires, 80, 470);
        game.font.draw(game.batch, "PUNTUACIÓN: " + score, 180, 470);
        if(!level1)
            game.font.draw(game.batch, "NIVEL 1", 740, 25);
        else
            game.font.draw(game.batch, "NIVEL 2", 740, 25);
        if(inmortal)
            game.font.draw(game.batch, "Tiempo de inmortalidad: " + timeInmortal, 310, 470);
        game.batch.draw(Tpause, 715, 510 - 110 / 2, 80, 20);
        game.batch.end();
    }

    public void render(float delta) {
        time = time + delta;
        switch(state) {
            case GAME_RUNNING:
                update(delta);
                draw(delta);
                break;
            case GAME_PAUSED:
                pause();
                break;
        }
    }

    public void dispose() {
    }

    public void hide() {
    }

    @Override
    public void pause() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(fondo, 0, 0, 800, 480);
        game.batch.end();
        
        game.batch.enableBlending();
        game.batch.begin();
        game.batch.draw(TmenuPause, 278, 225 - 110 / 2, 250, 100);
        game.batch.draw(Tsound, 250, 350 - 110 / 2, 300, 110);
        game.batch.end();
            
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if(sound) {
                clickSound.play();
                music.play();
            }
            state = GAME_RUNNING;
            return;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(sound)
                clickSound.play();
            game.setScreen(new MainScreen(game, difficulty, sound));
            return;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            if(sound) {
                clickSound.play();
                sound = false;
                wizard.setSound(false);
                JOptionPane.showMessageDialog(null, "Se ha desactivado el sonido");
            } else {
                JOptionPane.showMessageDialog(null, "El sonido ya está desactivado");
            }

            return;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            if(!sound) {
                sound = true;
                wizard.setSound(true);
                JOptionPane.showMessageDialog(null, "Se ha activado el sonido");
            } else {
                clickSound.play();
                JOptionPane.showMessageDialog(null, "El sonido ya está activado");
            }

            return;
        }
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    public void resume() {
    }
}
