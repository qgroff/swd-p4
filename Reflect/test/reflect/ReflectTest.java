/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reflect;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Groff
 */
public class ReflectTest {
    
    /**
     * First test of main method, attempts reflection on yumlparser.jar
     */
    @Test
    public void printTestYumlparser() {
        RegTest.Utility.redirectStdOut("yumlOut.txt");
        
        String[] args = new String[2];
        args[0] = "yumlparser";
        args[1] = "otherclasses/yumlparser";
        Reflect.main(args);
        
        RegTest.Utility.validate("yumlOut.txt", "correctYumlOut.txt", false);
    }
    
    /**
     * Second test of main method, attempts reflection on itself (reflect.jar)
     */
    @Test
    public void printTestReflect() {
        RegTest.Utility.redirectStdOut("reflectOut.txt");
        
        String[] args = new String[2];
        args[0] = "reflect";
        args[1] = "build/classes/reflect";
        Reflect.main(args);
        
        RegTest.Utility.validate("reflectOut.txt", "correctReflectOut.txt", false);
    }
    
    //Note: I was having the same major.minor version issues, so I left the old
    // version of yumlparser in my path and was testing it.  The other unit
    // tests for Project 2 are what should be output, but I can't get it to do
    // so.
    
    @Test
    public void yumlTestYumlparser() {
        RegTest.Utility.redirectStdOut("yumlTestOut.yuml");
        
        String[] args = new String[3];
        args[0] = "yumlparser";
        args[1] = "otherclasses/yumlparser";
        args[2] = "yuml";
        Reflect.main(args);
        
        RegTest.Utility.validate("yumlTestOut.yuml", "correctYumlTestOut.yuml", false);
    }
    
    @Test
    public void yumlTestGraff() {
        RegTest.Utility.redirectStdOut("graffTestOut.yuml");
        
        String[] args = new String[3];
        args[0] = "graff";
        args[1] = "otherclasses/graff";
        args[2] = "yuml";
        Reflect.main(args);
        
        RegTest.Utility.validate("graffTestOut.yuml", "correctGraffTestOut.yuml", false);
    }
    
    @Test
    public void yumlTestNotepad() {
        RegTest.Utility.redirectStdOut("notepadTestOut.yuml");
        
        String[] args = new String[3];
        args[0] = "Notepad";
        args[1] = "otherclasses/Notepad";
        args[2] = "yuml";
        Reflect.main(args);
        
        RegTest.Utility.validate("notepadTestOut.yuml", "correctNotepadTestOut.yuml", false);
    }
    
    @Test
    public void yumlTestQuark() {
        RegTest.Utility.redirectStdOut("quarkTestOut.yuml");
        
        String[] args = new String[3];
        args[0] = "quark";
        args[1] = "otherclasses/quark";
        args[2] = "yuml";
        Reflect.main(args);
        
        RegTest.Utility.validate("quarkTestOut.yuml", "correctQuarkTestOut.yuml", false);
    }
}
