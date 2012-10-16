/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 */
public class Violetpl extends GProlog {

    @Override
    public String fileType() {
        return ".violet.pl";
    }

    @Override
    public String partialFileType() {
        return ".violet";
    }

    public Violetpl(String filename) {
        super(filename);
    }

    public Violetpl(String filename, String[] array) {
        super(filename, array);
    }

    // the following are transformation
    public SDB toSDB() {
        return toSDB("");
    }

    public SDB toSDB(String extra) {
        String[] array = {this.fullName, Common.homePath + "libpl/violet2sdb.pl", Common.homePath + "libpl/sdb.schema.pl", Common.homePath + "libpl/print.pl", Common.homePath + "libpl/sdb.run.pl"};
        SDB tmp = new SDB("tmp", array);
        SDB result = new SDB(filename + extra);
        tmp.executeProlog(result);
        tmp.delete();
        return result;
    }

    public Violet toViolet(String extra) {
        Violet result = new Violet(filename + extra);
        invokeVm2t(result, Common.homePath + "libvm/violetXml.vm");
        return result;
    }

    public Violet toViolet() {
        return toViolet("");
    }
    
    @Override
    public void conform(){
        conform(Common.homePath + "libpl/conform.pl");  
    }
    
    public void conform(String conformFile) {
        String[] list = {fullName,conformFile,Common.homePath + "libpl/violet.conform.pl"};
        SDB tmpconform = new SDB("tmpconform", list);
        tmpconform.executeProlog();
        tmpconform.delete();     
    }
}
