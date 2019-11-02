#version 120

uniform sampler2D DiffuseSampler;

uniform vec2 Doom;

varying vec2 texCoord;

void main() {
	vec4 color = texture2D(DiffuseSampler, texCoord);
	float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
	vec3 rgb = mix(color.rgb, vec3(gray, gray, gray), Doom.x);

	float base = Doom.y * 0.2;
	rgb = vec3(base, base, base) + rgb * (1.0 - Doom.y * 0.5);

    gl_FragColor = vec4(rgb, color.a);
}
