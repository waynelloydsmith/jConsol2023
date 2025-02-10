package com.moneydance.modules.features.jconsole2023.streams;

import java.io.IOException;
import java.io.Writer;
import java.io.File;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.BufferedWriter;
import java.lang.Thread;

import com.moneydance.modules.features.jconsole2023.JConsole2023;

/**
 * Data written to this will be displayed into the console
 * 
 * @author Andrew
 */
public class ConsoleOutputStream extends Writer
{
	private JConsole2023	console;
	public FileWriter   	myWriter;
	public Boolean  debug = false
;

	/**
	 * @param console
	 */
	public ConsoleOutputStream(JConsole2023 console)
	{
		this.console = console;
//        FileWriter myWriter = new FileWriter("~/.moneydance/jconsole.log")
//        File fOut = new File("~/.moneydance/jconsole.log");
        try {
            String homeDir = System.getProperty("user.home");
            String path = homeDir+"/.moneydance/jConsole2023.log";
//            File file = new File(path);
//            File fOut = new File(path);  // append ??
//            System.err.println(Thread.currentThread().getStackTrace()[1]); // very cool
            if (debug){ System.err.println(Thread.currentThread().getStackTrace()[1] +" logfile path is :" + path );};  // gets called twice
//            if (debug){ System.out.println(Thread.currentThread().getStackTrace()[1] +" logfile path is :" + path );};  // stdout doesn't work ?? gets called twice

//            if(!fOut.exists()){
//               System.err.println("JConsole2023 createNewFile()."); // doesn't run and doesn't work wirh FileWriter has no append
//               fOut.createNewFile();
//            }
//            FileWriter myWriter = new FileWriter(path ,true); // works true for append
            FileWriter myWriter = new FileWriter(path ,false); // make a new file its just going to be closed later anyway .. not really needed
            this.myWriter = myWriter;
//            this.fOut = fOut;
//          if(!fOut.exists())
//          if(!myWriter.exists())
            }
        catch (Exception e)
            {
            if (debug){ System.err.println("JConsole2023 jConsole2023.log FileWrite open error occurred.");};
            e.printStackTrace();
            }
//        try {
//            FileWriter fw = new FileWriter(fOut.getAbsoluteFile());
//            }
//        catch (IOException e)
//            {
//            System.err.println("JConsole2023 jConsole2023.log BufferdWrite error occurred.");
//            e.printStackTrace();
//            }
	}

	@Override
	public synchronized void close() throws IOException
	{
		console = null;
		myWriter.close();
        if (debug){System.err.println("logfile is closeing");}; // never seen this
	}

	@Override
	public synchronized void flush() throws IOException
	{
      //if (debug){ System.err.println("logfile is flushing");}; // this shows up frequenty
      myWriter.flush();
	}

	@Override
	public synchronized void write(char[] cbuf, int off, int len) throws IOException     // off is always 0
	{
//	    String buff66 = ""; // produced a zero lenght String
		StringBuilder temp = new StringBuilder(console.getText()); // pulls all the existing text from the screen which grows forever
		for (int i = off; i < off + len; i++)
		{
			temp.append(cbuf[i]);                            // temp.length grows forever which is ok on the console (command history) but messes up the log file
		}
		String buff66 = temp.toString();
//		console.setText(buff66);
		console.setText(temp.toString()); // redraws the entire screen.. must erase whats there now
//        myWriter.write(cbuf,off,len);     // works but does not contain what I typed on the console or the reults or the system.err messages
        myWriter.close();                   // close it
        String homeDir = System.getProperty("user.home");
        String path = homeDir+"/.moneydance/jConsole2023.log";
        myWriter = new FileWriter(path ,false);   // reopen it
        myWriter.write(buff66,0,buff66.length());             // need to empty the file and rewrite the entire thing .. not append to it. its a FileWriter

//        if (debug){System.err.println("writing to the logfile:"+len+":"+off+":"+temp.length());};
//        if (debug){System.err.println("writing to the logfile:"+len+":"+off+":"+buff66.length()+":"+temp.length());};
//        myWriter.flush();
//      myWriter.write(temp.tostring());
      }
}
