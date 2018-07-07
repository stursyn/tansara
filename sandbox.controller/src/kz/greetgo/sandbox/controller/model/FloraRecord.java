package kz.greetgo.sandbox.controller.model;

public class FloraRecord {
  public Integer id;
  public String num;
  public String catalog;
  public String collectedBy;
  public String typeTitle;
  public String familyTitle;
  public String useReason;
  public String floraNum;
  public String collectPlace;
  public String collectCoordinate;
  public String collectAltitude;
  public String collectDate;
  public String floraWeight;
  public String behaviorPercent;

  @Override
  public String toString() {
    return "FloraRecord{" +
        "id='" + id + '\'' +
        ", num='" + num + '\'' +
        ", catalog='" + catalog + '\'' +
        ", collectedBy='" + collectedBy + '\'' +
        ", typeTitle='" + typeTitle + '\'' +
        ", familyTitle='" + familyTitle + '\'' +
        ", useReason='" + useReason + '\'' +
        ", floraNum='" + floraNum + '\'' +
        ", collectPlace='" + collectPlace + '\'' +
        ", collectCoordinate='" + collectCoordinate + '\'' +
        ", collectAltitude='" + collectAltitude + '\'' +
        ", collectDate='" + collectDate + '\'' +
        ", floraWeight='" + floraWeight + '\'' +
        ", behaviorPercent='" + behaviorPercent + '\'' +
        '}';
  }
}
