This is the beta (almost done) version of your games prac :)
I've released this so that you can get a feel for what you are going to be working with. You will only *have* to code with this in about 4 weeks time but I would suggest familiarizing yourself with the code before that time.

There are Java-Docs of the API in the doc/ directory (it's mostly done though I still have to complete one or two things). I suggest you read the documentation for the Game class as it is the backbone of the game.

The documentation for JOGL is available on Vula but this is mainly for interests sake.

All source files are in the src/ directory (there are 3 files Runner.java, AsteroidGame.java Objects.java)

One more thing, I understand that the code might be a bit intimidating at first. I'm only releasing this so that you can mess around with it and experiment. The final version that will be used for your prac will be similar to this and I'll try to keep it backwards compatible for those of you who want to jump right in and start changing things drastically

=============================================================================
Whats new
=============================================================================
Font rendering (see the GameDrawer class)
Input interface
    This includes not only the Keyboard input but also mouse input such as clicks, movements and the mousewheel.
    You have to add the GameInputInterface parameter to your logicStep method (see the new AsteroidGame source for an example, or email me for help - it should be a minor change to your code and it's all documented)

=============================================================================
How to play
=============================================================================

This is just a toy game, there is no winning conditions, if you crash into something you die. If you shoot an asteroid it'll break up into smaller ones.
If you shoot the smaller ones they will be destroyed. You have to restart the game to restart. You are obviously required to change it (see the specifications on Vula)

NOTE: Sometimes the game creates you on top of an asteroid. In this case you will simply be destroyed instantly. Restart that game.

Controls:

UP - move forward
LEFT - turn anti-clockwise
RIGHT - turn clockwise
SPACE - fire weapon

W,A,S,D this just to demonstrate how the entire world can be shifted (see the renderStep() function in AsteroidGame class)


=============================================================================
How to run
=============================================================================

Please you have the correct version of the API depending on your Operating System. The only difference is the Eclipse .classpath file and the "jogl-1.1.1-linux-i586/" and "jogl-1.1.1-windows-i586/" folders that contain the Operating System specific libraries

------------------------------------------------------------------------------
Eclipse:
------------------------------------------------------------------------------

These are instructions have only been tested on Linux in the Senior Labs (which has Eclipse 3.2.2) for the linux version and on my XP system at home. I'll try my best to resolve any issues on the forums :)

Extract these file to the desired directory
Open Eclipse (if it asks to set a workspace just click OK)
If you are not so already, go to workbench (the arrow icon on the left)
Click File->New->Project...
Click Java Project (should be the first option) and click Next
Select "Create project from existing source" then click browse
Find the directory that you extracted the files to and click OK
Type in the name "AsteroidGame" for your project name. Nameing it anything else will cause the project not to work (since the classpath variables will be wrong).
    You can rename the project later by selecting the project and pressing F2 (this will adjust the classpaths accordingly)

If this is your first time using eclipse the incorrect java JRE compliance might be set and there might be an error with some of the newer Java features. Like the generic classes (like Vector) here is how to fix it.

Right click your project.
Select Properties.
Select Java Compiler on the left
Select the tick-box labeled "Enable project specific options"
To the left of Compiler Compliance level there is a drop-down box
Select 5.0 or higher
Click OK (and yes if it prompts you)

From here on it should be working and you can run it as per normal for Eclipse Java applications

Again, post on the forums if you have a problem. Please be sure to specify exactly what is wrong and provide any relevant information that I might need.

------------------------------------------------------------------------------
Command Line (Linux  only):
------------------------------------------------------------------------------
Extract these file to the desired directory,
cd into the directory
in the main directory type "make" to compile
type "./run.sh" to run

I can only guarantee this works in the Senior Labs. Though, I will try my best to resolve any issues you might have on the forums.



