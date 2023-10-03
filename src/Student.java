/*
 * Class used to create Student object to help hold data
 */
import java.util.*;
public class Student{

    private int ID;
    private String name;
    private LinearHashTable<String, Integer> totals;
    private LinearHashTable<String, ArrayList<Integer>> categories;

    public Student(){
        this.categories = new LinearHashTable<String, ArrayList<Integer>>(new SimpleStringHasher());
        this.totals = new LinearHashTable<String, Integer>(new SimpleStringHasher());
    }

    @Override
    public String toString() {
        return "Student [ID=" + ID + ", name=" + name + ", categories=" + categories + "]";
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public LinearHashTable<String, ArrayList<Integer>> getCategories() {
        return categories;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategories(LinearHashTable<String, ArrayList<Integer>> categories) {
        this.categories = categories;
    }

    public LinearHashTable<String, Integer> getTotals() {
        return totals;
    }

    public void setTotals(LinearHashTable<String, Integer> totals) {
        this.totals = totals;
    }

    //Method used for details file print out
    public String printOut(ArrayList<String> names){

        String stringPrint = (this.getID() + ", " + this.getName() + ", ");
        for(int j = 0; j < names.size(); j++){
                ArrayList<Integer> hws = this.categories.getElement(names.get(j));
                for(Integer i : hws){
                stringPrint = stringPrint + i.toString() + ", ";
                }
        }
        return stringPrint;
    }

    //Returns a students' total points
    public int studentTotal(ArrayList<String> names){
        Vector<String> totalsKeys = this.totals.getKeys();
        int sum = 0;
        for(int i = 0; i < names.size(); i++){
            sum = sum + this.totals.getElement(names.get(i));
        }
        return sum;
    }
}