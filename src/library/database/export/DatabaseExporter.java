package library.database.export;

import java.io.File;
import java.sql.CallableStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.concurrent.Task;
import library.database.handler.DatabaseHandler;
import library.util.LibraryUtil;

public class DatabaseExporter extends Task<Boolean> {
	
	private final File backupDirectory;

	@Override
	protected Boolean call() throws Exception {
		try {
			createBackup();
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public DatabaseExporter(File backupDirectory) {
		this.backupDirectory = backupDirectory;
	}
	
	private void createBackup() throws Exception {
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
		String backupDir = backupDirectory.getAbsolutePath() + File.separator + LocalDateTime.now().format(dateFormat);
		try (CallableStatement cs = DatabaseHandler.getInstance().getConnection().prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)")) {
			cs.setString(1, backupDir);
			cs.execute();
		}
		File file = new File(backupDir);
		LibraryUtil.openFileWithDesktop(file);
	}

}
