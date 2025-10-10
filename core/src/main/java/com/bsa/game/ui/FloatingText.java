package com.bsa.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Texto flotante temporal, usado para mostrar recompensas o mensajes breves.
 */
public class FloatingText {

    private final Label label;

    public FloatingText(Stage stage, Skin skin, String message, Color color) {
        label = new Label(message, skin);
        label.setColor(color);
        label.setFontScale(1.1f);
        label.pack();

        // Posición: centro superior de la pantalla
        float x = (Gdx.graphics.getWidth() - label.getWidth()) / 2f;
        float y = Gdx.graphics.getHeight() - 100f;
        label.setPosition(x, y);

        // Animación: sube y desvanece
        label.addAction(Actions.sequence(
            Actions.parallel(
                Actions.moveBy(0, 60, 2.5f),
                Actions.fadeOut(2.5f)
            ),
            Actions.removeActor()
        ));

        stage.addActor(label);
    }

    public Actor getActor() {
        return label;
    }
}
