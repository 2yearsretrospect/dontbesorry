ArrayList <String> displayedMessages = new ArrayList <String>();
String inputString;
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
public void keyPressed()
{
	if (key == DELETE || key == BACKSPACE)
		inputString = inputString.substring(0, inputString.length());
	else if (key == ENTER || key == RETURN)
		displayedMessages.add(inputString);
	else
		inputString += key;
}