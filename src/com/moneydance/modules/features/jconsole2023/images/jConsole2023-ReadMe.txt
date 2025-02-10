This is the ReadMe.txt File for JConsole2023
First:You need to define EDITOR in the enviroment to make everything work.
Do an export EDITOR=kwrite  .. can be any editor but kwrite monitors the file for changes while you have it open.
Put it in /etc/profile.d/local.sh .. also.. you need to reboot to make local.sh run
This jConsole2023-ReadMe.txt is in the .mxt archive under /images .If your wondering where it came from.
The .mxt archive is in ~/.moneydance/fmodules/jConsole2023.mxt
I have added a log file to jConsole2023 .. It is only good for a session
I also added a command line history feature that's good for 64 commands.
It is saved in a file so it lives through reboots etc.
You can delete/edit this file with the first pick from the Menu button.
It is updated when you close JConsole2023 so open the history file with the menu button and then close JConsole2023.
When your done editing the command history save it and restart JConsole2023 the edited History will get reloaded
from ~/.moneydance/jConsole2023.save when JConsole2023 restarts.
I did this because sometimes the command line history gets messed up.
I added a Clear Screen button.
the "View Log File" Button is Jconsole2023's log file not moneydance's errlog.txt file.
I use runScipts.py to start all my moneydance scripts.
So I added a button to start it up.
I suggest you create a directory /opt/moneydance/scripts and put all your scripts in there.
I'm so used to typing ls and cd and pwd on the console >>> that I added these commands
to Jconsole2023. I mapped them to os.whatever rather than relearn these commands.
Monitoring the moneydance errlog.txt is a good idea so I added a menu item to open it.
My moneydance error log was growing into a huge file and I didn't realize it.
That's all for now.
bash-5.1$ ls -l /home/wayne/.moneydance/jConsole*
-rw-r--r-- 1 wayne users  787 Feb  8 09:40 /home/wayne/.moneydance/jConsole2023.log
-rw-r--r-- 1 wayne users  473 Feb  8 08:31 /home/wayne/.moneydance/jConsole2023.save


