Requirements:
Must have MySQL installed on computer.

How to run:
1. Navigate into the ‘src’ directory
2. Enter the compile and run commands mentioned below

To compile, use the following commands:

Windows:
javac -cp mysql.jar action\*.java gui\*.java model\*.java paths\*.java web\*.java Main.java

Mac:
javac -cp mysql.jar action/*.java gui/*.java model/*.java paths/*.java web/*.java Main.java



To run, use the following commands:

Windows:
java -cp .;mysql.jar Main

Mac:
java -cp .:mysql.jar Main



Possible Arguments:
Provide text file names for input and output.
- args[0]: input(.txt)
OR
- args[0]: input(.txt) args[1]: output(.txt)





