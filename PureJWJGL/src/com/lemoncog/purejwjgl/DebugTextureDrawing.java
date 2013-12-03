package com.lemoncog.purejwjgl;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

public class DebugTextureDrawing extends JWGLBase
{

	public DebugTextureDrawing()
	{
		this(DebugTextureDrawing.class.getSimpleName(), GL_NEAREST, GL_CLAMP_TO_EDGE);
	}

	public int target = GL_TEXTURE_2D;
	public int id;
	public final int width;
	public final int height;

	public static final int LINEAR = GL_LINEAR;
	public static final int NEAREST = GL_NEAREST;

	public static final int CLAMP = GL_CLAMP;
	public static final int CLAMP_TO_EDGE = GL_CLAMP_TO_EDGE;
	public static final int REPEAT = GL_REPEAT;

	private int mActiveFilter;
	private int mActiveWrap;
	private ByteBuffer mByteBuffer;

	public DebugTextureDrawing(String title, int filter, int wrap)
	{
		super(title);
		// set up image dimensions
		width = WIDTH;
		height = HEIGHT;

		// we are using RGBA, i.e. 4 components or "bytes per pixel"
		final int bpp = 4;

		// create a new byte buffer which will hold our pixel data
		mByteBuffer = BufferUtils.createByteBuffer(bpp * width * height);
		mByteBuffer.put(dummyImage());

		// flip the buffer into "read mode" for OpenGL
		mByteBuffer.flip();

		mActiveFilter = filter;
		mActiveWrap = wrap;
	}

	@Override
	protected void create()
	{
		super.create();

		// enable textures and generate an ID
		glEnable(target);
		id = glGenTextures();

		// bind texture
		bind();

		// setup unpack mode
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		// setup parameters
		glTexParameteri(target, GL_TEXTURE_MIN_FILTER, mActiveFilter);
		glTexParameteri(target, GL_TEXTURE_MAG_FILTER, mActiveFilter);
		glTexParameteri(target, GL_TEXTURE_WRAP_S, mActiveWrap);
		glTexParameteri(target, GL_TEXTURE_WRAP_T, mActiveWrap);

		// pass RGBA data to OpenGL
		glTexImage2D(target, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, mByteBuffer);
	}

	@Override
	protected void render()
	{
		//setup our texture coordinates
		//(u,v) is another common way of writing (s,t)
		float u = 0f;
		float v = 0f;
		float u2 = 1f;
		float v2 = 1f;

		float x = -10;//-(getWidth()/2);
		float y = -10;//(getHeight()/2);
		
		//immediate mode is deprecated -- we are only using it for quick debugging
		glColor4f(1f, 1f, 1f, 1f);
		glBegin(GL_QUADS);
			glTexCoord2f(u, v);
			glVertex2f(x, y);
			glTexCoord2f(u, v2);
			glVertex2f(x, y + height);
			glTexCoord2f(u2, v2);
			glVertex2f(x + width, y + height);
			glTexCoord2f(u2, v);
			glVertex2f(x + width, y);
		glEnd();
	}

	public static int WIDTH = 200;
	public static int HEIGHT = 200;
	public static Random rand = new Random();

	public static byte[] dummyImage()
	{
		byte[] imageArray = new byte[WIDTH * HEIGHT * 4];

		for (int i = 0; i < imageArray.length; i++)
		{
			imageArray[i] = (byte) rand.nextInt(255);
		}

		return imageArray;
	}

	public void bind()
	{
		glBindTexture(target, id);
	}
}
