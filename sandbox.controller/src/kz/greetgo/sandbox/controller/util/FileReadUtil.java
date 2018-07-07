package kz.greetgo.sandbox.controller.util;


import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class FileReadUtil {

  public static void readDocxTable(InputStream inputStream, Function readFunction) throws IOException {
    XWPFDocument document = new XWPFDocument(inputStream);
    document.getTables().forEach( table -> table.getRows().forEach( row -> readFunction.apply(row)));
  }
}
