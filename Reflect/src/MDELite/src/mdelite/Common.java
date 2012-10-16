/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 */
public class Common {
    public static String homePath="";
    
    public static void setHomePath(boolean printMe) {
                // set homePath -- the directory in which MDELite.jar resides
        Class me = Main.class;
        String tmp = me.getProtectionDomain().getCodeSource().getLocation().getPath();
        homePath = tmp.replace("MDELite.jar","").substring(1);
        // the next line only for adjusting homePath for a netbeans environment
        homePath = homePath.replace("MDELite/build/classes","MDELite");
        // eric's *nix path fix (added leading /)
        if (!homePath.contains(":"))
            homePath = "/"+homePath;
        if(printMe) 
            System.out.println(homePath);
    }
    
    public static void main(String[] args) {
        setHomePath(true);
    }
    
    public static String typeString(int t) {
        switch(t) {
            case 1: return ".umlf";
            case 2: return ".yuml";
            case 3: return ".violet";
            default: System.err.println("unrecognizable type "+t+" input to Common.typeString()");
                System.exit(1);
                return null; // pacify whiny compiler
        }
    }

    public static int getType(String t) {
        if (t.equals("umlf")) {
            return 1;
        }
        if (t.equals("yuml")) {
            return 2;
        }
        if (t.equals("violet")) {
            return 3;
        }
        System.err.println("unrecognizable type "+t+" to Common.getType()");
        System.exit(1);
        return 0; // pacify whiney compiler
    }
    
}
