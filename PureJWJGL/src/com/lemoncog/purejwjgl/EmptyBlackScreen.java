package com.lemoncog.purejwjgl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;


public class EmptyBlackScreen extends JWGLBase{	
	public EmptyBlackScreen() {
		super("Empty Black Screen", 800, 600);
	}

	// Exit our game loop and close the window
	public void exit() {
		running = false;
	}

	// Called to render our game
	protected void render() {
		// Clear the screen
		glClear(GL_COLOR_BUFFER_BIT);

		// ... render our game here ...
	}
}
