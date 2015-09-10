package ru.tomatapps.steps.others;

import java.util.ArrayList;

/**
 * Created by LarryLurex (dmitry.borodin90@gmail.com) on 09.09.2015.
 */
public class TransportItem {
    private String name;
    private boolean checked;
    private int color;

    public TransportItem(String name, int color, boolean checked) {
        this.name = name;
        this.color = color;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<String> getCheckedNames(ArrayList<TransportItem> list){
        ArrayList<String> names = new ArrayList<>();
        for(TransportItem item : list){
            if(item.isChecked())
                names.add(item.getName());
        }
        return names;
    }
    public static ArrayList<String> getNames(ArrayList<TransportItem> list){
        ArrayList<String> names = new ArrayList<>();
        for(TransportItem item : list){
                names.add(item.getName());
        }
        return names;
    }
    public static boolean setTransportChecked(ArrayList<TransportItem> list, String name, boolean isChecked){

        for(TransportItem item : list){
            if(item.getName().equalsIgnoreCase(name)){
                item.setChecked(isChecked);
                return true;
            }
        }
        return false;
    }
    public static int getTransportColor(ArrayList<TransportItem> list, String name){

        for(TransportItem item : list){
            if(item.getName().equalsIgnoreCase(name)){
                return item.getColor();
            }
        }
        return 0;
    }
}
