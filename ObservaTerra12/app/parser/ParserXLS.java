package parser;

import java.io.IOException;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import model.Observation;

public class ParserXLS extends AbstractParser {
	
	public void read() throws IOException  {
	    Workbook w;
	    try {
	      w = Workbook.getWorkbook(this.file);
	      // Get the first sheet
	      Sheet sheet = w.getSheet(0);
	      // Loop over first 10 column and lines
	      
	      int aux = 0;
	      
	      for (int i = 0; i < sheet.getColumns(); i++) {
	    	  Cell cell = sheet.getCell(i, 2);
	    	  //System.out.println(cell.getContents());
	    	  if(cell.getContents().equalsIgnoreCase("country code"))
	    		  aux = i;
	      }
	      
	      System.out.println(aux);

	      /*for (int i = 0; i < sheet.getRows(); i++) {
	        for (int j = 0; j < sheet.getColumns(); j++) {
	          Cell cell = sheet.getCell(j, i);
	            System.out.print(cell.getContents() + " ");

	        }
	        System.out.println();
	      }*/
	    } catch (BiffException e) {
	      e.printStackTrace();
	    }
	  }

	@Override
	public List<Observation> getParsedObservations() {
		// TODO Auto-generated method stub
		return null;
	}
}
