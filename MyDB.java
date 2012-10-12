package reflect;
import java.util.ArrayList;

/**
 *
 * @author aengroff
 */
class MyDB {

    public MyDB(String packageName) {
        name = packageName;
//        dbStrings.add("dbase(mydb,[class,attribute,method,parameter]).");
//        dbStrings.add("tuple(class,L):-class(I,N),L=[I,N].");
//        dbStrings.add("tuple(atribute,L):-attribute(I,N,T,C),L=[I,N,T,C].");
//        dbStrings.add("tuple(method,L):-method(I,N,T,C),L=[I,N,T,C].");
//        dbStrings.add("tuple(parameter,L):-parameter(I,T,M),L=[I,T,M].");
    }

    public String name;
    public ArrayList<String> dbClasses = new ArrayList<String>();
	public ArrayList<String> dbAttributes = new ArrayList<String>();
	public ArrayList<String> dbMethods = new ArrayList<String>();
	public ArrayList<String> dbParameters = new ArrayList<String>();
    public int[] counter = {0,0,0,0};
}
