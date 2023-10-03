import java.io.*;
import java.util.*;

/*
 * For the File print out to work, file types must be fed consecutively
 * For example, "/homework_1.csv /homework_2.csv /quizzes_1.csv /quizzes_2.csv"
 */
public class PA1_main {
    
    public static void main(String args[]){
        
        ArrayList<String> allKeys = new ArrayList<String>();
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<ArrayList> totals = new ArrayList<ArrayList>();

        //Lists that will hold all of our students and all of our maxGrades
        ArrayList<ArrayList> keysL = new ArrayList<ArrayList>();
        LinearHashTable<String, Student> students = new LinearHashTable<>(new SimpleStringHasher());
        LinearHashTable <String, String> maxGrades = new LinearHashTable<>(new SimpleStringHasher());

        //Lists to hold things for our Summary print
        

        for(String fileName : args){
            File file = new File(fileName);
           // System.out.println(file.getName());

            //Used to get the number of file
            Scanner name = new Scanner(file.getName());
            String nameS = name.next();
            StringTokenizer str = new StringTokenizer(nameS, "_", false);
            String nameHalf = str.nextToken();
            String numberHalf = str.nextToken();
            //System.out.println(nameHalf); //test

            //Adds the type of file into a list
            if(!names.contains(nameHalf)){
                names.add(nameHalf);
            }

            organizeFile(file, allKeys, maxGrades, students, keysL, nameHalf);
        }

        //Simulates printing to file
        printDetailReport(students, allKeys, maxGrades, names);
        printSummary(students, names, maxGrades);
    }

    //Method for taking a file and adding on to the main keys and maxGrade structures, and creates student data
    public static void organizeFile(File file, ArrayList<String> ak, LinearHashTable maxGrades, LinearHashTable students, ArrayList <ArrayList> keysL, String category){
        try {

            //Gets the first line and breaks up the labels which will be used as keys
            Scanner scan = new Scanner(new FileReader(file));
            String keysS = scan.nextLine();
            StringTokenizer st = new StringTokenizer(keysS, ",", false);
            
            //Creates a list of keys while also adding to big list of all keys
            ArrayList<String> keys = new ArrayList<String>();            
            while(st.hasMoreTokens()){
                String token = st.nextToken();
                keys.add(token);
               // keysLL.add(token);
                if(!ak.contains(token) && !token.equals("Total")){
                    ak.add(token); //Keeps track of all total keys
                }
            }

            //Creates a HashTable for each given file of max grades
            String gradeTotals = scan.nextLine();
            StringTokenizer str = new StringTokenizer(gradeTotals, ",", false);

            //The reason why I start at one is because the second line starts with ",". This little
            //change makes sure the keys and values stay secure
            maxGrades.addElement("ID", ""); //Werid Token bug
            int Counter = 1;

            //Adds onto maxGrades hashtable that can be used for details print out
            while(str.hasMoreTokens()){
                String token = str.nextToken();
                maxGrades.addElement(keys.get(Counter), token);
                Counter++;
            }
            
            //Main loop where student data is created and stored
            while(scan.hasNextLine()){
                String studentLine = scan.nextLine();
                StringTokenizer breaker = new StringTokenizer(studentLine, ",", false);

                int currentKey = 0;//Helper key, make this a switch statement later
                ArrayList<Integer> assignments = new ArrayList<Integer>();
                Student currentStudent;

                //A new student created each loop
                //Creates a new student for a given line
                String helper = breaker.nextToken(); //helper eqauls ID
                if(!students.containsElement(helper)){
                    students.addElement(helper, new Student());
                }
                currentStudent = (Student) students.getElement(helper);

                //Sets ID
                currentStudent.setID(Integer.parseInt(helper));
                String firstName = breaker.nextToken();
                String lastName = breaker.nextToken();
                currentStudent.setName(firstName + lastName);

               //Creates a totals LinearHashTable for a student and fills it 
                while(breaker.hasMoreTokens()){
                    
                    String current = breaker.nextToken();

                    //Sets Totals
                    if(currentKey == 0){
                        currentStudent.getTotals().addElement(category, Integer.parseInt(current));
                    } 
                    
                    //Adds onto assignments
                    else {
                        assignments.add(Integer.parseInt(current));
                    }
                    currentKey++;

                }
                currentStudent.getCategories().addElement(category, assignments);
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
    }

    //Method that prints the details file
    public static void printDetailReport(LinearHashTable<String, Student> students, ArrayList<String> keys, LinearHashTable grades, ArrayList<String> names){

        Vector<String> studentKeys = students.getKeys();
        try {
            PrintStream o = new PrintStream(new File("details.csv"));
            System.setOut(o);

            //print keys 1st line
            System.out.println(keys);

            //print out second line
            for(int i = 0; i < keys.size(); i++){
             System.out.print(grades.getElement(keys.get(i)) + ", ");
             }
             System.out.println();

            //loop through students and do a detail report print out for each student
            for(int i = 0; i < studentKeys.size(); i++){
                String key = studentKeys.get(i);
                System.out.println(students.getElement(key).printOut(names));
            }
        } catch (Exception FileNotFound) {
            // TODO: handle exception
        }
    }

    //Method that prints summary file
    public static void printSummary(LinearHashTable<String, Student> students, ArrayList<String> names, LinearHashTable<String, String> maxGrades){
        try {
            PrintStream o = new PrintStream(new File("summary.csv"));
            System.setOut(o);

            //Tool for calculating total point possible
            int possiblePoints = 0;

            //First line print out
            String firstLine = ("#ID, Name, Final Grade, ");
            for(int i = 0; i < names.size(); i++){
                firstLine = firstLine + names.get(i) + ", ";
            }
            System.out.println(firstLine);

            //Second line print out
            String secondLine = (", Overall, , ");
            for(int i = 0; i < names.size(); i++){
                secondLine = secondLine + maxGrades.getElement("Total") + ", ";
                possiblePoints = possiblePoints + Integer.parseInt(maxGrades.getElement("Total"));
                maxGrades.removeElement("Total");
            }
            System.out.println(secondLine);

            Vector<String> studentKeys = students.getKeys();
            for(int i = 0; i < studentKeys.size(); i++){
                String key = studentKeys.get(i);
                Student currentStudent = students.getElement(key);
                System.out.print(currentStudent.getID() + ", " + currentStudent.getName() + ", ");

                //Add Final grade to print
                //Also used code from online about turning a number into a percentage
                //and then rounding to two decimal places
                float correct = currentStudent.studentTotal(names);
                float questionNum = possiblePoints;
                double percent = (correct * 100.0f) / questionNum;
                percent = Math.round(percent * 100.0) / 100.0;

                System.out.print(percent + ", ");

                //Goes through totals and prints them out
                for(int j = 0; j < names.size(); j++){
                    System.out.print(currentStudent.getTotals().getElement(names.get(j)) + ", ");
                }

                //Indents people
                System.out.println();
                
            }                

        } catch (Exception e) {
        }
    }
           
    
}
