ENV=LD_LIBRARY_PATH="`pwd`/libs/jogl-1.1.1-linux-i586/lib/" CLASSPATH="`pwd`/jar/2DGame_1.2.jar":"`pwd`/libs/jogl-1.1.1-linux-i586/lib/jogl.jar":"`pwd`/libs/jogl-1.1.1-linux-i586/lib/gluegen-rt.jar":"`pwd`/src/game/game.jar"
JAVACMD=javac

src/*.class: src/*.java
	$(ENV) $(JAVACMD) src/*.java
	
clean:
	rm src/*.class src/*~
