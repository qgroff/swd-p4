/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

import java.io.File;
import java.io.PrintStream;
import Positioner.*;

/**
 *
 * @author dsb
 */
public class Dot extends MDELite {

    @Override
    public String fileType() {
        return ".dot";
    }
    
    @Override
    public String partialFileType() { return ""; }

    public Dot(String filename) {
        super(filename);
    }

    /****************** methods/transformations of Dot objects ************/
    public Dot dot2dot() {
        return dot2dot("");
    }
    
    public Dot dot2dot(String extra) {
        String infile = filename + fileType();
        String outfile = filename + extra + fileType();
        String[] Args = {"infile=" + infile, "outfile=" + outfile, "--algorithm=planarization"};
        try {
            if (MDELite.configFile.getProperty("POSITION_PACKAGE") != null &&
                MDELite.configFile.getProperty("POSITION_PACKAGE").
                    toLowerCase().equals("positioner"))
                Positioner.Main.main(Args);
            else
                de.cau.cs.kieler.kwebs.tools.ConsoleClient.main(Args);
            
        } catch (Exception ex) {
            System.err.println("unable to call Kieler server or to translate " + infile);
            done(ex);
        }
        
        return new Dot(filename + extra);
    }

    public SDB toSDB() {
        String[] args = { partialName };
        xformPrograms.dot2sdb.main(args);
        // don't test conformance as it won't pass
        // remember: all that is being returned is is a position table
        return new SDB(filename);
    }
}
