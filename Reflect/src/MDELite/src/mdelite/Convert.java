/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 */
public class Convert extends Common {

    public static void marquee() {
        System.out.println("Usage: Convert <from> <to> <filename(not including dot extension)>");
        System.out.println("       <from>,<to> := umlf | yuml | violet ");
        System.exit(1);
    }
    
    public static void main(String args[]) {
        if (args.length!=3)
            marquee();
        int from = getType(args[0]);
        int to   = getType(args[1]);
        SDB in = toSDB(from,args[2]);
        spoke(to,in);
    }
    
    public static SDB toSDB(int from, String f) {
        switch(from) {
            case 1: // umlf
                UMLF x = new UMLF(f);
                UMLFpl u = x.toUMLFpl();
                return u.toSDB();
            case 2: // yuml
                Yuml yx = new Yuml(f);
                Yumlpl y = yx.toYumlpl();
                return y.toSDB().refreshPosition(f + "p");
            case 3: // violet
                Violet v = new Violet(f);
                Violetpl p = v.toVioletpl();
                return p.toSDB();
            default:
                System.err.println("unrecognizable type " + from);
                System.exit(1);
                return null; // pacify whiney compiler
        }
    }
    
    public static void spoke(int to, SDB in) {
        switch(to) {
            case 1: // umlf
                UMLFpl x = in.toUMLFpl();
                x.toUMLF();
                break;
            case 2: // yuml
                Yumlpl y = in.toYumlpl();
                y.toYuml();
                break;
            case 3: // violet
                Violetpl v = in.toVioletpl();
                v.toViolet();
                break;
            default:
                System.err.println("unrecognizable type " + to);
                System.exit(1);
        }
    }
}
