............................................................... Feb 9 2025
....its been two years since I've built an extension
... the java code lives under
bash-5.1$ pwd
/mnt/DATA/home/wayne/source/moneydance/moneydance-devkit-5.1/src/com/moneydance/modules/features/jconsole2023
bash-5.1$ ls -l
-rw-r--r-- 1 wayne users  9859 Mar  3  2023 CommandHistory.java
-rw-r--r-- 1 wayne users 15715 Feb  9 06:34 JConsole2023.java
-rw-r--r-- 1 wayne users  6103 Feb 25  2023 Main-Feb-25-2023.java.not
-rw-r--r-- 1 wayne users 31645 Mar 11  2023 Main.java
drwxr-xr-x 2 wayne users  4096 Feb  9 06:44 images
-rw-r--r-- 1 wayne users   383 Feb 14  2023 meta_info.dict
drwxr-xr-x 2 wayne users  4096 Feb 22  2023 streams
... I edited images/jConsole2023-ReadMe.txt ... its becomes visible under the "Menu" button on jConsole2023
....now check what java version moneydance is useing today
bash-5.1$ /opt/moneydance/jre/bin/java --version
openjdk 21.0.2 2024-01-16 LTS
OpenJDK Runtime Environment Temurin-21.0.2+13 (build 21.0.2+13-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.2+13 (build 21.0.2+13-LTS, mixed mode)
.... check my active version
bash-5.1$ java --version
openjdk 17.0.6 2023-01-17
OpenJDK Runtime Environment Temurin-17.0.6+10 (build 17.0.6+10)
OpenJDK 64-Bit Server VM Temurin-17.0.6+10 (build 17.0.6+10, mixed mode, sharing)

bash-5.1$ env | grep JAVA
JAVA_HOME=/home/wayne/.sdkman/candidates/java/current
..... oh yes sdkman is in charge .. love it
bash-5.1$ ls /home/wayne/.sdkman/candidates/java
1.8_201-oracle	11.0.17-zulu  11.0.17.LT-zulu  17.0.6-tem  18.9-oracle	8.66.0.15-zulu	current
so Temurin-21.0.2 is not available which is what moneydance 2024.2 (5172) is running
sdk list java
.... hold the down arrow key down after you do... sdk list java
.... will try building with the current active version
bash-5.1$ pwd
/mnt/DATA/home/wayne/source/moneydance/moneydance-devkit-5.1/src
bash-5.1$ whoami
wayne
bash-5.1$ ant -version
Apache Ant(TM) version 1.10.13 compiled on January 4 2023
bash-5.1$ ls -l
-rw-r--r-- 1 wayne users  8586 Mar  4  2023 build.xml
-rw-r--r-- 1 wayne users  3907 Feb 10  2023 priv_key
-rw-r--r-- 1 wayne users  1349 Feb 10  2023 pub_key
..... I'm assuming the above files are up to date
.....if not you have to do ant genkeys and/or edit the build.xml file
.....I find that editing the build.xml file is more work than writing the code
.....first problem .. some of the code uses jConsole2023 and some uses jconsole2023 .. I should really fix this but ...
bash-5.1$ ant jconsole2023
... this worked .. I had to enter the pass phrase "buster the dog"
... you could
cp -i /mnt/DATA/home/wayne/source/moneydance/moneydance-devkit-5.1/dist/* /home/wayne/.moneydance/fmodules/
... but don't do this .. use moneydance extension manager->Add From File
... the extension runs ok
... now to post the source code for jConsole2023 on github
... I have several other extensions I've found or created over the years but I don't use them
.... I use jConsole2023 every day
... Why? because I don't like the Moneydance Developer Console. Its to much like a net nanny.
... Also I'm very comfortable working from the command line.(my god its been over 40 years now)
)
.........................................................................Posted to LQ.org on Feb 9 2025 Making Moneydance Extensions










...................................................older stuff
fix the env (see below)  .. use sdk now
pass phrase is "buster the dog"
in the src directory run
"ant genkeys" to make new keys
"ant myextension" to build the sample myextension
copy /src/build.xml from devkit4
ant jconsole270
ant jython270
look in dist directory for results
copy the .mxt file to
file:///home/wayne/.moneydance/fmodules
to run it


fix the env for the JAVA_HOME and the PATH (can do chmod and reboot too .. see java-Install-feb-9-2023.txt)
switch to jdk11 from jdk8 without rebooting
from cat /etc/profile.d/zulu-openjdk11.sh
export JAVA_HOME=/usr/lib64/zulu-openjdk11   ... did this was jdk8
export MANPATH="${MANPATH}:${JAVA_HOME}/man"  ... not done
export PATH="${PATH}:${JAVA_HOME}/bin"        ... not done
export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${JAVA_HOME}/lib/server" ... not done

PATH=/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/lib64/libexec/kf5:/usr/lib64/libreoffice/program:~/.local/bin:/usr/lib64/qt5/bin:/usr/lib64/zulu-openjdk8/bin:/usr/lib64/zulu-openjdk8/jre/bin .. what the PATH was with jdk8
export PATH="/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/lib64/libexec/kf5:/usr/lib64/libreoffice/program:~/.local/bin:/usr/lib64/qt5/bin:${JAVA_HOME}/bin"   .. did this


you have to play with
file:///mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/build.xml
too change what gets built
used the one from the 4.0 devkit

was unable to install any of my built extensions even the sample one.maybe the java version???
try changing LD_LIBRARY_PATH
bash-5.1$ pwd
/etc/profile.d
ash-5.1$ grep LD_LIB *.sh
zulu-openjdk11.sh:export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${JAVA_HOME}/lib/server"
bash-5.1$ env | grep LD_LIBRARY_PATH   .. not defined
export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${JAVA_HOME}/lib/server"  .. did this
ant jconsole270
ant jython270
ant myextension
cp -i /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/dist/* /home/wayne/.moneydance/fmodules/ .. did three overwrites
money dance still won't load them
will try switching to the moneydance java version
export JAVA_HOME=/opt/moneydance/jre
export PATH="/usr/local/bin:/usr/bin:/bin:/usr/games:/usr/lib64/libexec/kf5:/usr/lib64/libreoffice/program:~/.local/bin:/usr/lib64/qt5/bin:${JAVA_HOME}/bin"
export LD_LIBRARY_PATH="${JAVA_HOME}/lib/"
bash-5.1$ env | grep HOME
JAVA_HOME=/opt/moneydance/jre
ANT_HOME=/usr/share/ant
HOME=/home/wayne
.. now the ant build fails .... Unable to find a javac compiler;
bash-5.1$ java --version
openjdk 18.0.1 2022-04-19
OpenJDK Runtime Environment Temurin-18.0.1+10 (build 18.0.1+10)
OpenJDK 64-Bit Server VM Temurin-18.0.1+10 (build 18.0.1+10, mixed mode)
will try switching to jdk11 and rebooting...
bash-5.1# chmod -x /etc/profile.d/zulu-openjdk8.sh
bash-5.1# chmod -x /etc/profile.d/zulu-openjdk8.csh
bash-5.1# chmod +x /etc/profile.d/zulu-openjdk11.csh
bash-5.1# chmod +x /etc/profile.d/zulu-openjdk11.sh
cp -i /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/dist/* /home/wayne/.moneydance/fmodules/  ....... don't do this .. use moneydance extension manager
didn't work ?????????????????????????
one error was java.util.zip END header not found from jconsole270.mxt  .. like the file is corrupted
found some stuff in the 5.1 build file .. switched from 4.0 build file to it.
did ant myextention .. still got error on load java.util.zip END header not found
checked a new devkit 5.1 I just downloaded .. feb 9 2023 .. it has some 2019 stuff in in it like src/build.xml and the sample ???
will install it as devkit5.1 and rename the existing one to devkit5.1.old.
these build.xml files are a pain in the ass
tried to load myextension .. it called it accountlist then failed java.lang.NullPointerException
Cannot invoke java.io.FileInputStream.close() because "in" is NullPointerException
its like the class path is wrong or the java version is wrong.
I hate java .. what a waste of time
.......from "unable to load extension"
https://infinitekind.tenderapp.com/discussions/moneydance-development/2303-unable-to-load-extension-file
Clicking Manage Extensions > Add from File, copies the extension from it's current location into the .moneydance/fmodules folder.
Manually placing it there and then using 'Add from File' is likely the cause of the first odd behavior.
also from sean
OK, in that case, in the JDK directory where java lives can you check for the existence of a "conf" folder?
If it exists (it should, in java 11) can you remove the conf/security/policy/limited directory?
That should remove any encryption limitations which I suspect is the issue.
.................................................
switched to jdk11
rebooted
install myextension from both moneydance-dev5.1 and from moneydance-5.1old
they both worked
that says that the export stuff I did worked to change the java version will I was on 5.1old
tried jython270 and jconsole270 from 5.1-old
jconsole270 crashed during install
jython270 crashes when I try to run it
moral of the story is don't copy mxt files into ~/.moneydancefmodules directory
you need to let moneydance extension manager do the copying......
now I will try to build the two consoles with the latest moneydance-devkit-5.1
copied and renamed the build file to build-myextension.xml
did a find / replace on the build.xml file replaced myextension with jython270
did ant jython270
got ten errors
bash-5.1$ pwd
/mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src
redid the build.xml .. added a new <target> section for jython270
did ant jython270 .. got 10 errors
ant myextension got 14 errors ????
removed the target jython270 section
ant myextension got 14 errors ????
got a copy of build.xml from the tar.gz archive
ant myextension got 14 errors ????
first error looks like this
init:
myextension:
    [javac] This version of java does not support the classic compiler; upgrading to modern
    [javac] Compiling 3 source files to /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/build
    [javac] This version of java does not support the classic compiler; upgrading to modern
    [javac] /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/com/moneydance/modules/features/myextension/jpython/Main.java:14:
            error: package org.python.util does not exist

ant -version and java --version looks ok
bash-5.1$ javac --version .. javac 11.0.17
bash-5.1$ jython --version .. Jython 2.7.3
bash-5.1$ python --version .. Python 2.7.18
bash-5.1$ ant -version .. Apache Ant(TM) version 1.10.12 compiled on October 13 2021
bash-5.1$ java --version ... openjdk 11.0.17 2022-10-18 LTS
I found this org.python.util in /opt/moneydance/lib/mdpython.jar ... along with 50 other jars
I wonder if I can just copy it to ... didn't work
added <pathelement path="../lib/mdpython.jar"/> to the build.xml file
now I get different errors so the above worked
new error is
cannot find symbol [javac]   private RootAccount root = null;
error: package com.moneydance.apps.md.model does not exist
this is not in the moneydance-dev.jar ????????????????
but it is in /opt/moneydance/lib/moneydance.jar ???????????????
so what is going on here
the myextension src on the new devkit does not have a jpython directory but the one I'm using does ????????????
going to delete it
this fixed the build no errors
............................................looks like a drag and drop error from  copying 4.0 stuff to 5.1
but I learned how to borrow jars from the active moneydance to dev5
will try to run it in moneydance
had to rebuild it but it installed and runs ... got the zip error when installing
will leave the mdpython jar in the dev lib for now .. moneydance could change it in a future release
now back to putting jconsole270 in the build.xml
ant jconsole270 got one error
/mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/com/moneydance/modules/features/jconsole270/JConsole270.java:395: error: cannot find symbol [javac] pythonThread.destroy();
ant jconsole same error
cannot find symbol [javac] pythonThread.destroy();
ant jython270 same error
    cannot find symbol [javac] pythonThread.destroy();
got it to build and install .. the error just magicly disappeared
but run time erro File",string,line1 in module AttributeError class definitions has no attribute
now I get import error no module named ... ..   I fixed the icon file location I think
tried again but get same erro "import error no module named"
delete some import stuff now I got a crash..Null pointer somthing about java.util.zip
had rebuild three times to get it loaded ???? then crashed
java.util.zip.ZipFile.getEntry(String)" because "this.jarFile" is null
the extension manager seems to be screwed up
used sdk insatll java  .. seemed to help with the erorrs on install extensions in moneydance
skd use java 17.0.6-tem
did ant jython270 .. no errors
run time ImportError: No module named console
AttributeError: class definitions has no attribute 'ClassPathNames'
runs now finally
had to put stuff back in definitions.py file and had to restart moneydance
the stuff in definitions.py ClassPathNames should be moved the jython270 Main
WARNING: A terminally deprecated method in java.lang.System has been called
WARNING: System::setSecurityManager has been called by org.apache.tools.ant.types.Permissions (file:/usr/share/ant/lib/ant.jar)
WARNING: Please consider reporting this to the maintainers of org.apache.tools.ant.types.Permissions
WARNING: System::setSecurityManager will be removed in a future release
......................................................................... maybe try an older jdk or ant
now to try jconsole270
ant jconsole270
cannot find symbol [javac] 			pythonThread.destroy();
this error came back ..
bash-5.1$ export LD_LIBRARY_PATH="" was set to :/usr/lib64/zulu-openjdk11/lib/server
didn't help
removed     <pathelement path="../lib/mdpython.jar"/> from build.xml
now I get 12 errors
starting with package org.python.core does not exist  .. why do I need python ????????????
its in jConsole270.java ?? ..............use's the python org.python.util.InteractiveInterpreter
put import org.python.core.*; back in jConsole270.java
and mdpython.jar back into build.xml
error pythonThread.destroy(); came back
cannot find it in mdpython.jar  .. but there is a jconsole in it already
    [javac] /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/com/moneydance/modules/features/jconsole270/JConsole270.java:398: error: cannot find symbol
    [javac] 			pythonThread.destroy();
    [javac] 			            ^
    [javac]   symbol:   method destroy()
    [javac]   location: variable pythonThread of type Thread
line 446 says
//			pythonThread.destroy();
I commented it out ??????????????????????????????????? wonder where pythonThread is imported from
build ok now 1 warning
Note: /mnt/DATA/home/wayne/source/moneydance-devkit-5.1/src/com/moneydance/modules/features/jconsole270/JConsole270.java uses or overrides a deprecated API.
ran ok in moneydance ... will try to get ride of definitions dependany now
don't forget to restart moneydance to test the new version
removed definitions from jconsole270
removed definitions from jython270
now to edit definitions.py
done
now too play with sdk







.................................................
DESCRIPTION

This is a toolkit that will assist in the creation of extensions
for the Moneydance personal finance application.  Extensions can
be compiled, packaged, and signed using the ANT build tool.  ANT
is open source, and is increasingly used to replace Makefiles 
when building Java projects.  ANT can be downloaded for many
platforms from http://ant.apache.org/

The Moneydance Developers Kit includes an ANT build.xml, the necessary
jar files to compile and build an extension, and working sample source
code for a new extension.  API documentation for Moneydance can be
found in the "Developer" section of http://infinitekind.com/moneydance

USAGE

Before building your extension, you will first need to generate a
key pair.  This can be done by running "ant genkeys" from the "src"
directory.  You will be prompted for a passphrase that is used to
encrypt the private key file.  Your new keys will be stored in the
priv_key and pub_key files.

Once your keys have been generated, you are ready to compile an 
extension.  The build.xml file has been set up to compile and
build the sample extension with ID "myextension".  The source 
code for this sample extension can be found under:
  src/com/moneydance/modules/features/myextension/

To compile and package the sample extension, run "ant myextension"
from the src directory.  After the extension is compiled and built,
you will be asked for the passphrase to your private key which will
be used to sign the extension and place the new extension file in
the dist directory with the name myextension.mxt.  Please feel free 
to modify the source to the "myextension" extension to build your own 
extensions.

If you would like to share your extension with others and would prefer
they not see the unrecognized-signature warning when loading the extension
then you can send your source code to support@moneydance.com where we
will inspect the source code for security problems, compile the
extension, and sign it with the official moneydance key.  If you like,
we can then also put the extension into the list of available extensions
for all Moneydance users to see.


ADVANCED USAGE

To create your own extension that is separate from the sample 
extension you must first come up with a unique ID for your extension.  
An extension ID is all lower case and alphanumeric.  For this example, 
let's say your new extension ID is "newextension".  You would take the 
following steps to set up the development environment for the new extension:

1) Copy the files from 
     src/com/moneydance/modules/features/myextension/
   to
     src/com/moneydance/modules/features/newextension/
2) Edit the new java source files to change the package names from
     com.moneydance.modules.features.myextension
   to
     com.moneydance.modules.features.newextension
   When loading an extension, looks for the class named:
   com.moneydance.modules.features.{extensionid}.Main
3) Add a "newextension" target to the build.xml file.  This can be
   done easily by duplicating the "myextension" target in the
   build.xml file, and changing every instance of "myextension" to
   "newextension" in the new target.
4) At this point you can run "ant newextension" in the src directory
   and your new extension will be built and placed in the dist
   directory.


FURTHER ASSISTANCE

If you would like further assisstance, please contact support@moneydance.com
We will be more than happy to answer any questions.

