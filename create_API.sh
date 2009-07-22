# cleaning up dev folder
rm -rf AsteroidGame_linux/
cd AsteroidGame_dev/
make with_src
make clean_all
make clean_docs
make docs
rm -rf bin/
cd ..
# copying linux version

echo Copying to AsteroidGame_linux
cp -r AsteroidGame_dev/ AsteroidGame_linux

echo Copying to AsteroidGame_windows
cp -r AsteroidGame_dev/ AsteroidGame_windows

echo Cleaning up AsteroidGame_linux
rm -rf AsteroidGame_linux/jogl-1.1.1-windows-i586/
rm -rf AsteroidGame_linux/Makefile
mv AsteroidGame_linux/Makefile_non_dev AsteroidGame_linux/Makefile
rm -rf AsteroidGame_linux/src/game/*.java
rm -rf AsteroidGame_linux/src/Answer.java
rm -rf AsteroidGame_linux/.classpath_windows
mv AsteroidGame_linux/.classpath_linux AsteroidGame_linux/.classpath
rm -rf AsteroidGame_linux/TODO.txt

echo Cleaning up AsteroidGame_windows
rm -rf AsteroidGame_windows/jogl-1.1.1-linux-i586/
rm -f  AsteroidGame_windows/Makefile
rm -f  AsteroidGame_windows/Makefile_non_dev
rm -f  AsteroidGame_windows/run.sh
rm -rf AsteroidGame_windows/src/game/*.java
rm -rf AsteroidGame_windows/src/Answer.java
rm -rf AsteroidGame_windows/.classpath_linux
mv AsteroidGame_windows/.classpath_windows AsteroidGame_windows/.classpath
rm -rf AsteroidGame_windows/TODO.txt
