/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 * This defines the domain (category object) of Chrome's UMLFactory
 * 
 */
public class UMLFpl extends GProlog {

    @Override
    public String fileType() {
        return ".umlf.pl";
    }

    @Override
    public String partialFileType() {
        return ".umlf";
    }

    public UMLFpl(String filename) {
        super(filename);
    }

    public UMLFpl(String filename, String[] array) {
        super(filename, array);
    }

    /****************** methods/transformations of ChromeUMLFactory objects ************/
    public SDB toSDB() {
        return toSDB("");
    }

    public SDB toSDB(String extra) {
        String[] array = {this.fullName, Common.homePath + "libpl/umlf2sdb.pl", Common.homePath + "libpl/sdb.schema.pl", Common.homePath + "libpl/print.pl", Common.homePath + "libpl/sdb.run.pl"};
        SDB tmp = new SDB("tmp", array);
        SDB result = new SDB(filename + extra);
        tmp.executeProlog(result);
        result.conform();
        tmp.delete();
        return result;
    }

    public UMLF toUMLF() {
        return toUMLF("g");
    }

    public UMLF toUMLF(String extra) {
        UMLF result = new UMLF(filename + extra);
        invokeVm2t(result, Common.homePath + "libvm/umlf2xml.vm");
        return result;
    }
    
    @Override
    public void conform(){
        conform(Common.homePath + "libpl/conform.pl");
    }
    
    public void conform(String filename){
        String[] list = {fullName,filename,Common.homePath + "libpl/umlf.conform.pl"};
        SDB tmpconform = new SDB("tmpconform", list);
        tmpconform.executeProlog();
        tmpconform.delete();     
    }
    
}
