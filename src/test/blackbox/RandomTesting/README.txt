For Random Testing, since we are using CLI for inputs, randoop couldn't do test cases. Then we created a method for giving random input to execute the randoop properly.

In Utility class, which has inputOutput() method for receiving every input, we added another method which can create random inputs and put that method when inputs are necessary. Then we executed randoop and it worked according to our method.

***** If Random Tests have to be executed, The comments in Utility class have to be removed and try-catch method has to put in comments.*****

To try that method and execute the command below, also these steps must to be followed.

command for creating random based test cases with randoop:

(You have to fill the stars below with absolute path)

*****\smqa-placement-app\src> java -classpath "*****\smqa-placement-app\lib\hamcrest-core-1.3.jar";"******\smqa-placement-app\lib\junit-4.12.jar";"******\smqa-placement-app\bin";"C:\Users\etalh\IdeaProjects\smqa-placement-app\lib\randoop-all-4.3.3.jar" randoop.main.Main gentests --classlist="*****\smqa-placement-app\myclasses.txt" --time-limit=900 --usethreads=true --call-timeout=8000

