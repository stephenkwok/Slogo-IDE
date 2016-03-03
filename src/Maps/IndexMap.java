package Maps;

import Observables.MapObservable;
import View.Xml.MapFromXML;

public abstract class IndexMap {
    protected IndexMap() throws Exception{
        addElements("default");
    }
    
    public void addElements(String type) throws Exception{
        newMap();
        if(type.equals("default")){
            defaultElements();
        }else{
            getElementsFromXML(type);
        }
    }
    
    protected abstract void newMap ();

    protected void getElementsFromXML(String type) throws Exception{
        MapFromXML xMap = new MapFromXML(this.getIndexMap());
        xMap.getElements(type);
    }

    protected abstract void defaultElements () throws Exception;

    public abstract MapObservable<Integer, String> getIndexMap();
    public abstract void setAtIndex(int index, String value) throws Exception;
    
    public abstract String get(int key);

}