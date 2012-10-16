/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author dsb
 */
public class Yuml extends MDELite {

    @Override
    public String fileType() {
        return ".yuml";
    }

    @Override
    public String partialFileType() {
        return "";
    }

    public Yuml(String filename) {
        super(filename);
    }

    /* the following are transformations */
    public Yumlpl toYumlpl() {
        String[] args = {filename};
        yumlparser.Main.main(args);
        return new Yumlpl(filename);
    }
}







