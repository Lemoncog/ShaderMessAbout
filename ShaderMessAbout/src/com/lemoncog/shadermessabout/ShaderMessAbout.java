package com.lemoncog.shadermessabout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class ShaderMessAbout implements ApplicationListener
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;

	public static final int FBO_SIZE = 1024;
	
	@Override
	public void create()
	{
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		camera.setToOrtho(false);
		batch = new FluctuatingSpriteBatch();

		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Gdx.input.setInputProcessor(new InputAdapter()
		{
			@Override
			public boolean keyUp(int keycode)
			{
				switch (keycode)
				{
				case Keys.R:
					loadShaderProgram();
					return true;
				}

				return false;
			}
		});
	}
	
	private ShaderProgram mSecondShaderProgram;

	protected void loadShaderProgram()
	{
		ShaderProgram testShader = new ShaderProgram(Gdx.files.internal("data/shaders/redbox.vert"), Gdx.files.internal("data/shaders/redbox.frag"));

		if (testShader.isCompiled())
		{
			Gdx.app.log(ShaderMessAbout.class.getSimpleName(),
					"++++++++++++++++++++++++++++++++++SHADER SUCCESS++++++++++++++++++++++++++++++++++++++++++++++=");
			mSecondShaderProgram = testShader;
		} else
		{
			Gdx.app.log(ShaderMessAbout.class.getSimpleName(),
					"==================================SHADER FAILE===============================================");
			Gdx.app.log(ShaderMessAbout.class.getSimpleName(), testShader.getLog());
			Gdx.app.log(ShaderMessAbout.class.getSimpleName(),
					"=============================================================================================");
		}
	}

	@Override
	public void dispose()
	{
		batch.dispose();
		texture.dispose();
	}

	private FrameBuffer mFrameBuffer;
	private TextureRegion mFrameTextureRegion;
	//private SpriteBatch mOverlayBatch;

	private Vector2 mousePos = new Vector2();

	void resizeBatch(int width, int height) {
		camera.setToOrtho(false, width, height);
		batch.setProjectionMatrix(camera.combined);
	}
	
	void renderEntities(SpriteBatch batch) {
		batch.draw(texture, 0, 0);
		//batch.draw(tex2, tex.getWidth()+5, 30);
	}
	
	@Override
	public void render()
	{
		if (mSecondShaderProgram != null)
		{
			float blurX = Gdx.input.getX()/ (float)Gdx.graphics.getWidth();
		//	mSecondShaderProgram.setUniformf("u_fluctuate", blurX * 2);
		}

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		if (mFrameBuffer == null)
		{
			mFrameBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);
			mFrameTextureRegion = new TextureRegion(mFrameBuffer.getColorBufferTexture());
			mFrameTextureRegion.flip(false, true);
		}

		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setShader(null);
		resizeBatch(FBO_SIZE, FBO_SIZE);

		mFrameBuffer.begin();

		batch.begin();

		renderEntities(batch);

		batch.flush();

		mFrameBuffer.end();

		
		if(mSecondShaderProgram != null)
		{
			batch.setShader(mSecondShaderProgram);
		}
		
		resizeBatch(FBO_SIZE, FBO_SIZE);
		
		batch.draw(mFrameTextureRegion, 0, 0);

		batch.end();
	}

	@Override
	public void resize(int width, int height)
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}
}
