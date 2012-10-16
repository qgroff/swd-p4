/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDELite.src.mdelite;

/**
 *
 * @author aengroff
 */
public class JavaByteCode {
    public JavaByteCode(String packageName, String classPath) {
        myPackage = packageName;
        path = classPath;
    }
    
    private String myPackage;
    private String path;
    
    public String getPackage() {
        return myPackage;
    }
    
    public String getPath() {
        return path;
    }
    
    public MDELite.src.mdelite.MyDB p4() {
        String[] args = {myPackage, path, "mydb"};
        reflect.Reflect.main(args);
        return new MDELite.src.mdelite.MyDB(myPackage + ".mydb.pl", myPackage);
    }
}
