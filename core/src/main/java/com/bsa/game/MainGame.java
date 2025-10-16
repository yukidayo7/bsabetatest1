package com.bsa.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bsa.game.ui.StatsWindow;
import com.bsa.game.ui.Minimap;

import java.util.ArrayList;
import java.util.Iterator;

public class MainGame extends ApplicationAdapter {
    public static final int WORLD_WIDTH = 10000;
    public static final int WORLD_HEIGHT = 6000;
    private static final int VIRTUAL_WIDTH = 1280;
    private static final int VIRTUAL_HEIGHT = 720;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Player player;
    private Texture backgroundTexture;

    private ArrayList<Npc> npcs;
    private Npc selectedNpc;

    private boolean isMousePressed = false;
    private boolean isAttacking = false;

    // timers & rates
    private float laserTimer = 0f;
    private float damageTimer = 0f;
    private float laserRate = 0.25f;
    private float damageRate = 1.0f;
    private float attackRange = 700f;
    private int damagePerTick = 100;

    private ArrayList<Projectile> projectiles;
    private ArrayList<Impact> impacts;

    // UI
    private Stage uiStage;
    private Skin skin;
    private StatsWindow statsWindow;
    private PlayerStats playerStats;

    @Override
    public void create() {
        // --- CARGA DE CONTENIDO (naves, npcs, mapas) ---
        try {
            com.bsa.game.data.GameContentLoader.loadAll();
            Gdx.app.log("MainGame", "Contenido cargado correctamente.");
        } catch (Exception e) {
            Gdx.app.error("MainGame", "Error cargando contenido data-driven", e);
        }
        // --- FIN BLOQUE DE CARGA ---
        // Cámara y viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();

        // Jugador y entorno
        player = new Player(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        backgroundTexture = new Texture(Gdx.files.internal("default_space_bg.png"));

        projectiles = new ArrayList<>();
        impacts = new ArrayList<>();
        npcs = new ArrayList<>();

        // NPCs de ejemplo
        npcs.add(new Npc(5000, 3200, "Streuner"));
        npcs.add(new Npc(5200, 2900, "Lordakia"));
        npcs.add(new Npc(5400, 3100, "Sibelon"));

        // Estadísticas del jugador
        playerStats = new PlayerStats();

        // Scene2D UI
        uiStage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        statsWindow = new StatsWindow(skin, playerStats);
        uiStage.addActor(statsWindow);

        // Agregar el minimapa
        uiStage.addActor(new Minimap(player, npcs, WORLD_WIDTH, WORLD_HEIGHT));

        // InputMultiplexer
        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(uiStage);
        mux.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
                    isMousePressed = true;
                    handleMouseInput(screenX, screenY);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == com.badlogic.gdx.Input.Buttons.LEFT) {
                    isMousePressed = false;
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(mux);
    }

    /** Manejo de clics: si clicas un NPC -> sólo selecciona. Si no -> mueve. **/
    private void handleMouseInput(int screenX, int screenY) {
        Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
        boolean npcClicked = false;

        for (Npc npc : npcs) {
            if (!npc.isDead() && npc.isClicked(worldCoords.x, worldCoords.y)) {
                selectedNpc = npc;
                npcClicked = true;
                break;
            }
        }

        if (!npcClicked) {
            player.setTarget(worldCoords.x, worldCoords.y);
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        uiStage.act(delta);

        // Movimiento dinámico con click sostenido
        if (isMousePressed) {
            Vector3 worldCoords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            boolean overNpc = false;
            for (Npc npc : npcs) {
                if (!npc.isDead() && npc.isClicked(worldCoords.x, worldCoords.y)) {
                    overNpc = true;
                    break;
                }
            }
            if (!overNpc) player.setTarget(worldCoords.x, worldCoords.y);
        }

        // Control de ataque (Ctrl)
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            isAttacking = !isAttacking;
            if (!isAttacking) player.clearRotationLock();
        }

        // Rotación y ataque
        if (isAttacking && selectedNpc != null && !selectedNpc.isDead()) {
            float dx = selectedNpc.getPos().x - player.getPos().x;
            float dy = selectedNpc.getPos().y - player.getPos().y;
            float angleDeg = new com.badlogic.gdx.math.Vector2(dx, dy).angleDeg() - 90f;
            player.setRotation(angleDeg);

            float dist = player.getPos().dst(selectedNpc.getPos());

            if (dist <= attackRange) {
                laserTimer += delta;
                while (laserTimer >= laserRate) {
                    projectiles.add(new Projectile(player.getPos(), selectedNpc.getPos(), Color.RED));
                    laserTimer -= laserRate;
                }

                damageTimer += delta;
                if (damageTimer >= damageRate) {
                    selectedNpc.takeDamage(damagePerTick);
                    damageTimer = 0f;
                    impacts.add(new Impact(selectedNpc.getPos().x, selectedNpc.getPos().y));

                    if (selectedNpc.isDead()) {
                        playerStats.addExperience(selectedNpc.getRewardXP());
                        playerStats.addCredits(selectedNpc.getRewardCredits());
                        playerStats.addUridium(selectedNpc.getRewardUridium());
                        playerStats.addHonor(selectedNpc.getRewardHonor());

                        // Mostrar notificación flotante
                        new com.bsa.game.ui.FloatingText(
                            uiStage,
                            skin,
                            "+ " + selectedNpc.getRewardXP() + " XP   + " +
                                selectedNpc.getRewardCredits() + " C   + " +
                                selectedNpc.getRewardUridium() + " U   + " +
                                selectedNpc.getRewardHonor() + " H",
                            Color.GOLD
                        );

                        isAttacking = false;
                        selectedNpc = null;
                        player.clearRotationLock();
                    }
                }
            } else {
                laserTimer = 0f;
                damageTimer = 0f;
            }
        }

        // Actualización lógica
        player.update(delta);
        for (Npc n : npcs) n.update(delta);

        // Proyectiles
        Iterator<Projectile> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Projectile p = pIt.next();
            p.update(delta);
            boolean removed = false;
            for (Npc npc : npcs) {
                if (npc.isDead()) continue;
                float hitRadius = npc.getSize() * 0.6f;
                if (p.getPos().dst(npc.getPos()) <= hitRadius + p.getRadius()) {
                    impacts.add(new Impact(npc.getPos().x, npc.getPos().y));
                    removed = true;
                    break;
                }
            }
            if (!p.isAlive() || removed) pIt.remove();
        }

        // Impactos
        Iterator<Impact> iIt = impacts.iterator();
        while (iIt.hasNext()) {
            Impact im = iIt.next();
            im.update(delta);
            if (!im.isAlive()) iIt.remove();
        }

        camera.position.set(player.getPos().x, player.getPos().y, 0);
        camera.update();

        // --- Dibujado del mundo ---
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

        // NPCs
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Npc npc : npcs) {
            if (!npc.isDead()) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(npc.getPos().x, npc.getPos().y, npc.getSize() / 2f);
            }
        }
        shapeRenderer.end();

        // Proyectiles
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Projectile proj : projectiles) {
            shapeRenderer.setColor(proj.getColor());
            shapeRenderer.circle(proj.getPos().x, proj.getPos().y, proj.getRadius());
        }
        shapeRenderer.end();

        // Impactos
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Impact im : impacts) {
            shapeRenderer.setColor(1f, 0.9f, 0.4f, im.getAlpha());
            shapeRenderer.circle(im.getPos().x, im.getPos().y, im.getRadius());
        }
        shapeRenderer.end();

        // Selección
        if (selectedNpc != null && !selectedNpc.isDead()) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(selectedNpc.getPos().x, selectedNpc.getPos().y, selectedNpc.getSize());
            shapeRenderer.end();
        }

        // Nave
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.identity();
        shapeRenderer.translate(player.getPos().x, player.getPos().y, 0);
        shapeRenderer.rotate(0, 0, 1, player.getRotation());
        shapeRenderer.triangle(0, 15, -8, -8, 8, -8);
        shapeRenderer.identity();
        shapeRenderer.end();

        // HUD: nombres + coordenadas
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Npc npc : npcs) {
            if (!npc.isDead()) {
                font.draw(batch, npc.getName() + " (" + (int) npc.getHealth() + " HP)",
                    npc.getPos().x - 30, npc.getPos().y - npc.getSize() - 10);
            }
        }
        font.draw(batch,
            "Player: " + (int) player.getPos().x + ", " + (int) player.getPos().y,
            camera.position.x - VIRTUAL_WIDTH / 2f + 10,
            camera.position.y + VIRTUAL_HEIGHT / 2f - 30);
        batch.end();

        // --- UI Stage ---
        statsWindow.updateStats();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        backgroundTexture.dispose();
        uiStage.dispose();
        skin.dispose();
    }
}
