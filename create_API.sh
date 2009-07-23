# cleaning up dev folder
rm -rf $1"_linux/"/
rm -rf $1"_windows/"/

cd common_files/
make all
cd ..

echo Copying to $1"_linux"/
mkdir $1"_linux"
cp -r common_files/* $1"_linux"/
rm -r $1"_linux"/Makefile
cp -r linux_files/* $1"_linux"/
rm -r $1"_linux"/src/2DGame/
cp -r games_src/$1/* $1"_linux"/src/

python create_project_files.py linux $1 $1"_linux"/

echo Copying to $1"_windows"
mkdir $1"_windows"
cp -R common_files/* $1"_windows"/
rm -r $1"_windows"/Makefile
cp -R windows_files/* $1"_windows"/
rm -r $1"_windows"/src/2DGame/
cp -r games_src/$1/* $1"_windows"/src/

python create_project_files.py windows $1 $1"_windows"/

#echo Cleaning up $1"_linux"/
#mv $1"_linux"/Makefile_non_dev $1"_linux"/Makefile
#rm -rf $1"_linux"/src/2DGame/

#echo Cleaning up $1"_windows"
#rm -f  $1"_windows"/Makefile
#rm -f  $1"_windows"/Makefile_non_dev
#rm -f  $1"_windows"/run.sh
#rm -rf $1"_windows"/src/game/*.java
#rm -rf $1"_windows"/src/Answer.java
#rm -rf $1"_windows"/.classpath_linux
#mv $1"_windows"/.classpath_windows $1"_windows"/.classpath
#rm -rf $1"_windows"/TODO.txt

