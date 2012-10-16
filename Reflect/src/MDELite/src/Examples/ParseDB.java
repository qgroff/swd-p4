/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import java.io.File;
import vm2t.*;
/**
 *
 * this program is to test the DB parser that is part of the vm2t tool
 */
public class ParseDB {
    
    public static void main(String args[]) {
        Model m = new Model(new File("testData/Parse/3patched.sdb.pl"));
    }
}
