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

const vec3 SEPIA = vec3(1.2, 1.0, 0.8);

void spotlight(){
	vec4 textColor = texture2D(u_texture, v_texCoords);

	float r = 0.6;
	float softness = 0.1;
	
	float mod = 1;
	float dist = distance(v_l_source, v_texCoords);
	
	mod = smoothstep(r, r-softness, dist);//1 - step(dist, 0.2);
	textColor.rgb = textColor.rgb * mod;
	
	gl_FragColor = v_color * textColor;
}

void blur(){

	vec4 textColor = texture2D(u_texture, v_texCoords);

	float radius = v_fluctuate;
	float resolution = 1240;
	vec2 dir = vec2(1,0);

	//this will be our RGBA sum
	vec4 sum = vec4(0.0);
	
	//our original texcoord for this fragment
	vec2 tc = textColor;
	
	//the amount to blur, i.e. how far off center to sample from 
	//1.0 -> blur by one pixel
	//2.0 -> blur by two pixels, etc.
	float blur = radius/resolution; 
    
	//the direction of our blur
	//(1.0, 0.0) -> x-axis blur
	//(0.0, 1.0) -> y-axis blur
	float hstep = dir.x;
	float vstep = dir.y;
    
	//apply blurring, using a 9-tap filter with predefined gaussian weights
    
	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0162162162;
	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0540540541;
	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.1216216216;
	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.1945945946;
	
	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.2270270270;
	
	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.1945945946;
	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.1216216216;
	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0540540541;
	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0162162162;

	//discard alpha for our simple demo, multiply by vertex color and return
	gl_FragColor = v_color * vec4(sum.rgb, 1.0);
}


void sepia(){
	vec4 textColor = texture2D(u_texture, v_texCoords);

	//uses NTSC conversion weights
	float gray = dot(textColor.rgb, vec3(0.299, 0.587, 0.114));
	
	vec3 sepiaColor = vec3(gray) * SEPIA;
	textColor.rgb = mix(textColor.rgb, sepiaColor, 0.75);
	
	//again we'll use mix so that the sepia effect is at 75%
	
    gl_FragColor = v_color * textColor;
}

void main() {	
	spotlight();
}