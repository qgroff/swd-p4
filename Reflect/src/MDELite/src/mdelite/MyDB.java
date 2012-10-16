/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDELite.src.mdelite;

/**
 *
 * @author aengroff
 */
public class MyDB {
    public MyDB(String filepath, String packageName) {
        path = filepath;
        myPackage = packageName;
    }
    
    private String path;
    private String myPackage;
    
    public String getPath() {
        return path;
    }
    
    public String getPackage() {
        return myPackage;
    }
    
    public JavaSource toJava() {
        vm2t.main(path, "MDELite.libvm.toJava.vm");
        return new JavaSource(myPackage);
    }
}
