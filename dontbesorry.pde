ArrayList <String> displayedMessages = new ArrayList <String>();
ArrayList <int[]> displayedMessageColor = new ArrayList <int[]>();
String inputString = "";
JSONObject currentCommandSet;
JSONArray quest1;

public void addMessage(String msg, int r, int g, int b) {
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
	size(800, 500);
	PFont mono;
	mono = loadFont("Consolas-20.vlw");
	textFont(mono);
	quest1 = loadJSONArray("quest1.json");
	currentCommandSet = quest1.getJSONObject(0);
	addMessage(quest1.getJSONObject(0).getString("initialMessage"), 255, 255, 150);
	/*Cmd[] initialCommands_quest1 = {
		new Cmd(
			"walk in",
			"You enter the elder's home. He asks you for a reminder of your name."),
		new Cmd(
			"walk away",
			"You walk away and return to your father's farm where you work the land for the rest of your life, and even have a couple of kids. You die at the age of 34 from tuberculosis with many regrets.")
	};
	quest1 = new Quest(
		"Welcome to Don't Be Sorry! You are a member of the plebian masses of Landia, your hometown. Yesterday you turned 16, the age of coming in your rather primal society, and chose the vocation of Adventurer. You are currently standing outside your respective elder's abode, contemplating entrance and seriously doubting yesterday's decision.",
		initialCommands_quest1
		);*/
	//currentCommands = quest1.initialCommands;
	
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
	/*if (displayedMessages.size() != 0 && displayedMessages.get(displayedMessages.size()-1).length() >= 71)
	{
		String left_fragment = "";
		String right_fragment = displayedMessages.get(displayedMessages.size()-1);
		displayedMessages.remove(displayedMessages.size()-1);
		while(right_fragment.length() >= 71)
		{
			int x = 70;
			while (right_fragment.charAt(x) != ' ')
			{
				x--;
				if (x == -1) {
					x = 70;
					break;
				}
			}
			left_fragment = right_fragment.substring(0, x);
			right_fragment = right_fragment.substring(x+1);
			displayedMessages.add(left_fragment);
		}
		displayedMessages.add(right_fragment);
	}*/
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
		if (inputString == "")
		{
			return;
		}
		addMessage(inputString, 255);
		boolean isCommand = false;
		for (int c = 0; c < currentCommandSet.getJSONArray("commands").size(); c++)
		{
			JSONObject command = currentCommandSet.getJSONArray("commands").getJSONObject(c);
			if (inputString.equals(command.getString("command")))
			{
				addMessage(command.getString("result"), 255, 255, 150);
				if (command.getInt("next") != -1)
				{
					currentCommandSet = quest1.getJSONObject(command.getInt("next"));
				}
				isCommand = true;
				//currentCommands = command.next.toArray(new Cmd[0]);
				break;
			}
		}
		if (isCommand == false)
		{
			addMessage("You can't do that.", 255, 255, 150);
		}

		inputString = "";
	}
	else if (inputString.length() <= 70)
	{
		inputString += key;
	}
}
/*class Quest
{
	public int progress;
	public String initialMessage;
    public Cmd[] initialCommands;
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
}*/
