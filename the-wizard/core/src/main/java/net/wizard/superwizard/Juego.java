/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.wizard.superwizard;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author raulr
 */
public class Juego extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    int difficulty = 2;

    public void create() {
            batch = new SpriteBatch();
            font = new BitmapFont(); // use libGDX's default Arial font
            this.setScreen(new MainScreen(this, difficulty));
    }

    public void render() {
            super.render(); // important!
    }

    public void dispose() {
            batch.dispose();
            font.dispose();
    }
}
