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
ArrayList <int[]> displayedMessageColor = new ArrayList <int[]>();
StringDict state = new StringDict();
ArrayList <String> visited = new ArrayList <String>();
String inputString = "";
JSONObject quest1;
JSONObject quest;
JSONObject location;
JSONObject currentCommand;
JSONObject Types;
JSONObject Creatures;
JSONObject Items;

public void addMessage(String msg, int r, int g, int b) {
	if (msg == null)
	{
		msg = "ERROR: Attempting to add null message.";
		r = 255;
		g = 0;
		b = 0;
	}
	while (msg.indexOf("{") != -1)
	{
		int start = msg.indexOf('{');
		int end = msg.indexOf('}');
		if (end == -1)
		{
			continue;
		}
		String varname = msg.substring(start+1, end);
		if (state.hasKey(varname))
		{
			msg = msg.substring(0, start) + state.get(varname) + msg.substring(end+1);
		}
		else
		{	
			msg = msg.substring(0, start) + "???" + msg.substring(end+1);
		}
	}
	while(msg.length() >= 71)
	{
		int x = 70;
		while (msg.charAt(x) != ' ')
		{
			x--;
			if (x == -1) {
				x = 70;
				break;
			}
		}
		displayedMessages.add(msg.substring(0,x));
		int[] rgb = {r, g, b};
		displayedMessageColor.add(rgb);
		msg = msg.substring(x+1);
	}
	displayedMessages.add(msg);
	int[] rgb = {r, g, b};
	displayedMessageColor.add(rgb);
}
//it's defined several times so we don't have to fill in rgb if we don't want
public void addMessage(String msg, int a) {
	addMessage(msg, a, a, a);
}

public void addMessage(String msg) {
	addMessage(msg, 255, 255, 255);
}

