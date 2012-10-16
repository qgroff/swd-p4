/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import org.junit.Test;

import mdelite.*;

/**
 *
 * @author dsb
 */
public class Yuml2VioletTest {

//    @Test
//    public void test1() {
//        theWork("Notepad");
//    }
//
//    @Test
//    public void test2() {
//        theWork("graff");
//    }
//
//    @Test
//    public void test3() {
//        theWork("quark");
//    }

    @Test
    public void test4() {
        theWork("yumlparser");
    }
    
    public void theWork(String name) {
        String folder = "TestData/Yuml2Violet/";
        String[] args = { "yuml", "violet", folder+name };
        Convert.main(args);
        RegTest.Utility.validate(folder+name+"p.violet", "Correct/"+folder+name+".violet",true);
    }

}
