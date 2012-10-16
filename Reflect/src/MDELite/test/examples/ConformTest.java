/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import mdelite.GProlog;
import mdelite.UMLFpl;
import org.junit.Test;

/**
 *
 * @author dsb
 */
public class ConformTest {
    public ConformTest() {
    }

    
    public void theWork(String filename) {
        String outputFile = filename+"Errors.txt";
        RegTest.Utility.redirectStdErr(outputFile);
        UMLFpl u = new UMLFpl(filename);
        try {
            u.conform();
        } catch (RuntimeException e) {
            assert (e.getMessage().equals(GProlog.conformanceFailure));
        }
        RegTest.Utility.validate(outputFile,"Correct/"+outputFile,false);
    }
    
    @Test
    public void test1() {
        //theWork("TestData/Error/10standard");
    }
    
    @Test
    public void test2() {
        //theWork("TestData/Error/3standard");
    }
}
