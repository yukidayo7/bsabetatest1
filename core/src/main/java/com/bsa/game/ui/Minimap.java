package com.bsa.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.bsa.game.Player;
import com.bsa.game.Npc;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import java.util.List;

/** Mini mapa con control de navegación (clic para moverse) **/
public class Minimap extends Actor {

    private final Player player;
    private final List<Npc> npcs;
    private final float worldWidth, worldHeight;
    private final ShapeRenderer shapeRenderer;

    private final float aspectRatio;

    public Minimap(Player player, List<Npc> npcs, float worldWidth, float worldHeight) {
        this.player = player;
        this.npcs = npcs;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.shapeRenderer = new ShapeRenderer();

        this.aspectRatio = worldWidth / worldHeight;

        // 🔹 Tamaño duplicado (antes 160f)
        float baseHeight = 320f;
        float baseWidth = baseHeight * aspectRatio;

        setSize(baseWidth, baseHeight);

        // 🔹 Posición esquina inferior derecha
        float margin = 10f;
        setPosition(Gdx.graphics.getWidth() - getWidth() - margin, margin);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == com.badlogic.gdx.Input.Buttons.LEFT) {

                    // x e y son coordenadas locales dentro del minimapa
                    float localX = x;
                    float localY = y;

                    // ✅ Corregimos el eje Y (ahora sí como en el mundo real)
                    float worldX = (localX / getWidth()) * worldWidth;
                    float worldY = (localY / getHeight()) * worldHeight;

                    player.setTarget(worldX, worldY);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        shapeRenderer.setProjectionMatrix(getStage().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float x = getX();
        float y = getY();
        float w = getWidth();
        float h = getHeight();

        // Fondo del minimapa
        shapeRenderer.setColor(new Color(0f, 0.05f, 0.1f, 0.75f));
        shapeRenderer.rect(x, y, w, h);

        // Borde
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(x, y, x + w, y, 2);
        shapeRenderer.rectLine(x, y, x, y + h, 2);
        shapeRenderer.rectLine(x + w, y, x + w, y + h, 2);
        shapeRenderer.rectLine(x, y + h, x + w, y + h, 2);

        // Posición del jugador (punto + cruz)
        float px = x + (player.getPos().x / worldWidth) * w;
        float py = y + (player.getPos().y / worldHeight) * h;

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(px, py, 4);
        shapeRenderer.rectLine(px - 8, py, px + 8, py, 1.4f);
        shapeRenderer.rectLine(px, py - 8, px, py + 8, 1.4f);

        // NPCs vivos
        for (Npc npc : npcs) {
            if (npc.isDead()) continue;
            float nx = x + (npc.getPos().x / worldWidth) * w;
            float ny = y + (npc.getPos().y / worldHeight) * h;
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(nx, ny, 3.2f);
        }

        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public void act(float delta) {
        // Mantiene el minimapa anclado abajo a la derecha
        float margin = 10f;
        setPosition(Gdx.graphics.getWidth() - getWidth() - margin, margin);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
