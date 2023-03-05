package net.wizard.superwizard;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Skull extends Image{
    TiledMapTileLayer layer;
    Texture calaveraTexture;
    Sound golpear;
    final float width = 29;
    final float height = 30;
     
    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;

    final float GRAVITY = -2.5f;
    final float DAMPING = 0.87f;
    
    boolean borrar = false;
    
    public Skull() {
        calaveraTexture = new Texture(Gdx.files.internal( "skull.png"));
        this.setSize(1, height / width);
    }
    
    public Rectangle rectangulo() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
    
    public void act(float delta) {
        time = time + delta;

        yVelocity = yVelocity + GRAVITY;

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x + xChange, y, false) == false) {
            xVelocity = xChange = 0;
        }

        if (canMoveTo(x, y + yChange, yVelocity > 0) == false) {
            canJump = yVelocity < 0;
            yVelocity = yChange = 0;
        }

        this.setPosition(x + xChange, y + yChange);

        xVelocity = xVelocity * DAMPING;
        if (Math.abs(xVelocity) < 0.5f) {
            xVelocity = 0;
        }
    }
    
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(calaveraTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        float endX = startX + this.getWidth();
        float endY = startY + this.getHeight();

        int x = (int) startX;
        while (x < endX) {

            int y = (int) startY;
            while (y < endY) {
                if (layer.getCell(x, y) != null) {
                    if (shouldDestroy) {
                        layer.setCell(x, y, null);
                        golpear.play();
                        
                    }
                    return false;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return true;
    }
}
