#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aCol;
out vec4 fCol;
void main() {
    fCol = aCol;
    gl_Position = vec4(aPos, 1.0);
}
#type fragment
#version 330 core
in vec4 fCol;
out vec4 col;
void main() {
    col = fCol;
}