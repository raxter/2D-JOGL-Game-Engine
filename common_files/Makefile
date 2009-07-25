LIBCLASSFILES="`pwd`/../OS_specific_files/linux-i586/libs/jogl-1.1.1-linux-i586/lib/jogl.jar":"`pwd`/../OS_specific_files/linux-i586/libs/jogl-1.1.1-linux-i586/lib/gluegen-rt.jar"
ENV=LD_LIBRARY_PATH="`pwd`/../OS_specific_files/linux-i586/libs/jogl-1.1.1-linux-i586/lib/" CLASSPATH="`pwd`/jar/2DGame_1.2.jar":$(LIBCLASSFILES)
JAVACMD=javac

JAR_FILE=2DGame_1.2.jar

all: jar docs

jar: bytecode
	cd src/2DGame/ && jar cf $(JAR_FILE) GameEngine/*.class && cd ../.. || cd ../..
	mkdir jar ; mv src/2DGame/$(JAR_FILE) jar/$(JAR_FILE)
	#$(ENV) $(JAVACMD) src/*.java
	
docs:
	javadoc -d docs src/2DGame/GameEngine/*.java -classpath $(LIBCLASSFILES)
	
bytecode:
	$(ENV) $(JAVACMD) src/2DGame/GameEngine/*.java
  
	
#src/*.class: src/*.java
#	$(ENV) $(JAVACMD) src/*.java
	
clean_all: clean_docs clean_jar clean_bytecode
	rm *~
	rm */*~
	rm */*/*~
	
clean_jar:
	rm -rf jar/
	
clean_bytecode:
	rm src/*/*.class src/*/*~
	
clean_docs:
	rm -rf docs/
