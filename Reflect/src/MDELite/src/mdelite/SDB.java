/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mdelite;

/**
 *
 * @author dsb
 */
public class SDB extends GProlog {

    @Override
    public String fileType() {
        return ".sdb.pl";
    }

    @Override
    public String partialFileType() {
        return ".sdb";
    }

    public SDB(String filename) {
        super(filename);
    }

    public SDB(String filename, String[] files) {
        super(filename, files);
    }

    /**
     * **************** methods/transformations of SDB objects ***********
     */
    public Dot toDot() {
        return toDot("");
    }

    public Dot toDot(String extra) {
        Dot result = new Dot(filename + extra);
        invokeVm2t(result, Common.homePath + "libvm/p2dot.vm");
        return result;
    }

    public SDB project(String outputFilename, String plfile) {
        String[] list = {this.fullName, Common.homePath + "libpl/sdb.schema.pl", plfile, Common.homePath + "libpl/print.pl"};
        SDB tmp = new SDB("tmp", list);
        SDB result = new SDB(outputFilename);
        tmp.executeProlog(result);
        tmp.delete();
        // remember: don't test conformance as it won't pass
        // reason: the result of this operatin is some part of an SDB database
        return result;
    }

    public Yumlpl toYumlpl() {
        return toYumlpl("");
    }

    public Yumlpl toYumlpl(String extra) {
        String[] list = {Common.homePath + "libpl/discontiguous.pl", Common.homePath + "libpl/sdb2yuml.pl",
            Common.homePath + "libpl/print.pl",
            Common.homePath + "libpl/yuml.schema.pl",
            Common.homePath + "libpl/yuml.run.pl",
            this.fullName};
        SDB pmerge = new SDB("pmerge", list);
        Yumlpl result = new Yumlpl(filename + extra);
        pmerge.executeProlog(result);
        result.conform();
        pmerge.delete();
        return result;
    }

    public SDB scalePosition(String extra) {
        String[] list = {Common.homePath + "libpl/print.pl", this.fullName, Common.homePath + "libpl/scalePosition.pl"};
        SDB tmp = new SDB("tmp", list);
        SDB result = new SDB(filename + extra);
        tmp.executeProlog(result);
        tmp.delete();
        // don't test conformance as nothing will have changed
        // remember: only tuples of the position table are updated
        // so no logical constraints have been violated
        return result;
    }
    
    public SDB refreshPosition(String newName) {
        Dot d1 = toDot();
        Dot d2 = d1.dot2dot("K");
        SDB onlyPositions = d2.toSDB();
        SDB scaledPositions = onlyPositions.scalePosition("SP");
        SDB noPosition = project(filename+"noPosition",Common.homePath + "libpl/removePosition.pl");
        String[] array = {noPosition.fullName,scaledPositions.fullName};
        SDB patched = new SDB(newName, array); 
        return patched;
    }

    public UMLFpl toUMLFpl() {
        return toUMLFpl("");
    }

    public UMLFpl toUMLFpl(String extra) {
        String[] list = {this.fullName, Common.homePath + "libpl/umlf.schema.pl", Common.homePath + "libpl/sdb2umlf.pl", Common.homePath + "libpl/print.pl", Common.homePath + "libpl/umlf.run.pl"};
        UMLFpl tmp = new UMLFpl("tmp", list);
        UMLFpl result = new UMLFpl(filename + extra);
        tmp.executeProlog(result);
        result.conform();  // make sure that the db conforms to umlfpl schema
        tmp.delete();
        return result;
    }
    
    public Violetpl toVioletpl() {
        return toVioletpl("");
    }

    public Violetpl toVioletpl(String extra) {
        String[] list = {this.fullName, Common.homePath + "libpl/violet.schema.pl", Common.homePath + "libpl/sdb2violet.pl", Common.homePath + "libpl/print.pl", Common.homePath + "libpl/violet.run.pl"};
        Violetpl tmp = new Violetpl("tmp", list);
        Violetpl result = new Violetpl(filename + extra);
        tmp.executeProlog(result);
        result.conform();  // make sure that the db conforms to umlfpl schema
        tmp.delete();
        return result;
    }    
    
    @Override
    public void conform(){
        String[] list = {fullName,Common.homePath + "libpl/conform.pl",Common.homePath + "libpl/sdb.conform.pl"};
        SDB tmpconform = new SDB("tmpconform", list);
        tmpconform.executeProlog();
        tmpconform.delete();     
    }
}
