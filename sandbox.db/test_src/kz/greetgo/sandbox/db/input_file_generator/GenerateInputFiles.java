package kz.greetgo.sandbox.db.input_file_generator;

import kz.greetgo.util.RND;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class GenerateInputFiles {
  public static void main(String[] args) throws Exception {
    new GenerateInputFiles().execute();
  }

  private static final String ENG = "abcdefghijklmnopqrstuvwxyz";
  private static final String DEG = "0123456789";

  private static final char[] ALL = (ENG + ENG.toUpperCase() + DEG).toCharArray();
  private static final char[] BIG = (ENG.toUpperCase() + DEG).toCharArray();

  private static final Random random = new Random();

  @SuppressWarnings("SameParameterValue")
  private static String rndStr(int len) {
    final int allLength = ALL.length;
    final char ret[] = new char[len];
    for (int i = 0; i < len; i++) {
      ret[i] = ALL[random.nextInt(allLength)];
    }
    return new String(ret);
  }

  private static String rndClientId() {
    char cc[] = new char[8];
    cc[0] = DEG.charAt(random.nextInt(DEG.length()));
    for (int i = 1; i < cc.length; i++) {
      cc[i] = BIG[random.nextInt(BIG.length)];
    }

    return String.valueOf(cc, 0, 1) + '-' +
      String.valueOf(cc, 1, 3) + '-' +
      String.valueOf(cc, 4, 2) + '-' +
      String.valueOf(cc, 6, 2) + '-' +
      rndStr(10);
  }

  enum ErrorType {
    NO_SURNAME(1),
    EMPTY_SURNAME(1),
    NO_NAME(1),
    EMPTY_NAME(1),
    NO_CHARM(1),

    BIRTH_DATE_TOO_OLD(1),
    BIRTH_DATE_TOO_YOUNG(1),
    BIRTH_DATE_NO(1),
    BIRTH_DATE_LEFT(1),

    //
    ;

    public final float priority;

    ErrorType(float priority) {
      this.priority = priority;
    }

    public float getPriority() {
      return priority;
    }
  }

  enum RowType {
    NEW_FOR_PAIR(100),
    EXISTS(100),
    @SuppressWarnings("unused")
    NEW_ALONE(10),
    ERROR(1),

    //
    ;
    final float priority;

    RowType(float priority) {
      this.priority = priority;
    }

    public float getPriority() {
      return priority;
    }
  }

  final List<String> idList = new ArrayList<>();

  private static class SelectorRnd<T> {
    private Object[] values;

    public SelectorRnd(T[] values, Function<T, Float> priority) {

      int len = values.length * 10;
      this.values = new Object[len];
      float total = 0;
      for (T value : values) {
        total += priority.apply(value);
      }

      outer_for:
      for (int i = 0; i < len; i++) {

        float I = (float) i / (float) len * total;

        float current = 0;
        for (T value : values) {
          float p = priority.apply(value);
          if (current + p > I) {
            this.values[i] = value;
            continue outer_for;
          }
          current += p;
        }

        throw new RuntimeException("Left error");
      }

    }

    public T next() {
      //noinspection unchecked
      return (T) values[random.nextInt(values.length)];
    }

    public void showInfo() {
      System.out.println("Selector rnd: values.length = " + values.length);
      int i = 0;
      for (Object value : values) {
        System.out.println("  " + i++ + " : " + value);
      }
    }
  }

  final SelectorRnd<RowType> rowTypeRnd = new SelectorRnd<>(RowType.values(), RowType::getPriority);
  final SelectorRnd<ErrorType> errorTypeRnd = new SelectorRnd<>(ErrorType.values(), ErrorType::getPriority);

  private static String spaces(int count) {
    char ret[] = new char[count];
    for (int i = 0; i < count; i++) ret[i] = ' ';
    return new String(ret);
  }

  PrintStream ciaPrinter = null;
  String ciaFileName;
  File ciaFile;

  final AtomicBoolean clearCiaPrinter = new AtomicBoolean(false);

  static final String DIR = "build/out_files";

  private void execute() throws Exception {

    rowTypeRnd.showInfo();
    errorTypeRnd.showInfo();

    final File workingFile = new File(DIR + "/__working__");
    final File newCiaFile = new File(DIR + "/__new_cia_file__");

    workingFile.getParentFile().mkdirs();
    workingFile.createNewFile();

    newCiaFile.getParentFile().mkdirs();
    newCiaFile.createNewFile();

    final AtomicBoolean working = new AtomicBoolean(true);
    final AtomicBoolean showInfo = new AtomicBoolean(false);

    new Thread(() -> {

      while (workingFile.exists() && working.get()) {

        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          break;
        }

        if (!newCiaFile.exists()) {
          clearCiaPrinter.set(true);

          newCiaFile.getParentFile().mkdirs();
          try {
            newCiaFile.createNewFile();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }

      }

      working.set(false);

    }).start();

    new Thread(() -> {

      while (working.get()) {

        try {
          Thread.sleep(700);
        } catch (InterruptedException e) {
          break;
        }

        showInfo.set(true);
      }

    }).start();

    int i = 1;
    for (RowType rowType : RowType.values()) {
      printClient(i++, rowType);
    }

    int fileIndex = 1;

    for (; currentCiaFileRecords <= 1_000_000 && working.get(); i++) {

      printClient(i, rowTypeRnd.next());
      currentCiaFileRecords++;

      if (fileIndex == 1 && currentCiaFileRecords >= 300) {
        fileIndex++;
        clearCiaPrinter.set(true);
      }
      if (fileIndex == 2 && currentCiaFileRecords >= 3000) {
        fileIndex++;
        clearCiaPrinter.set(true);
      }
      if (fileIndex == 3 && currentCiaFileRecords >= 30_000) {
        fileIndex++;
        clearCiaPrinter.set(true);
      }
      if (fileIndex == 4 && currentCiaFileRecords >= 300_000) {
        fileIndex++;
        clearCiaPrinter.set(true);
      }

      if (showInfo.get()) {
        showInfo.set(false);
        System.out.println("Сформировано записей в текущем файле: "
          + currentCiaFileRecords + ", всего записей: " + i);
      }
    }

    finishCiaPrinter();

    working.set(false);

    System.out.println("ИТОГО: Сформировано записей в текущем файле: "
      + currentCiaFileRecords + ", всего записей: " + i);
  }

  int currentCiaFileRecords = 0;

  @SuppressWarnings("unused")
  enum PhoneType {
    homePhone, mobilePhone, workPhone,
  }

  final static char DEG_CHARS[] = DEG.toCharArray();

  static class Phone {
    String number;

    static Phone next() {

      StringBuilder sb = new StringBuilder();
      sb.append('+');
      sb.append('7');
      sb.append('-');
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append('-');
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append('-');
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append('-');
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);
      sb.append(DEG_CHARS[random.nextInt(DEG_CHARS.length)]);

      if (random.nextInt(5) == 0) {
        sb.append(" вн. ").append(RND.intStr(4));
      }

      {
        Phone ret = new Phone();
        ret.number = sb.toString();
        return ret;
      }
    }

    String tag(PhoneType type) {
      if (type == null || number == null) return null;
      return "<" + type.name() + ">" + number + "</" + type.name() + ">";
    }
  }

  static class Address {
    String street, house, flat;

    static Address next() {
      Address ret = new Address();
      ret.street = RND.str(20);
      ret.house = RND.str(2);
      ret.flat = RND.str(2);
      return ret;
    }

    String toTag(String tagName) {
      return "<" + tagName + " street=\"" + street + "\" house=\"" + house + "\" flat=\"" + flat + "\"/>";
    }
  }

  private void finishCiaPrinter() {
    PrintStream pr = ciaPrinter;
    if (pr != null) {
      pr.println("</cia>");
      pr.close();
      ciaPrinter = null;

      File newCiaFile = new File(ciaFileName + "-" + currentCiaFileRecords + ".xml");
      ciaFile.renameTo(newCiaFile);
    }
  }

  private void printClient(int clientIndex, RowType rowType) throws Exception {

    List<String> tags = new ArrayList<>();

    ErrorType errorType = null;

    if (rowType == RowType.ERROR) errorType = errorTypeRnd.next();

    if (errorType != ErrorType.NO_SURNAME) {

      if (errorType == ErrorType.EMPTY_SURNAME) {
        tags.add("    <surname value=\"\"/>");
      } else {
        tags.add("    <surname value=\"" + nextSurname() + "\"/>");
      }

    }

    if (errorType != ErrorType.NO_NAME) {

      if (errorType == ErrorType.EMPTY_NAME) {
        tags.add("    <name value=\"\"/>");
      } else {
        tags.add("    <name value=\"" + nextName() + "\"/>");
      }

    }

    if (errorType != ErrorType.BIRTH_DATE_NO) {

      if (errorType == ErrorType.BIRTH_DATE_LEFT) {

        tags.add("    <birth value=\"" + RND.str(10) + "\"/>");

      } else {

        Date date = null;

        if (errorType == ErrorType.BIRTH_DATE_TOO_OLD) {
          date = RND.dateYears(-10_000, -200);
        } else if (errorType == ErrorType.BIRTH_DATE_TOO_YOUNG) {
          date = RND.dateYears(-10, 0);
        }

        if (date == null) {
          date = RND.dateYears(-100, -18);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tags.add("    <birth value=\"" + sdf.format(date) + "\"/>");

      }

    }

    switch (random.nextInt(5)) {
      case 1:
        break;

      case 2:
        tags.add("    <patronymic value=\"" + spaces(random.nextInt(3)) + "\"/>");
        break;

      default:
        tags.add("    <patronymic value=\"" + nextPatronymic() + "\"/>");
        break;
    }

    tags.add("    <address>\n" +
      "      " + Address.next().toTag("fact") + "\n" +
      "      " + Address.next().toTag("register") + "\n" +
      "    </address>"
    );

    if (errorType != ErrorType.NO_CHARM) {
      tags.add("    <charm value=\"" + nextCharm() + "\"/>");
    }

    tags.add("    <gender value=\"" + (random.nextBoolean() ? "MALE" : "FEMALE") + "\"/>");

    {
      int phoneCount = 2 + random.nextInt(5);
      for (int i = 0; i < phoneCount; i++) {
        tags.add("    " + Phone.next().tag(PhoneType.values()[random.nextInt(PhoneType.values().length)]));
      }
    }

    {
      PrintStream pr = ciaPrinter;

      if (pr == null || clearCiaPrinter.get()) {
        clearCiaPrinter.set(false);

        finishCiaPrinter();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        Date now = new Date();

        ciaFileName = DIR + "/from_cia_" + sdf.format(now) + "-" + ciaFileNo++;
        ciaFile = new File(ciaFileName + ".xml");
        ciaFile.getParentFile().mkdirs();

        pr = new PrintStream(ciaFile, "UTF-8");
        ciaPrinter = pr;
        pr.println("<cia>");
        currentCiaFileRecords = 0;
      }

      Collections.shuffle(tags);

      String clientId = rndClientId();
      if (rowType == RowType.EXISTS && idList.size() > 0) {
        clientId = idList.get(random.nextInt(idList.size()));
      }

      if (rowType == RowType.NEW_FOR_PAIR) {
        idList.add(clientId);
      }

      pr.println("  <client id=\"" + clientId + "\"> <!-- " + clientIndex + " -->");
      tags.forEach(pr::println);
      pr.println("  </client>");
    }
  }

  int ciaFileNo = 1;

  final AtomicReference<List<String>> charmList = new AtomicReference<>(Collections.emptyList());

  {
    moreCharms(89);
  }

  @SuppressWarnings("SameParameterValue")
  void moreCharms(int count) {

    List<String> list = new ArrayList<>(charmList.get());

    for (int i = 0; i < count; i++) {
      list.add(RND.str(10));
    }

    charmList.set(Collections.unmodifiableList(list));
  }

  private String nextCharm() {
    List<String> list = charmList.get();
    return list.get(random.nextInt(list.size()));
  }

  private String nextPatronymic() {
    return RND.str(13);
  }

  private String nextName() {
    return RND.str(10);
  }

  private String nextSurname() {
    return RND.str(10);
  }
}
