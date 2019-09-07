// Ben Marden
// CSCI 315 Programming Languages
// Spring 2019

//  Project solution - Groovy script that creates a directory structure for a 
// c++ project. 


// Variables to hold user input
def prj_name  
def main_file  
def path

// Instance of System.in to gather user input
def console = System.in.newReader()

// Welcome message
println "Hello, this script will create a directory structure for a c++ project\n"
// Get the Project name 
println 'Please enter the project name:'
prj_name = console.readLine()
// Get the path to desired Project location
println 'Please enter the path where the project should be located:'
path = console.readLine()

// Error check the path entered
File dir = new File(path)
boolean isDirectory = dir.isDirectory()

// Loop until a valid path is entered
while (!(isDirectory)) {
  // Output some error message and grab path again
  println("Please enter a valid path. Example: /path/dir")
  path = console.readLine()
  dir = new File(path)
  isDirectory = dir.isDirectory()
}
// Notify user of path entered
println "The path used will be: $path\n"

// Get the main driver file name
println 'Please enter a name for the main driver file:'
main_file = console.readLine()
println "The driver file name used will be: $main_file" + ".cpp\n"
main_file = "$main_file" + ".cpp"

// Check if user wants to add additional source files
println 'Would you like to add additional source file? (Y/n)'
def response = console.readLine()
def srcList = []

// While they want to add source files we store each file name in a list
while (response == 'Y' || response == 'y') {
  def file_name 
  println 'Enter a filename: '
  file_name = console.readLine()
  srcList += file_name 
  println 'Another? (Y/n)'
  response = console.readLine()
}

// New AntBuilder object to assist us with creating the directory structure 
// and files
def ant = new AntBuilder()

// Variables to hold paths to each new directory 
def prjDir = "$path" + "/" + "$prj_name"
def srcDir = "$prjDir" + "/" + "src"
def binDir = "$prjDir" + "/" + "bin"
def hDir = "$prjDir" + "/" + "header"

// Block of ant will create file structure
ant.sequential {
    // Echo out some information to the user and make each directory 
    // defined above
    echo("Creating Project $prj_name")
    mkdir(dir: prjDir)
    mkdir(dir: srcDir)
    mkdir(dir: binDir)
    mkdir(dir: hDir)
    echo("Project structure created")
}

// Now we need to check if any source files were entered
if (srcList.isEmpty()) {
  println "No additional source files to add\n"
} else {
  // Loop through each string in the user specified source/header files and create them
  for (item in srcList) {
    // Define names used for file creation
    def srcFile = item + ".cpp"
    def headerFile = item + ".h"
    // Used as the class declaration in .h file. Capitilize first character of item
    def className = item.capitalize()
    ant.sequential {
      // Notify user of files being added
      echo("Adding source file $srcFile and header file $headerFile")
      // Files to write to, use Gstring so we can insert variable names in the
      // code creation
      // Header file creation
      echo(file:"$hDir" + "/" + "$headerFile", "#ifndef ${item.toUpperCase()}_H\n" +
      "#define ${item.toUpperCase()}_H\n\n" + "#include <iostream>\n\n" + 
      "class $className {\n" + "  public:\n    $className();\n  private:\n};\n#endif")

      // Source file creation
      echo(file:"$srcDir" + "/" + "$srcFile", "#include <iostream>\nusing namespace std;\n\n" +
      "#include \"$headerFile\"\n")
    }
  }
}
  // Now create the main driver file
  ant.sequential{
    echo(file:"$srcDir" +"/" + "$main_file", 
'''#include <iostream>
using namespace std;
\n''')
  // Add in includes for header files if needed
  if (!srcList.isEmpty()) {
    for (item in srcList) {
      def headerFile = item + ".h"
      echo(file:"$srcDir" + "/" + "$main_file", 
      "#include\"$headerFile\"\n", append:true)
    }
  }
  // Finish creating main driver
  echo(file:"$srcDir" + "/" + "$main_file",'''
int main() {
  cout << "Hello World" << endl;
  return 0;
}''', append:true) 
  echo("$main_file created")
  }





    
    