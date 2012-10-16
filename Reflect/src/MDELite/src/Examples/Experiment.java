/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import mdelite.*;

/**
 *
 * @author dsb
 */
public class Experiment {
    
    public static void main(String[] args) {
        UMLF start = new UMLF("TestData/testDataUMLF/1standard");
        UMLFpl u = start.toUMLFpl();
        SDB s = u.toSDB();
        Violetpl v = s.toVioletpl();
        Violet result = v.toViolet();
    }
    
}
