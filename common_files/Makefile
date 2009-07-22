LIBCLASSFILES="`pwd`/libs/jogl-1.1.1-linux-i586/lib/jogl.jar":"`pwd`/libs/jogl-1.1.1-linux-i586/lib/gluegen-rt.jar"
ENV=LD_LIBRARY_PATH="`pwd`/libs/jogl-1.1.1-linux-i586/lib/" CLASSPATH="`pwd`/jar/2DGame_1.2.jar":$(LIBCLASSFILES)
JAVACMD=javac

JAR_FILE=2DGame_1.2.jar

jar:
	$(ENV) $(JAVACMD) src/2DGame/*.java
	cd src && jar cf $(JAR_FILE) 2DGame/*.class && mv $(JAR_FILE) ../jar && cd .. || cd ..
	#$(ENV) $(JAVACMD) src/*.java
	
docs:
	javadoc -d docs src/2DGame/*.java -classpath $(LIBCLASSFILES)
	
#src/*.class: src/*.java
#	$(ENV) $(JAVACMD) src/*.java
	
clean_all:
	rm -f ./*~ src/game/*.class src/game/*~ src/*.class src/*~
	
clean_docs:
	rm -rf docs/
	
clean:
	rm src/*.class src/*~
