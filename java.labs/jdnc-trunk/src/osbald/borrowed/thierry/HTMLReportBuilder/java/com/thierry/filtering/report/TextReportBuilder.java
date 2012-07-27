package com.thierry.filtering.report;

import java.awt.Color;

public class TextReportBuilder implements ReportBuilder {
	
	private static final String LINE_SEPARATOR =
            System.getProperty("line.separator");
    
    private static final String CELL_SEPARATOR = "\t";

    private StringBuilder builder = new StringBuilder();
	
	public void addCell(Color bgColor, Color fgColor, String content) {
		builder.append(content);
		builder.append(CELL_SEPARATOR);
	}

	public void addColumnHeader(String header) {
		builder.append(header);
		builder.append(CELL_SEPARATOR);
	}

	public void close() {
		// do nothing
	}

    public void endLine() {
        int lastIndex = builder.length() - 1;
        if (lastIndex >= 0) {
            if (CELL_SEPARATOR.equals(builder.charAt(lastIndex))) {
                builder.replace(lastIndex, lastIndex, LINE_SEPARATOR);
            } else {
                builder.append(LINE_SEPARATOR);
            }
        }
    }

	public void endTable() {
		
	}

	public void endTableHeader() {
        endLine();
	}

	public void open() {
	}

	public void startLine() {
	}

	public void startTable() {
	}

	public void startTableHeader() {
	}

	@Override
	public String toString() {
		return builder.toString();
	}
}
