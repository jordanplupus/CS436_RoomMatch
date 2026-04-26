package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadWrite {
	public static Scanner ReadFile(String path) {
		Scanner file = null;
		String workingDir = System.getProperty("user.dir");

		workingDir += path;
		try {
			file = new Scanner(new File(workingDir));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to read from file " + workingDir);
			e.printStackTrace();
		}

		return file;
	}
	
	public static void WriteFile(String path, String save) throws IOException {
		FileWriter writer;
		String workingDir = System.getProperty("user.dir");
		
		workingDir += path;
		writer = new FileWriter(workingDir, true);
		writer.write("\n" + save);
		writer.close();
	}
	
	public static void WriteFile(String path, java.util.List<String> save) throws IOException {
		FileWriter writer;
		String workingDir = System.getProperty("user.dir");
		String txt = "";
		
		for(int i=0; i<save.size(); i++) {
			txt += save.get(i) + (i!=save.size()-1 ? "\n" : "");
		}
		
		workingDir += path;
		writer = new FileWriter(workingDir);
		writer.write(txt);
		writer.close();
	}
	
	public static ArrayList<String> RetrieveFileAsTextArr(String path) {
		Scanner file = null;
		String workingDir = System.getProperty("user.dir");
		ArrayList<String> text = new ArrayList<>();

		workingDir += path;
		try {
			file = new Scanner(new File(workingDir));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to read from file " + workingDir);
			e.printStackTrace();
		}
		
		while(file.hasNextLine()) {
			text.add(file.nextLine());
		}
		file.close();

		return text;
	}
}