public void setup()
{
	
	PFont mono;
	mono = loadFont("Consolas-20.vlw");
	textFont(mono);
	quest1 = loadJSONObject("quest1.json");
	quest = loadJSONObject("quest1.json");
	Types = loadJSONObject("types.json");
	Creatures = Types.getJSONObject("creatures");
	Items = Types.getJSONObject("items");
	state.set("location", quest1.getJSONObject("start").getString("location"));
	location = quest.getJSONObject("locations").getJSONObject(state.get("location"));
	addMessage(location.getJSONObject("onfirstenter").getString("display"), 255, 255, 150);	
	currentCommand = location;
}
public void draw()
{
	background(0);
	fill(130);
	rect(0, 0, width-1, 40);
	fill(255);
	textSize(20);
	text("DON'T BE SORRY", width/2 -80, 25);
	text(inputString, 15, height-15);
	stroke(255);
	line(0, height-40, width, height-40);
	int j = displayedMessages.size() - 1;
	for (int i = height-60; i > 60; i -= 30)
	{
		if (displayedMessages.size() == 0 || j == -1)
			break;
		//have to make yellow if text is game-generated
		int[] colors = displayedMessageColor.get(j);
		fill(colors[0], colors[1], colors[2]);
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
		//command parser
		if (inputString == "")
		{
			return;
		}
		addMessage(inputString, 255);
		boolean isWildcard = false;
		
		if (inputString.contains("*"))
		{
			//stop the wildcard processor from activating
			addMessage("You can't do that.", 255, 255, 150);
			inputString = "";
			return;
		}
		if (inputString.startsWith("walk") || inputString.startsWith("move") || inputString.startsWith("travel"))
		{
			//handles location changing, currently only supports cardinal directions
			String plocation;
			if (inputString.endsWith("north") && !location.isNull("north"))
			{
				location = quest.getJSONObject("locations").getJSONObject(location.getString("north"));
				plocation = location.getString("north");
			}
			else if (inputString.endsWith("south") && !location.isNull("south"))
			{
				location = quest.getJSONObject("locations").getJSONObject(location.getString("south"));
				plocation = location.getString("south");

			}
			else if (inputString.endsWith("east") && !location.isNull("east"))
			{
				location = quest.getJSONObject("locations").getJSONObject(location.getString("east"));
				plocation = location.getString("east");
			}
			else if (inputString.endsWith("west") && !location.isNull("west"))
			{
				location = quest.getJSONObject("locations").getJSONObject(location.getString("west"));
				plocation = location.getString("west");
			}
			else if (inputString.contains(" into ")) {
				//chop the location from the end of the string
				plocation = inputString.substring(inputString.indexOf(" into ") + " into ".length()).toLowerCase();
				if (!location.isNull("in") && location.getJSONArray("in").toString().contains(plocation) && !quest.getJSONObject("locations").isNull(plocation))
				{
					//the player wants to walk into a valid location
					location = quest.getJSONObject("locations").getJSONObject(plocation);
				}
				else 
				{
					addMessage("You can't go there.", 255, 255, 150);
					inputString = "";
					return;
				}
			}
			else
			{
				addMessage("You can't go there.", 255, 255, 150);
				inputString = "";
				return;
			}
			state.set("location", plocation);
			if (!visited.contains(plocation) && !location.isNull("onfirstenter"))
			{
				currentCommand = location.getJSONObject("onfirstenter");
				visited.add(plocation);
			}
			else if (!location.isNull("onenter"))
			{
				currentCommand = location.getJSONObject("onenter");
			}
			else
			{
				addMessage("Mysteriously, this location has no entry message.", 255, 0, 0);
				inputString = "";
				return;
			}

		}
		// possible commands will be stored in one of the following, and will be searched in the following order:
		// 1: current location
		// 2: current command set
		// 3: current items
		// 4: current command set wildcards
		else if (!location.isNull("options") && !location.getJSONObject("options").isNull(inputString))
		{
			// the player chose a valid non-wildcard location option
			currentCommand = location.getJSONObject("options").getJSONObject(inputString);
		}
		else if (!currentCommand.isNull("options") && !currentCommand.getJSONObject("options").isNull(inputString))
		{
			// the player chose a valid non-wc option out of the current command set
			currentCommand = currentCommand.getJSONObject("options").getJSONObject(inputString);
		}
		else if (false)
		{
			// leave me alone i'll implement items later i just wanna build the bloody app
		}
		else if (!currentCommand.isNull("options") && !currentCommand.getJSONObject("options").isNull("*"))
		{
			// there's a wildcard option in the current command set options
			currentCommand = currentCommand.getJSONObject("options").getJSONObject("*");
			isWildcard = true;
		}
		else
		{
			addMessage("You can't do that.", 255, 255, 150);
			inputString = "";
			return;
		}
		// if we've gotten this far we have a command to execute
		addMessage(currentCommand.getString("display"), 255, 255, 150);
		if (!currentCommand.isNull("statechange"))
		{
			// time to run some statechanges
			for (int c = 0; c < currentCommand.getJSONArray("statechange").size(); c++)
			{
				// each change has at least three strings named "left", "op", and "right"
				// which essentially is directly executed as (left op right)
				// eg {"left": "thirst", "op": "=", "right": "0"} executes (thirst = 0)
				JSONObject change = currentCommand.getJSONArray("statechange").getJSONObject(c);
				if (change.isNull("left") || change.isNull("op") || change.isNull("right"))
				{
					addMessage("ERROR: Malformed statechange operation.", 255, 0, 0);
					continue;
				}
				/*if (change.isNull("if"))
				// screw it adding conditionals later
				{
					ArrayList<boolean> results = new ArrayList<boolean>();
					for (JSONObject cond : change.getJSONArray("if"))
					{

					}
				}
				*/
				if (isWildcard && change.getString("op") == "=")
				{
					// currently wildcards are only supported on the right side
					state.set(change.getString("left"), inputString);
				}
				else if (change.getString("op") == "=")
				{
					state.set(change.getString("left"), change.getString("right"));
				}
				//else if (change.getString("op") == "+")
				// more operators TBI but I WANT TO LAUNCH THE APP
			}
		}
		inputString = "";
	}
	else if (inputString.length() <= 70)
	{
		inputString += key;
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
