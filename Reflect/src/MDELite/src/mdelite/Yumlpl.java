/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 */
public class Yumlpl extends GProlog {

    @Override
    public String fileType() {
        return ".yuml.pl";
    }

    @Override
    public String partialFileType() {
        return ".yuml";
    }

    public Yumlpl(String filename) {
        super(filename);
    }

    public Yumlpl(String filename, String[] array) {
        super(filename, array);
    }

    // the following are transformation
    public SDB toSDB() {
        return toSDB("");
    }

    public SDB toSDB(String extra) {
        String[] array = {this.fullName, Common.homePath + "libpl/yuml2sdb.pl", Common.homePath + "libpl/sdb.schema.pl", Common.homePath + "libpl/print.pl", Common.homePath + "libpl/sdb.run.pl"};
        SDB tmp = new SDB("tmp", array);
        SDB result = new SDB(filename + extra);
        tmp.executeProlog(result);
        result.conform();
        tmp.delete();
        return result;
    }

    public Yuml toYuml(String extra) {
        Yuml result = new Yuml(filename + extra);
        invokeVm2t(result, Common.homePath + "libvm/yumlpl2yuml.vm");
        return result;
    }

    public Yuml toYuml() {
        return toYuml("");
    }

    @Override
    public void conform() {
        conform(Common.homePath + "libpl/conform.pl");
    }
    
    public void conform(String filename) {
        String[] list = {fullName, filename, Common.homePath + "libpl/yuml.conform.pl"};
        SDB tmpconform = new SDB("tmpconform", list);
        tmpconform.executeProlog();
        tmpconform.delete();
    }
}
