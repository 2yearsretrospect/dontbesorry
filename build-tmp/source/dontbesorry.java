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
String inputString = "";
public void setup()
{
	
	PFont mono;
	mono = loadFont("Consolas-20.vlw");
	textFont(mono);
}
public void draw()
{
	background(0);
	fill(130);
	rect(0, 0, width, 40);
	fill(255);
	textSize(20);
	text("DON'T BE SORRY", 350, 25);
	text(inputString, 15, height-15);
	stroke(255);
	line(0, height-40, width, height-40);
	int j = displayedMessages.size() - 1;
	for (int i = height-60; i > 60; i -= 30)
	{
		if (displayedMessages.size() == 0 || j == -1)
			break;
		System.out.println("i is " + i);
		System.out.println("j is " + j);
		fill(255);
		text(displayedMessages.get(j), 15, i);
		j--;
	}
}
public void keyPressed()
{
	if (key == DELETE || key == BACKSPACE)
	{
		inputString = inputString.substring(0, inputString.length()-1);
	}
	else if (key == ENTER || key == RETURN)
	{
		displayedMessages.add(inputString);
		inputString = "";
	}
	else
		inputString += key;
}
  public void settings() { 	size(800, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dontbesorry" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
