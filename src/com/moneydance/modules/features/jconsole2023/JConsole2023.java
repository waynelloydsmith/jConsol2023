package com.moneydance.modules.features.jconsole2023;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.lang.InterruptedException;

import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


import com.moneydance.modules.features.jconsole2023.streams.ConsoleInputStream;
import com.moneydance.modules.features.jconsole2023.streams.ConsoleOutputStream;

import com.moneydance.apps.md.controller.FeatureModule;
import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.apps.md.controller.ModuleUtil;
import com.moneydance.apps.md.controller.UserPreferences;

//import com.infinitekind.moneydance.model.*;

//import org.python.core.*;
import org.python.core.PyException;
import org.python.util.InteractiveInterpreter;
import org.python.core.PySystemState;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
//import org.python.google.common.base.internal.Finalizer;
//import org.python.util.InteractiveConsole;
import java.lang.ProcessBuilder;
import java.lang.ProcessBuilder.Redirect;

import java.io.FileInputStream;
import java.util.Properties;


/**
 * @author Andrew Somebody modifed by waynelloydsmith
 */
public class JConsole2023 extends JTextArea implements KeyListener
{
	public static final Boolean  debug = false ;

	private static final long	serialVersionUID	= -5866169869353990380L;
	/**
	 * The input stream that will pass data to the script engine
	 */
	public final Reader		in;
	/**
	 * The output stream from the script engine
	 */
	public final Writer		out;
	/**
	 * The error stream from the script engine
	 */
	public final Writer		err;
	public static CommandHistory		history;
	/**
	 * index of where we can start editing text
	 */
	int				editStart;
	/**
	 * True when a script is running
	 */
	boolean				running;
	/**
	 * The script engine and scope we're using
	 */
	public static InteractiveInterpreter  engine;
	/**
	 * The allowed variables and stuff to use
	 */
	// private Bindings bindings;
	// ScriptContext context;
	private ConsoleFilter		filter;
	private Thread			pythonThread;

	/**
	 * 
	 */
	  
	public JConsole2023(FeatureModuleContext context)
	{
	    // import os;
		// create streams that will link with this
		in = new ConsoleInputStream(this);
		// System.setIn(in);
		out = new ConsoleOutputStream(this);
		// System.setOut(new PrintStream(out));
		err = new ConsoleOutputStream(this);
		// setup the command history
		history = new CommandHistory();
		JConsole2023.history = history;
		history.loadHistory();
// see		http://web.mit.edu/jython/jythonRelease_2_2alpha1/Doc/javadoc/org/python/util/PythonInterpreter.html#exec(org.python.core.PyObject)
		// setup the script engine
		PySystemState.initialize(); // instead of engine.initialize ??
        setTabSize(4); //  these all effect the console
		setLineWrap(true); // new feb 22 2023 this worked
		setWrapStyleWord(true); // new feb 22 2023 this worked
		engine = new InteractiveInterpreter();
    //		self.engine = engine; // ?? says  don't need it this time
		engine.setIn(in);
		engine.setOut(out);
		engine.setErr(err);
		engine.exec("import sys");
        engine.exec ("import os");
//		engine.exec("buff = sys.registry['user.home']+'/moneydance/scripts/'");
        PyObject result ;
        String buff22 = new String ("sys.path.count('/opt/moneydance/scripts/')");
        result = engine.eval (buff22);      // this worked .. run the command on jython and get the results in Java :)
        if (debug){ System.err.println("JConsole 118 engine.eval sys.path.count "+ result.asInt());};
        if (!(result.asInt() >= 1) ){
              if (debug){ System.err.println("JConsole 120 doing sys.path.append ");};
              engine.exec("sys.path.append('/opt/moneydance/scripts/')");
              }
        else {
              if (debug){ System.err.println("JConsole 124 NOT doing sys.path.append ");};
         }

//        if (!sys.path.__contains__("/opt/moneydance/scripts/")) //
//          if (!os.path.exists("/opt/moneydance/scripts/")) //
        engine.exec("os.chdir('/opt/moneydance/scripts')");
        /**  moved this to main along with all my testing commented out
        try {
            ProcessBuilder pb = new ProcessBuilder(System.getenv("EDITOR"), System.getenv("HOME")+"/.moneydance/jConsole2023.log" );
            Process p = pb.start();
        }
        catch (IOException e){
            if (debug){ System.err.println("Jconsole 136 ProcessBuilder Exception");};
            e.printStackTrace();
			}
			**/
//        engine.exec ("try:\n  execfile('runScripts.py')\nexcept:\n  sys.stderr.write('JConsole2023 failed to load runScripts.py\\n')\n");  // moved to main//      fire up the script picker swing GUI
//      engine.exec ("del buff"); // buff not used
//		engine.exec("from definitions import definitions");  // definitions not used for this anymore
//		engine.exec("for buff in definitions.ClassPathNames:\n  exec(buff)\n");
		engine.set("moneydance", context );
		addKeyListener(this);
		filter = new ConsoleFilter(this);
		((AbstractDocument) getDocument()).setDocumentFilter(filter);
		// start text and edit location
		setText("JConsole2023 Interactive Console\r\nuse execfile('mysript.py')\r\n >>>"); // this shows up as the first three lines on the console
		// editStart = getText().length();
		getCaret().setDot(editStart);
	}

