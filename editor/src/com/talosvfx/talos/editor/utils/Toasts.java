package com.talosvfx.talos.editor.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.talosvfx.talos.editor.project2.SharedResources;
import com.talosvfx.talos.editor.widgets.ui.common.ColorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Toasts {

	private static final Logger logger = LoggerFactory.getLogger(Toasts.class);

	public static float LENGTH_LONG = 3f;
	public static float LENGTH_SHORT = 1f;

	private static Toasts instance;
	private static VerticalGroup toastTarget;

	private static class Toast extends Table {

		private final VisLabel messageLabel;

		Toast (String message, Color color) {
			messageLabel = new VisLabel(message, color);
			add(messageLabel);

			setTransform(true);
		}

	}

	public static Toasts getInstance () {
		if (instance == null) {
			instance = new Toasts();
			toastTarget = new VerticalGroup();
			SharedResources.stage.addActor(toastTarget);

		}
		return instance;
	}

	private Toasts () {
	}

	public void showInfoToast (String message) {
		Toast toast = new Toast(message, ColorLibrary.BLUE);
		toastTarget.addActor(toast);
		toastTarget.setFillParent(true);
		toastTarget.setPosition(0, 0);
		toastTarget.toFront();

		toast.setScale(0, 0);
		toast.setOrigin(toast.getWidth()/2f, toast.getHeight()/2f);


		toast.addAction(Actions.sequence(
			Actions.scaleTo(1, 1, 0.1f, Interpolation.swingOut),
			Actions.delay(LENGTH_LONG),
			Actions.removeActor()
		));
	}

}
