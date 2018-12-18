
package library.ui.settings;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.gson.Gson;
import library.alert.ThrowAlert;

public class Preferences {

	public static final String CONFIG_FILE = "config.txt";
	
	private int num_DaysWithoutFine;
	private double finePerDay;
	private String Username;
	private String Password;
	
	public Preferences() {
		
		num_DaysWithoutFine = 7;
		finePerDay = 2.0;
		Username = "admin";
		setPassword("admin");
	}
	
	// Getter and Setter methods for all
	public int getnum_DaysWithoutFine() {
		return num_DaysWithoutFine;
	}
	
	public void setnum_DaysWithoutFine(int num_DaysWithoutFine) {
		this.num_DaysWithoutFine = num_DaysWithoutFine;
	}
	
	public double getFinePerDay() {
		return finePerDay;
	}
	
	public void setFinePerDay(double finePerDay) {
		this.finePerDay = finePerDay;
	}
	
	public String getUsername() {
		return Username;
	}
	
	public void setUsername(String Username) {
		this.Username = Username;
	}
	
	public String getPassword() {
		return Password;
	}
	
	public void setPassword(String Password) {
		if (Password.length() < 16)
			this.Password = DigestUtils.shaHex(Password);
		else
			this.Password = Password;
	}
	
	public static void initConfig() {
		
		Writer writer = null;
		try {
			Preferences preference = new Preferences();
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(preference, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				writer.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Preferences getPreferences() {
		Gson gson = new Gson();
		Preferences preference = new Preferences();
		try {
			preference  = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
		} catch (FileNotFoundException e) {
			initConfig();
			e.printStackTrace();
		}
		
		return preference;
	}
	
	public static void setPreferences(Preferences preferences) {
		Writer writer = null;
		try {
			Gson gson = new Gson();
			writer = new FileWriter(CONFIG_FILE);
			gson.toJson(preferences, writer);
			ThrowAlert.showInformationMessage("Success", "Settings Updated Successfully");
		} catch (IOException e) {
			e.printStackTrace();
			ThrowAlert.showErrorMessage(e, "Error Occured", "Can't Save Configuration File");
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
