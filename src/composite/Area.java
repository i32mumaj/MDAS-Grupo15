package composite;

import java.util.ArrayList;
import java.util.List;

public class Area implements Element{

    private List<Element> elements;
    private String name;

    public Area(String name){
        this.elements = new ArrayList<Element>();
        this.name = name;
    }

    public Area(List<Element> elements, String name){
        this.elements = elements;
        this.name = name;
    }

    public void addElement(Element element){
        this.elements.add(element);
    }

    public void removeElement(Element element){
        this.elements.remove(element);
    }

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    public double calculateEnergyCosts(){
        double ret_v = 0;

        for(Element element : elements){
            ret_v += element.calculateEnergyCosts();
        }

        return ret_v;

    }

}
