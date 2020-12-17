package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private int total;
	private List<Report> reports;

	private Diary (){
		total=0;
		reports= new LinkedList<Report>();
	}
	private static class SingeltonHolder{
		private static Diary single_instance = new Diary();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return SingeltonHolder.single_instance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try {
			FileWriter toPrint = new FileWriter(filename);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(this);
			toPrint.write(json);
			toPrint.flush();
			toPrint.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total;
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		total++;
	}
}
