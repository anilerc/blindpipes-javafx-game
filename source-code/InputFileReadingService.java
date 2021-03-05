package testing;

//Fatih Said Duran 150119029
//Anılcan Erciyes 150119520


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class InputFileReadingService {	// For scanning given file and returning it as a arraylist of strings.

    public static ArrayList<String> inputFileReader(File file) throws FileNotFoundException {

        ArrayList<String> readInputFile = new ArrayList<String>();	// Arraylist for holding  the strings
        Scanner scanner = new Scanner(file);	//file is put to scanner
        while(scanner.hasNext()) {	//while there is a string to take from scanner: 
            readInputFile.add(scanner.next());	// we add the next string
        }
        scanner.close();	
        return readInputFile;
    }
}