	@Override
	public void setText(String text)
	{
		setText(text, true);
	}

	/**
	 * @param text
	 * @param updateEditStart
	 */
	public void setText(String text, boolean updateEditStart)
	{
		filter.useFilters = false;
		super.setText(text);
		filter.useFilters = true;
		if (updateEditStart)
		{
			editStart = text.length();
		}
		getCaret().setDot(text.length());
	}
// setup the document filter so output and old text can't be modified

	private class ConsoleFilter extends DocumentFilter
	{
		private JConsole2023	console;
		public boolean		useFilters;

		public ConsoleFilter(JConsole2023 console)
		{
			this.console = console;
			useFilters = true;
		}

		@Override
		public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException
		{
            if (debug){ System.err.println("docfilter insertString");};
			if (useFilters)
			{
				// determine if we can insert
				if (console.getSelectionStart() >= console.editStart)
				{
					// can insert
					fb.insertString(offset, string, attr);
				}
				else
				{
					// insert at the end of the document
					fb.insertString(console.getText().length(), string, attr);
					// move cursor to the end
					console.getCaret().setDot(console.getText().length());
					// console.setSelectionEnd(console.getText().length());
					// console.setSelectionStart(console.getText().length());
				}
			}
			else
			{
				fb.insertString(offset, string, attr);
			}
		}

		@Override
		public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException
		{
//		    if (debug){ System.err.println("docfilter replace");};
			if (useFilters)
			{
				// determine if we can replace
				if (console.getSelectionStart() >= console.editStart)
				{
					// can replace
					fb.replace(offset, length, text, attrs);
				}
				else
				{
					// insert at end
					fb.insertString(console.getText().length(), text, attrs);
					// move cursor to the end
					console.getCaret().setDot(console.getText().length());
					// console.setSelectionEnd(console.getText().length());
					// console.setSelectionStart(console.getText().length());
				}
			}
			else
			{
				fb.replace(offset, length, text, attrs);
			}
		}

		@Override
		public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException
		{
			if (debug){ System.err.println("docfilter remove");};
			if (useFilters)
			{
				if (offset > console.editStart)
				{
					// can remove
					fb.remove(offset, length);
				}
				else
				{
					// only remove the portion that's editable
					fb.remove(console.editStart, length - (console.editStart - offset));
					// move selection to the start of the editable section
					console.getCaret().setDot(console.editStart);
					// console.setSelectionStart(console.editStart);
					// console.setSelectionEnd(console.editStart);
				}
			}
			else
			{
				fb.remove(offset, length);
			}
		}
	}

// setup the event handlers and input processing

