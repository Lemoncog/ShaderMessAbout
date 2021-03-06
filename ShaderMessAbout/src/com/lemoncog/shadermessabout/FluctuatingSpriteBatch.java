package com.lemoncog.shadermessabout;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class FluctuatingSpriteBatch extends SpriteBatch
{
	private ShaderProgram mCustomShader;

	@Override
	public void setShader(ShaderProgram shader)
	{
		super.setShader(shader);

		mCustomShader = shader;
	}

	public Vector2 MOVING_VEC = new Vector2();
	public Vector2 TARGET_VEC = new Vector2(1,0);
	public float alpha = 0;
	@Override
	public void begin()
	{
		super.begin();

		if (mCustomShader != null)
		{
			//setFlucuate();
			//setSource();
		}
	}

	private void setSource()
	{		
		alpha+=0.02f;
		
		if(alpha > 1)
		{
			alpha = 0;
			TARGET_VEC.set(rand.nextFloat(), rand.nextFloat());
		}
		
		float xDiff = TARGET_VEC.x - MOVING_VEC.x;
		float yDiff = TARGET_VEC.y - MOVING_VEC.y;
		
		MOVING_VEC.add(xDiff*0.02f, yDiff*0.02f);
		
		mCustomShader.setUniformf("u_l_source0", MOVING_VEC);
	}

	public static final Random rand = new Random();

	private void setFlucuate()
	{
		mCustomShader.setUniformf("u_fluctuate", rand.nextFloat());
	}
}
