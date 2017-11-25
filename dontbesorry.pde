public void setup()
{
	size(700, 500);
	PFont mono;
	mono = loadFont("Consolas-20.vlw");
	textFont(mono);
}
public void draw()
{
	background(0);
	textSize(20);
	text("this is text", 20, 200);
}
