package com.bsa.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class Impact {
    private final Vector2 pos;
    private float time = 0f;
    private final float duration = 0.35f;
    private final float maxRadius = 18f;

    public Impact(float x, float y) {
        this.pos = new Vector2(x, y);
    }

    public void update(float dt) { time += dt; }

    public boolean isAlive() { return time < duration; }

    public float getRadius() {
        return Math.min(maxRadius, (time / duration) * maxRadius);
    }

    public float getAlpha() {
        return 1f - (time / duration);
    }

    public Vector2 getPos() { return pos; }

    public static class StatsWindow extends Window {

        private final PlayerStats stats;
        private final Label xpLabel;
        private final Label honorLabel;
        private final Label creditsLabel;
        private final Label uridiumLabel;
        private final Label levelLabel;
        private boolean minimized = false;
        private final Table contentTable;
        private final TextButton toggleButton;

        public StatsWindow(Skin skin, PlayerStats stats) {
            super("PILOT STATS", skin);
            this.stats = stats;

            setMovable(true);
            setResizable(false);
            setKeepWithinStage(true);

            toggleButton = new TextButton("-", skin);
            getTitleTable().add(toggleButton).padRight(5);
            toggleButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    toggleMinimize();
                }
            });

            contentTable = new Table(skin);
            contentTable.defaults().align(Align.left).pad(3);

            levelLabel = new Label("", skin);
            xpLabel = new Label("", skin);
            honorLabel = new Label("", skin);
            creditsLabel = new Label("", skin);
            uridiumLabel = new Label("", skin);

            contentTable.add(levelLabel).row();
            contentTable.add(xpLabel).row();
            contentTable.add(honorLabel).row();
            contentTable.add(creditsLabel).row();
            contentTable.add(uridiumLabel).row();

            add(contentTable).expand().fill();
            pack();

            setPosition(15, Gdx.graphics.getHeight() - getHeight() - 15);
        }

        public void updateStats() {
            levelLabel.setText("Level: " + stats.getLevel());
            xpLabel.setText("XP: " + stats.getExperience());
            honorLabel.setText("Honor: " + stats.getHonor());
            creditsLabel.setText("Credits: " + stats.getCredits());
            uridiumLabel.setText("Uridium: " + stats.getUridium());
        }

        private void toggleMinimize() {
            minimized = !minimized;
            contentTable.setVisible(!minimized);
            toggleButton.setText(minimized ? "+" : "-");
            pack();
        }
    }
}
