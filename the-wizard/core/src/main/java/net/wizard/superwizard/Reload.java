package net.wizard.superwizard;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Reload extends Image{
   
    TextureRegion stand;
    TiledMapTileLayer layer;
    Texture recargaTexture;
    Sound golpear;
    final float width = 50;
    final float height = 50;
     
    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;

    final float GRAVITY = -2.5f;
    final float DAMPING = 0.87f;
    
    boolean borrar = false;
    
    public Reload() {
        recargaTexture = new Texture(Gdx.files.internal( "reload.png"));
        TextureRegion[][] grid = TextureRegion.split(recargaTexture, (int) width, (int) height);
        stand = grid[0][0];
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
        batch.draw(stand, this.getX(), this.getY(), this.getWidth(), this.getHeight());
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
