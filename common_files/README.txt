I've released this so that you can get a feel for what you are going to be working with. You will only *have* to code with this in about 2 weeks time but I would suggest familiarizing yourself with the code before that time.

There are Java-Docs of the API in the doc/ directory (it's mostly done though I still have to complete one or two things, ask on the forum if a function isn't documented). I suggest (i.e. i really really really suggest) you read the documentation for the Game class as it is the backbone of the game.

The documentation for JOGL is available on Vula but this is mainly for interests sake.

All source files are in the src/ directory

=============================================================================
How to play
=============================================================================

This is just a toy game, there is no winning conditions. If you shoot something that is not a wall it'll be destroyed.
You have to restart the game to restart. You are obviously required to change this (see the specifications on Vula)

NOTE: Sometimes the game creates you on top of an asteroid. In this case you will not be able to move. Restart that game. You need to address this in your final game.

Controls:

UP, DOWN, LEFT, RIGHT - move
mouse position - turn to mouse
SPACE or left mouse button - fire weapon


=============================================================================
How to setup, compile and run
=============================================================================

You can use an IDE (Eclipse, soon to be NetBeans and JCreator as well) or you can use the command line

Please you have the correct version of the API depending on your Operating System. The only difference is the Eclipse .classpath file and the "jogl-1.1.1-linux-i586/" and "jogl-1.1.1-windows-i586/" and "jogl-1.1.1-windows-amd64/" folders that contain the Operating System specific libraries

------------------------------------------------------------------------------
Eclipse 3.5 (Galileo):
------------------------------------------------------------------------------

Extract the files to the desired directory
Open Eclipse (if it asks to set a workspace just click OK)
If you are not so already, go to workbench (the arrow icon on the left)
Click File->Import...
Select "Existing Project into Workspace" and click next
Select "Browse" next to the "Select root directory" then click browse
Find the directory that you extracted the files to and click OK
Select "Copy Projects into Workspace" if you want it to copy the files to your workspace directory (obviously...)
Click Finish and you're done

------------------------------------------------------------------------------
Eclipse (complicated instructions if the above one doesn't work):
------------------------------------------------------------------------------

Extract the files to the desired directory
Open Eclipse (if it asks to set a workspace just click OK)
If you are not so already, go to workbench (the arrow icon on the left)
Click File->New->Project...
Click Java Project (should be the first option) and click Next
Select "Create project from existing source" then click browse
Find the directory that you extracted the files to and click OK
Type in the name "SurvivalGame" for your project name. Nameing it anything else will cause the project not to work (since the classpath variables will be wrong).
    You can rename the project later by selecting the project and pressing F2 (this will adjust the classpaths accordingly)


------------------------------------------------------------------------------
Eclipse - Setting up the correct JDK
------------------------------------------------------------------------------

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
NetBeans 6.7:
------------------------------------------------------------------------------

Extract the files to the desired directory
Open NetBeans
Click File->Import Project->Eclipse Project...
Click the "Import Project ignoring Project Dependencies" radio button
For "Project to Import:" choose the directory where you extracted the files to for the 
For "Destination Folder:" choose the directory where you want your project
Click Finish
You should see the "SurvivalGame" project on the left now.
Right click on it and click "Properties"
Click on the "Run" tab on the left
In the textfield next to "VM Options" enter in ????
Click "OK" and you are ready to build and run


------------------------------------------------------------------------------
Command Line (Linux  only):
------------------------------------------------------------------------------
Extract these file to the desired directory,
cd into the directory
in the main directory type "make" to compile
type "./run.sh" to run

I can only guarantee this works in the Senior Labs. Though, I will try my best to resolve any issues you might have on the forums.



