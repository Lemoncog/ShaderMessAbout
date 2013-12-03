//the position of the vertex as specified by our renderer
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
uniform mat4 u_projTrans;
uniform float u_fluctuate;
uniform vec2 u_l_source0;

varying float v_fluctuate;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_l_source;

void main() {
    //pass along the position	
	v_color = a_color;		
	v_l_source = u_l_source0;
	v_fluctuate = u_fluctuate;
	v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}