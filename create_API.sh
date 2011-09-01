# cleaning up dev folder

# create_API GameName OS

mkdir output
rm -rf output/$1"_"$2/
mkdir output/$1"_"$2/
#rm -rf $1"_linux/"/
#rm -rf $1"_windows/"/

cd common_files/
make all
cd ..

echo Copying to output/$1"_"$2/
#mkdir output/$1"_"$2
cp -r common_files/* output/$1"_"$2/
rm -r output/$1"_"$2/Makefile
cp -r "OS_specific_files/"$2/* output/$1"_"$2/
rm -r output/$1"_"$2/src/2DGame/
cp -r games_src/$1/* output/$1"_"$2/src/

#echo python create_project_files.py "OS_specific_files"$2 $1 output/$1"_"$2/
python create_project_files.py $2 $1 output/$1"_"$2/

#echo Copying to output/$1"_windows"
#mkdir output/$1"_windows"
#cp -R common_files/* output/$1"_windows"/
#rm -r output/$1"_windows"/Makefile
#cp -R windows_files/* output/$1"_windows"/
#rm -r output/$1"_windows"/src/2DGame/
#cp -r games_src/$1/* output/$1"_windows"/src/

#python create_project_files.py windows $1 output/$1"_windows"/

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

