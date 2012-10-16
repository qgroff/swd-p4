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
public class Violet extends MDELite {

    @Override
    public String fileType() {
        return ".violet";
    }

    @Override
    public String partialFileType() {
        return ".violet";
    }

    public Violet(String filename) {
        super(filename);
    }

    /* the following are transformations */
    public Violetpl toVioletpl() { 
        //System.out.println("filename: " + filename);
        Violetpl result = new Violetpl(filename);
        String[] args = {filename+fileType()};
        violetParser.main.Main.main(args);
        result.conform();
        return result;
    }
}
