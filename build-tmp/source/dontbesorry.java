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

//import java.util.Map;
ArrayList <String> displayedMessages = new ArrayList <String>();
String inputString = "";
StringDict[] quest1;
public void setup()
{
	
	PFont mono;
	mono = loadFont("Consolas-20.vlw");
	textFont(mono);
	Cmd[] initialCommands_quest1 = {
		new Cmd(
			"walk in",
			"You enter the elder's home. He asks you for a reminder of your name."),
		new Cmd(
			"walk away",
			"You walk away and return to your father's farm where you work the land for the rest of your life, and even have a couple of kids. You die at the age of 34 from tuberculosis with many regrets.")
	};
	Quest quest1 = new Quest(
		"Welcome to Don't Be Sorry! You are a member of the plebian masses of Landia, your hometown. Yesterday you turned 16, the age of coming in your rather primal society, and chose the vocation of Adventurer. You are currently standing outside your respective elder's abode, contemplating entrance and seriously doubting yesterday's decision.",
		initialCommands_quest1
		);
	displayedMessages.add(quest1.initialMessage);
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
	if (displayedMessages.size() != 0 && displayedMessages.get(displayedMessages.size()-1).length() >= 71)
	{
		String left_fragment = "";
		String right_fragment = displayedMessages.get(displayedMessages.size()-1);
		displayedMessages.remove(displayedMessages.size()-1);
		while(right_fragment.length() >= 71)
		{
			left_fragment = right_fragment.substring(0, 70);
			right_fragment = right_fragment.substring(70);
			displayedMessages.add(left_fragment);
		}
		displayedMessages.add(right_fragment);
	}
	int j = displayedMessages.size() - 1;
	for (int i = height-60; i > 60; i -= 30)
	{
		if (displayedMessages.size() == 0 || j == -1)
			break;
		fill(255);
		text(displayedMessages.get(j), 15, i);
		j--;
	}
}
public void keyPressed()
{
	if ((key == DELETE || key == BACKSPACE) && inputString.length() != 0)
	{
		inputString = inputString.substring(0, inputString.length()-1);
	}
	else if (key == ENTER || key == RETURN)
	{
		displayedMessages.add(inputString);
		inputString = "";
	}
	else if (inputString.length() <= 70)
	{
		inputString += key;
	}
}
class Quest
{
	protected int progress;
	protected String initialMessage;
    protected Cmd[] initialCommands;
	public Quest(String initmsg, Cmd[] cmdlist)
	{
		progress = 0;
		initialMessage = initmsg;
		initialCommands = cmdlist;
	}
    public int getProgress()
    {
    	return progress;
    }
    public int incProgress()
    {
    	progress++;
    	return progress;
    }           
}

class Cmd
{
	String command;
	String result;
	ArrayList<Cmd> next;
	public Cmd(String cmd, String rslt)
	{
		command = cmd;
		result = rslt;
	}
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
