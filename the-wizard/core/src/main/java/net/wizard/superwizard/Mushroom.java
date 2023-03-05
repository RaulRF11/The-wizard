package net.wizard.superwizard;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Mushroom extends Image {
    TextureRegion stand;

    float time = 0;
    float xVelocity = 0;
    float yVelocity = 0;
    boolean canJump = false;
    boolean fireOverlaps = false;
    TiledMapTileLayer layer;

    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;

    public Mushroom() {
        final float width = 85;
        final float height = 70;
        this.setSize(1, height / width);

        Texture mushroomTexture = new Texture("mushroom.png");
        TextureRegion[][] grid = TextureRegion.split(mushroomTexture, (int) width, (int) height);

        stand = grid[0][0];
    }
    
    public Rectangle rectangulo() {
        return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void act(float delta) {
        time = time + delta;
        
        if (canJump) {
            yVelocity = yVelocity + MAX_VELOCITY * 4;
        }
        canJump = false;

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