	@Override
	public void keyPressed(KeyEvent e)
	{
 //       if (debug){ System.err.println("JConsole KeyPressed 282  "+ history.getCurrentCommand());};
		if (e.isControlDown()) // control key is pressed
		{
			if (e.getKeyCode() == KeyEvent.VK_A && !e.isShiftDown() && !e.isAltDown())
			{
				// handle select all (control A )
				// if selection start is in the editable region, try to select
				// only editable text
//				System.err.println("Control A Pressed");
				if (getSelectionStart() >= editStart)
				{
					// however, if we already have the editable region selected,
					// default select all
					if (getSelectionStart() != editStart || getSelectionEnd() != this.getText().length())
					{
						setSelectionStart(editStart);
						setSelectionEnd(this.getText().length());
						// already handled, don't use default handler
						e.consume();
					}
				}
			}
		}	
		else if (e.getKeyCode() == KeyEvent.VK_DOWN && !e.isShiftDown() && !e.isAltDown())
		{
				// next in command history (control down )
//				System.err.println(" Down Pressed");
				StringBuilder temp = new StringBuilder(getText());
				// remove the current command
				temp.delete(editStart, temp.length());
				temp.append(history.getNextCommand());
				setText(temp.toString(), false);
				e.consume();
		}
		else if (e.getKeyCode() == KeyEvent.VK_UP && !e.isShiftDown() && !e.isAltDown())
		{
				// prev in command history (control up )
//				System.err.println(" Up Pressed");				
				StringBuilder temp = new StringBuilder(getText());
				// remove the current command
				temp.delete(editStart, temp.length());
				temp.append(history.getPrevCommand());
				setText(temp.toString(), false);
				e.consume();
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			// handle script execution
			if (!e.isShiftDown() && !e.isAltDown())
			{
			    if (debug){ System.err.println("JConsole 329 Enter Pressed");}; // ..............................Enter keyPressed
				if (running)
				{
					// we need to put text into the input stream
					StringBuilder text = new StringBuilder(this.getText());
					text.append(System.getProperty("line.separator"));
					String command = text.substring(editStart);
					setText(text.toString());
					((ConsoleInputStream) in).addText(command);      // InputStream addText
				}
				else
				{
					// run the engine
					StringBuilder text = new StringBuilder(this.getText());
					String command = text.substring(editStart);
					text.append(System.getProperty("line.separator"));
					setText(text.toString());
					// add to the history
					history.add(command);                                    // history called here
					// run on a separate thread
					pythonThread = new Thread(new PythonRunner(command));
					// so this thread can't hang JVM shutdown
					pythonThread.setDaemon(true);
					pythonThread.start();
				}
				e.consume();
			}
			else if (!e.isAltDown()) // shift enter , removes selected text on screen . select text with shift up or control A
			{                        // control C , control V and control X all work 
				// shift+enter
//			        System.err.println("Shift Enter Pressed");
				StringBuilder text = new StringBuilder(this.getText());
				if (getSelectedText() != null)
				{
					// replace text
					text.delete(getSelectionStart(), getSelectionEnd());
				}
				text.insert(getSelectionStart(), System.getProperty("line.separator"));
				setText(text.toString(), false);
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_HOME)
		{
			int selectStart = getSelectionStart();
			if (selectStart > editStart)
			{
				// we're after edit start, see if we're on the same line as edit
				// start
				for (int i = editStart; i < selectStart; i++)
				{
					if (this.getText().charAt(i) == '\n')
					{
						// not on the same line
						// use default handle
						return;
					}
				}
				if (e.isShiftDown())
				{
					// move to edit start
					getCaret().moveDot(editStart);
				}
				else
				{
					// move select end, too
					getCaret().setDot(editStart);
				}
				e.consume();
			}
		}
	}

	private class PythonRunner implements Runnable
	{
		private String	commands;

		public PythonRunner(String commands)
		{
            this.commands = commands;

		    if (debug){ System.err.println("Jconsole 409 "+commands);};  // it showed up here

		}

		@Override
		public void run()
		{
			running = true;
			try
			{

			    if (debug){ System.err.println("Jconsole run() 420 "+commands);}; // ls showed up here too  .. commands is a String
			    if (commands.equals("ls"))  // this worked    == doesn't work on strings in java !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			    {
                    commands = "os.listdir(os.curdir)";   // try swapping the command out... this worked but the output is ugly
                    if (debug){ System.err.println("Jconsole run() found cummands ls: "+ commands);};
                }
                else if (commands.equals("pwd"))
			    {
                    commands = "os.getcwd()";
                    if (debug){ System.err.println("Jconsole run() found cummands pwd: "+ commands);};
                }
                else if (commands.startsWith("cd "))
			    {
			        String temp2 = commands.replaceFirst("cd ","");
                   if (debug){ System.err.println("Jconsole run() found cummands cd temp2..: "+ temp2);};
 			        commands = "os.chdir('"+temp2+"')";
                    if (debug){ System.err.println("Jconsole run() found cummands cd ..: "+ commands);};
                }




			    engine.runsource(commands);                                       // produces the NameError .. maybe we have to live with it...
			}
			catch (PyException e)
			{
                if (debug){ System.err.println("Jconsole 446 PyException "+commands);};
				// prints out the python error message to the console
				e.printStackTrace();
			}
			// engine.eval(commands, context);
			StringBuilder text = new StringBuilder(getText());
			text.append(">>> ");
//			System.err.println("Jconsole 453"+text.toString());
			setText(text.toString());
			running = false;
		}
	}

//	@SuppressWarnings("deprecation")
//	@Override
//	public void finalize()
//	{
//		if (running)
//		{
//			// I know it's depracated, but since this object is being destroyed,
//			// this thread should go, too
//			pythonThread.stop();
//			//pythonThread.destroy();
//		}
//	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// don't need to use this for anything
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// don't need to use this for anything
	}
}
