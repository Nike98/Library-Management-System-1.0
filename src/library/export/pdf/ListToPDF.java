package library.export.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.datatable.DataTable;
import library.alert.ThrowAlert;

public class ListToPDF {

	public enum Orientation {
		PORTRAIT, 
		LANDSCAPE
	}
	
	public boolean printToPDF(List<List> list, File saveLocation, Orientation orientation) {
		try {
			if (saveLocation == null)
				return false;
			if (!saveLocation.getName().endsWith(".pdf"))
				saveLocation = new File(saveLocation.getAbsolutePath() + ".pdf"); 
			PDDocument doc = new PDDocument();
			PDPage page = new PDPage();
			if (orientation == Orientation.LANDSCAPE)
				page.setMediaBox(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
			else
				page.setMediaBox(new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()));
			
			doc.addPage(page);
			
			float margin = 10;
			float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
			float startNewPage = page.getMediaBox().getHeight() - (2 * margin);
			float start = startNewPage;
			float bottomMargin = 0;
			
			BaseTable dataTable = new BaseTable(start, startNewPage, bottomMargin, 
					tableWidth, margin, doc, page, true, true);
			DataTable tab = new DataTable(dataTable, page);
			tab.addListToTable(list, DataTable.HASHEADER);
			dataTable.draw();
			doc.save(saveLocation);
			doc.close();
			
			return true;
		} catch(IOException ex) {
			ThrowAlert.showErrorMessage("Error occured during PDF export", ex.getMessage());
		}
		return false;
	}
}
