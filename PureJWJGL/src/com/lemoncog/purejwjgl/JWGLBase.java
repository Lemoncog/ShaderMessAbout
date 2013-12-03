package com.lemoncog.purejwjgl;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class JWGLBase {

	// Whether to enable VSync in hardware.
	public static final boolean VSYNC = true;
	// Whether to use fullscreen mode
	public static final boolean FULLSCREEN = false;

	// Whether our game loop is running
	protected boolean running = false;

	private String mTitle;
	// Width and height of our window
	private int mWidth = 800;
	private int mHeight = 600;
	
	public JWGLBase(String title)
	{
		this(title, 800, 600);
	}

	public JWGLBase(String title, int width, int height) {
		super();
		
		mTitle = title;
		mWidth = width;
		mHeight = height;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	public int getHeight()
	{
		return mHeight;
	}

	// Start our game
	public void start() throws LWJGLException {
		// Set up our display
		Display.setTitle("Display example"); // title of our window
		Display.setResizable(true); // whether our window is resizable
		Display.setDisplayMode(new DisplayMode(mWidth, mHeight)); // resolution
																	// of
																	// our
																	// display
		Display.setVSyncEnabled(VSYNC); // whether hardware VSync is enabled
		Display.setFullscreen(FULLSCREEN); // whether fullscreen is enabled

		// create and show our display
		Display.create();

		// Create our OpenGL context and initialize any resources
		create();

		// Call this before running to set up our initial size
		resize();

		running = true;

		// While we're still running and the user hasn't closed the window...
		while (running && !Display.isCloseRequested()) {
			// If the game was resized, we need to update our projection
			if (Display.wasResized())
				resize();

			// Render the game
			render();

			// Flip the buffers and sync to 60 FPS
			Display.update();
			Display.sync(60);
		}

		// Dispose any resources and destroy our window
		dispose();
		Display.destroy();
	}

	protected void create() {
		// Simple 2D graphics don't need a depth test, draw order determines Z
		// order
		glDisable(GL_DEPTH_TEST);

		// For alpha on sprites
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glClearColor(0f, 0f, 0f, 0f);
	}

	// Called to render our game
	protected void render() {
	}

	// Called to resize our game
	protected void resize() {
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		// ... update our projection matrices here ...
	}

	// Called to destroy our game upon exiting
	protected void dispose() {
		// ... dispose of any textures, etc ...
	}
}
