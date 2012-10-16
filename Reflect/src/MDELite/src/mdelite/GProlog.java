/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

/**
 *
 * @author dsb
 */
public abstract class GProlog extends MDELite {
    public static final String conformanceFailure = "Conformance Failure";

    GProlog(String filename, String[] files) {
        super(filename);
        mergeFiles(files);
    }

    // these empty constructors are needed because obscure run-time
    // exceptions are raised otherwise that take 1/2 to track down
    GProlog(String filename) {
        super(filename);
    }

    GProlog() {
    }  // shouldn't be called.

    // the result of merged files is the new value of the given object
    protected void mergeFiles(String[] filenames) {
        try {
            PrintStream ps = getPrintStream();
            for (String f : filenames) {
                stuff(f, ps);
            }
            ps.flush();
            ps.close();
        } catch (Exception e) {
            done(e);
        }
    }

    protected static void stuff(String inFileName, PrintStream ps) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFileName));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                ps.println(line);
            }
        } catch (Exception e) {
            done(e);
        }
    }

    // executes a prolog object.  The execution produces a file, whose object is created prior to the call
    // I know this is strange but this is a general problem of run-time objects being paired with files
    // at some point, one will exist before the other
    public void executeProlog(MDELite out) {
        try {
            PrintStream ps;
            ps = new PrintStream("script.txt");
            ps.print(":-['" + fullName + "'],tell('" + out.fullName + "'),run,told,halt.");
            ps.flush();
            ps.close();
        } catch (Exception e) {
            done(e);
        }
        String[] cmdarray = {MDELite.configFile.getProperty("SWI_PROLOG_LOCATION"), "--quiet", "-f", "script.txt"};
        try {
            execute(cmdarray);
        } catch (Exception e) {
            System.err.println("MDELite halts -- SWI Prolog Errors detected");
            System.err.println("debug this prolog file:  " + fullName);
            System.exit(1);
        }
        File s = new File("script.txt");
        s.delete();
    }

    // this method is used to test conformance of a database 
    // with its constraints.  it is coded only for this purpose
    // as it outputs nothing.
    public void executeProlog() {
        try {
            PrintStream ps;
            ps = new PrintStream("script.txt");
            ps.print(":-['" + fullName + "'],run,halt.");
            ps.flush();
            ps.close();
        } catch (Exception e) {
            done(e);
        }
        String[] cmdarray = {MDELite.configFile.getProperty("SWI_PROLOG_LOCATION"), "--quiet", "-f", "script.txt"};
        try {
            execute(cmdarray);
        } catch (Exception e) {
            System.err.println("MDELite halts -- SWI Prolog Errors detected");
            System.err.println("debug this prolog file:   " + fullName);
            System.err.println(conformanceFailure);
            System.exit(1);
        }
        File s = new File("script.txt");
        s.delete();
    }
    
    abstract void conform();
}
