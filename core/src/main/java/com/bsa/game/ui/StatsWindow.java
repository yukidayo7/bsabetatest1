package com.bsa.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.bsa.game.PlayerStats;

public class StatsWindow extends Window {

    private final PlayerStats stats;
    private final Label xpLabel;
    private final Label honorLabel;
    private final Label creditsLabel;
    private final Label uridiumLabel;
    private final Label levelLabel;

    private final Table contentTable;
    private final TextButton toggleButton;

    private boolean minimized = false;

    public StatsWindow(Skin skin, PlayerStats stats) {
        super("Estadísticas", skin);
        this.stats = stats;

        // ---- Configuración visual general ----
        setMovable(true);
        setResizable(false);
        setColor(Color.LIGHT_GRAY);
        getTitleLabel().setAlignment(Align.center);

        // ---- Botón minimizar ----
        toggleButton = new TextButton("-", skin);
        getTitleTable().add(toggleButton).size(22, 22).padRight(3);

        toggleButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toggleMinimize();
                return true;
            }
        });

        // ---- Tabla de contenido ----
        contentTable = new Table(skin);
        contentTable.defaults().align(Align.left).pad(2f);

        levelLabel = new Label("", skin);
        xpLabel = new Label("", skin);
        honorLabel = new Label("", skin);
        creditsLabel = new Label("", skin);
        uridiumLabel = new Label("", skin);

        contentTable.add(levelLabel).left().row();
        contentTable.add(xpLabel).left().row();
        contentTable.add(honorLabel).left().row();
        contentTable.add(creditsLabel).left().row();
        contentTable.add(uridiumLabel).left().row();

        add(contentTable).expand().fill().row();

        // ---- Posición y tamaño inicial ----
        pack();
        setSize(160, 140);
        setPosition(30, 50);
    }

    /** Alterna el modo minimizado / expandido */
    private void toggleMinimize() {
        minimized = !minimized;
        contentTable.setVisible(!minimized);
        setHeight(minimized ? 30 : 140);
        toggleButton.setText(minimized ? "+" : "-");
        pack();
    }

    /** Actualiza los valores mostrados */
    public void updateStats() {
        levelLabel.setText("Nivel: " + stats.getLevel());
        xpLabel.setText("XP: " + stats.getExperience());
        honorLabel.setText("Honor: " + stats.getHonor());
        creditsLabel.setText("Créditos: " + stats.getCredits());
        uridiumLabel.setText("Uridium: " + stats.getUridium());
    }
}
