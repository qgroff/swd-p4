package reflect;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Collections;

/**
 * Reflect - Java Reflection Class
 * @author Andrew Engroff
 * @uteid ade466
 * Displays a list of all non-nested classes contained within a directory
 */
public class Reflect {

    /**
     * Prints a list of all non-nested classes contained within the directory
     * given in args[1].
     * @param args the command line arguments
     * @param args[0] the qualified name of a Java class on the classpath
     * @param args[1] the directory where the class files of args[0] can be
     *                found
     */
    public static void main(String[] args) {
        // TODO usage statement and testing for # of arguments
        String packageArg = args[0];
        String directoryArg = args[1];
        String command;
        if (args.length > 2) {
            command = args[2];
        } else {
            command = "print";
        }

        MyDB db = new MyDB(packageArg);

        try {
            File directory = new File(directoryArg);
            String[] dirContents = directory.list();
            //TODO: Fail gracefully on directory with no classes
            for (int ii = 0; ii < dirContents.length; ii++) {
                String filename = dirContents[ii];
                if (filename.endsWith(".class") && !filename.contains("$")) {
                    filename = filename.substring(0, filename.length() - 6); //trim .class from filename
                    if (command.equals("print")) {
                        System.out.println("class " + filename);
                    }
                    Class<?> c = Class.forName(packageArg + "." + filename);
                    doReflection(c, db, command);
                }
            }
            if (command.equals("mydb")) {
                outputMyDB(db);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("main: Could not find class.  " + e);
        }
    }

    /**
     * Wrapper to call each individual process and print: constructors, fields,
     * and methods.
     * @param c Class we want to reflect upon
     */
    private static void doReflection(Class<?> c, MyDB db, String command) {
        String cname = c.getSimpleName();
        Member[] constructors = processConstructors(c, command);
        Member[] fields = processFields(c, command);
        Member[] methods = processMethods(c, command);
        if (command.equals("yuml")) {
            doYuml(constructors, fields, methods, c);
        }
        else if (command.equals("print")) {
            System.out.println("----");
        }
        else if (command.equals("mydb")) {
            db.dbClasses.add("class(c"+db.counter[0]+",\'"+cname+"\').");
            addAttributes(fields, db, cname);
            addMethods(constructors, methods, db, cname);
            db.counter[0]++;
        }
        else { System.out.println("Unrecognized Command."); }
    }

    private static void addAttributes(Member[] fields, MyDB db, String cname) {
        for (int ii = 0; ii < fields.length; ii++) {
            String aname = trimMember(fields[ii].getName(), cname);
            String type = trimMember(((Field)fields[ii]).getType().getSimpleName(), cname);
            db.dbAttributes.add("attribute(a"+db.counter[1]+",\'"+aname+"\',\'"+type+"\',c"+db.counter[0]+").");
            db.counter[1]++;
        }
    }

    private static void addMethods(Member[] constructors, Member[] methods, MyDB db, String cname) {
        for (int ii = 0; ii < constructors.length; ii++) {
            String conname = trimMember(constructors[ii].getName(), cname);
            Class<?>[] types = ((Constructor)constructors[ii]).getParameterTypes();
            db.dbMethods.add("method(m"+db.counter[2]+",\'"+conname+"\',\'"+cname+"\',c"+db.counter[0]+").");
            for (int ij = 0; ij < types.length; ij++) {
                String typename = trimMember(types[ij].getSimpleName(), cname);
                db.dbParameters.add("parameter(p"+db.counter[3]+",\'"+typename+"\',m"+db.counter[2]+").");
                db.counter[3]++;
            }
            db.counter[2]++;
        }
        for (int ii = 0; ii < methods.length; ii++) {
            String mname = trimMember(methods[ii].getName(), cname);
            Class<?>[] types = ((Method)methods[ii]).getParameterTypes();
            db.dbMethods.add("method(m"+db.counter[2]+",\'"+mname+"\',\'"+cname+"\',c"+db.counter[0]+").");
            for (int ij = 0; ij < types.length; ij++) {
                String typename = trimMember(types[ij].getSimpleName(), cname);
                db.dbParameters.add("parameter(p"+db.counter[3]+",\'"+typename+"\',m"+db.counter[2]+").");
                db.counter[3]++;
            }
            db.counter[2]++;
        }
    }

    private static void outputMyDB(MyDB db) {
        Collections.sort(db.dbClasses);
		Collections.sort(db.dbAttributes);
		Collections.sort(db.dbMethods);
		Collections.sort(db.dbParameters);
		db.dbClasses.add(0, "%table(class,[classid,\"name\"]).");
		db.dbAttributes.add(0, "%table(attribute,[attrid,\"name\",\"type\",classid]).");
		db.dbMethods.add(0, "%table(method,[methid,\"name\",\"returntype\",classid]).");
		db.dbParameters.add(0, "%table(parameter,[paramid,\"type\",methid]).");
        String filename = db.name + ".mydb.pl";

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(filename));
            for (String s : db.dbClasses) {
                out.write(s);
                out.newLine();
            }
			for (String s : db.dbAttributes) {
                out.write(s);
                out.newLine();
            }
			for (String s : db.dbMethods) {
                out.write(s);
                out.newLine();
            }
			for (String s : db.dbParameters) {
                out.write(s);
                out.newLine();
            }
        } catch (FileNotFoundException e) {
            System.out.print("File not found:  ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.print("IOException:  ");
            e.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Formats and executes given action for constructors of class c
     * @param c Class for which we want the constructors printed
     */
    private static Member[] processConstructors(Class<?> c, String command) {
        Member[] members = c.getDeclaredConstructors();
        Integer[] modifiers = new Integer[members.length];
        for (int ii = 0; ii < members.length; ii++) {
            modifiers[ii] = members[ii].getModifiers();
            if (command.equals("print")) {
                printConstructors(members, modifiers, c);
            }

        }
        return members;
    }

    /**
     * Formats and executes given action for fields of class c
     * @param c Class for which we want the fields printed
     */
    private static Member[] processFields(Class<?> c, String command) {
        Member[] members = c.getDeclaredFields();
        Integer[] modifiers = new Integer[members.length];
        for (int ii = 0; ii < members.length; ii++) {
            modifiers[ii] = members[ii].getModifiers();
        }
        if (command.equals("print")) {
            printFields(members, modifiers, c);
        }
        return members;
    }

    /**
     * Formats and executes given action for methods of class c
     * @param c Class for which we want the methods printed
     */
    private static Member[] processMethods(Class<?> c, String command) {
        Member[] members = c.getDeclaredMethods();
        Integer[] modifiers = new Integer[members.length];
        for (int ii = 0; ii < members.length; ii++) {
            modifiers[ii] = members[ii].getModifiers();
        }
        if (command.equals("print")) {
            printMethods(members, modifiers, c);
        }
        return members;
    }

    private static void printConstructors(Member[] members, Integer[] modifiers, Class<?> c) {
        System.out.println("Constructors");
        assert (members.length == modifiers.length): "Called improperly; length of argument arrays do not match.";
        for (int ii = 0; ii < members.length; ii++) {
            String formattedMember = trimConstructor(members[ii].toString(), c.getName());
            System.out.println(formattedMember);
        }
    }

    private static void printFields(Member[] members, Integer[] modifiers, Class<?> c) {
        System.out.println("Fields");
        assert (members.length == modifiers.length): "Called improperly; length of argument arrays do not match.";

        for (int ii = 0; ii < members.length; ii++) {
            if (!Modifier.isPublic(modifiers[ii])) {
                continue;
            }
            Field field = (Field) members[ii];
            StringBuilder result = new StringBuilder();
            if (Modifier.isStatic(modifiers[ii])) {
                result.append("static ");
            }
            result.append(trimMember(field.getType().getSimpleName(), c.getSimpleName()));
            result.append(" ");
            result.append(trimMember(field.getName(), c.getSimpleName()));
            System.out.println(result);
        }
    }

    private static void printMethods(Member[] members, Integer[] modifiers, Class<?> c) {
        System.out.println("Methods");
        assert (members.length == modifiers.length): "Called improperly; length of argument arrays do not match.";

        for (int ii = 0; ii < members.length; ii++) {
            if (!Modifier.isPublic(modifiers[ii])) {
                continue;
            }
            Method method = (Method) members[ii];
            StringBuilder result = new StringBuilder();
            if (Modifier.isStatic(modifiers[ii])) {
                result.append("static ");
            }
            result.append(trimMember(method.getReturnType().getSimpleName(), c.getSimpleName()));
            result.append(" ");
            result.append(trimMember(method.getName(), c.getSimpleName()));
            result.append("( ");
            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int ij = 0; ij < parameterTypes.length; ij++) {
                result.append(trimMember(parameterTypes[ij].getSimpleName(), c.getSimpleName()));
                if (ij + 1 < parameterTypes.length) {
                    result.append(", ");
                }
            }
            result.append(" )");
            System.out.println(result);
        }
    }

    /**
     * Trims unwanted package names in types found in the given Field or Method
     * signature, including its own parent package.
     * @param member The String containing the Member data we want to trim down
     * @param selfClass The name of the class from which we got this data
     * @return The trimmed string
     */
    private static String trimMember(String member, String selfClass) {

        return member.replaceAll("(java(?:(\\.lang)|(\\.util)|(\\.awt(\\.event)?)|(\\.io)|(x\\.swing))|(" + selfClass + "))\\.", "");
    }

    /**
     * Performs an extra trim just for constructors, to remove the package name
     * but leave the class name in the final formatted constructor string
     * @param constructor The String containing the (mostly pre-trimmed)
     *    Constructor data we want to do a final trim on
     * @param selfClass The name of the class from which we got this data
     * @return The trimmed string
     */
    private static String trimConstructor(String constructor, String selfClass) {
        constructor = constructor.replaceAll("(?:public |final |native |throws .*)", "");
        constructor = constructor.replaceAll("(java(?:(\\.lang)|(\\.util)|(\\.awt(\\.event)?)|(\\.io)|(x\\.swing))|(" + selfClass + "))\\.", "");
        String packageNameWithDot = selfClass.split("\\.")[0] + "\\.";
        return constructor.replaceAll(packageNameWithDot, "");
    }

    /**
     * Prints to stdout the Yuml specification for a single class.  If I had
     * more time I would output it to a file but I don't have time for the
     * mental hell that is Java I/O--at least not without a lot more time to
     * do a better program design.
     * @param constructors The array of constructors belonging to the class.
     * @param fields The array of fields belonging to the class.
     * @param methods The array of methods belonging to the class.
     * @param c The Class of the class.
     */
    private static void doYuml(Member[] constructors, Member[] fields, Member[] methods, Class<?> c) {
        StringBuilder result = new StringBuilder("[");
        result.append(c.getSimpleName());
        result.append("|");
        for (Member mem : fields) {
            int mods = mem.getModifiers();
            if (!Modifier.isPublic(mods)) {
                continue;
            }
            result.append(trimMember(mem.getName(), c.getName()));
            result.append(";");
        }
        result.append("|");
        for (Member mem : constructors) {
            int mods = mem.getModifiers();
            if (!Modifier.isPublic(mods)) {
                continue;
            }
            String toAppend = trimConstructor(mem.toString(), c.getName()).replaceAll("\\[\\]", "#");
            toAppend = toAppend.replaceAll(",", " ");
            result.append(toAppend);
            result.append(";");
        }
        for (Member mem : methods) {
            int mods = mem.getModifiers();
            if (!Modifier.isPublic(mods)) {
                continue;
            }
            if (Modifier.isStatic(mods)) {
                result.append("_");
            }
            result.append(mem.getName());
            result.append("(");
            Method m = (Method)mem;
            Class<?>[] parameterTypes = m.getParameterTypes();
            for (Class<?> type : parameterTypes) {
                result.append(type.getSimpleName().replaceAll("\\[\\]", "#"));
                result.append(" ");
            }
            result.append(");");
        }
        result.append("]");
        System.out.println(result);
    }

}

