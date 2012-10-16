/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import mdelite.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dsb
 */
public class Violet2UmlfTest {
    
    public Violet2UmlfTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public void theWork(String filename) {
        //First take in the standard violet xml file and convert to violet pl
        Violet violetXml    = new Violet(filename);
        Violetpl violetPl   = violetXml.toVioletpl();
        //Next convert violet pl to standard pl
        SDB standardDB      = violetPl.toSDB();
        //Next convert standard pl to Umlf pl
        UMLFpl umlfPl       = standardDB.toUMLFpl();
        //Next convert Umlf pl to Umlf xml
        UMLF umlfXml     = umlfPl.toUMLF();
        
        //Make sure Umlf is right
        RegTest.Utility.validate(umlfXml.fullName, "Correct/"+filename+".violet.umlf.xml",false);
    }
    
    
    @Test
    public void testStandard1() {
        theWork("TestData/Violet2Umlf/1standard");
    }
    
//    @Test
//    public void testStandard2() {
//        theWork("TestData/UMLF2YUML/2standard");
//    }
//    
//    @Test
//    public void testStandard3() {
//        theWork("TestData/UMLF2YUML/3standard");
//    }
//    
//    @Test
//    public void testStandard4() {
//        theWork("TestData/UMLF2YUML/4standard");
//    }
//    
//    @Test
//    public void testStandard5() {
//        theWork("TestData/UMLF2YUML/5standard");
//    }
}
