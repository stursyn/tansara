package kz.greetgo.sandbox.db.report;

import java.io.Closeable;
import java.io.IOException;

public interface ReportView extends Closeable {

  void start(ReportHeaderData headData);

  void addRow(ReportRow row);

  void finish(ReportFooterData footData) throws IOException;
}
