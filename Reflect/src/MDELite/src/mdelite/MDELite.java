/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

import java.io.*;
import java.util.Properties;

/**
 *
 * @author dsb
 */
public abstract class MDELite {

    public String filename;
    public String partialName;
    public String fullName;
    public static Properties configFile;

    static {
        configFile = new Properties();
        String cp = null;
        try {
            Common.setHomePath(false);
            cp = Common.homePath+"config.properties";
            configFile.load(new FileInputStream(cp));
        } catch (IOException e) {
            System.err.println("Error in loading config.properties file --- MDELite halts " + cp);
            System.exit(1);
        }
    }

    MDELite(String filename) {
        this.filename = filename;
        this.partialName = filename + partialFileType();
        this.fullName = filename + fileType();
    }

    protected MDELite() {
    } // shouldn't be called

    public abstract String fileType();

    public abstract String partialFileType();

    public static String[] makeArray(MDELite[] array) {
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].fullName;
        }
        return result;
    }

    public PrintStream getPrintStream() {
        PrintStream result = null;
        String tmp = fullName;
        try {
            result = new PrintStream(fullName);
        } catch (Exception e) {
            done(e);
        }
        return result;
    }

    public void delete() {
        File f = new File(fullName);
        f.delete();
    }

//    public static void execute(String[] cmdarray) {
//        try {
//            Runtime rt = Runtime.getRuntime();
//            Process p = rt.exec(cmdarray);
//            // assume input, output, error stream is standard input, output, error
//            p.waitFor();
//            p.destroy();
//        } catch (Exception e) {
//            done(e);
//        }
//    }
    public static void execute(String[] cmdarray) throws Exception {
        String line;
        boolean error = false;
        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(cmdarray);
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = er.readLine()) != null) {
                error = true;
                System.err.println("SWIProlog Stderr : " + line);
            }
            while ((line = in.readLine()) != null) {
                System.out.println("SWIProlog Stdout : " + line);
            }
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            done(e);
        }
        if (error) {
             System.err.print("Errors in executing command array: ");
             for (String i:cmdarray) System.err.print(" "+i);
             System.err.println();
             throw new Exception();
        }
    }

    public void invokeVm2t(MDELite output, String vmfile) {
        try {
            String[] args = {fullName, vmfile};
            vm2t.Main.main(args);
            File f = new File("vm2toutput.txt");
            File n = new File(output.fullName);
            n.delete();
            f.renameTo(n);
        } catch (Exception e) {
            done(e);
        }
    }

    public static void done(Exception e) {
        System.err.println(e.getMessage());
        System.exit(1);
    }
}
