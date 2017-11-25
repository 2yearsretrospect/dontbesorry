import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class dontbesorry extends PApplet {

ArrayList <String> displayedMessages = new ArrayList <String>();
String inputString;
public void setup()
{
	
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
  public void settings() { 	size(700, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dontbesorry" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
