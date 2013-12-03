#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_l_source;
varying vec2 v_texCoords;
varying float v_fluctuate;
uniform sampler2D u_texture;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    //pass along the color red	
	vec4 textColor = texture2D(u_texture, v_texCoords);
	
	float dist = distance(v_l_source, v_texCoords);
	dist = sqrt(dist);
	
	//v_fluctuate = v_fluctuate * v_texCoords.x;
	textColor.r = textColor.r * dist;
	textColor.g = textColor.g * dist;
	textColor.b = textColor.b * dist;
	
    gl_FragColor = v_color * textColor;
}