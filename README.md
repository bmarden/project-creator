# project-creator
Asks user for a project name then creates a directory with that name and 3 sub directories, src, header, and bin. 

Creates a main driver file with a user specified name. You can optionally add additional source files. Each new source file will create a .cpp and .h file that go in the src and header directories respectively. The .h files will be included in their associated source files and also in the main driver.

Requires groovy to be installed on your system to run. Tested on Linux and MacOS. Run the script with the following command: 

```bash
$ groovy prj_create.groovy 
```
