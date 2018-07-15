package kz.greetgo.sandbox.controller.model;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

public class FloraDetail {
  public Boolean edit = false;
  public Long num;
  public String catalog;
  public String usage;
  public String collectPlace;
  public String collectCoordinate;
  public String collectAltitude;
  public String collectedBy;
  public String family;
  public String genus;
  public String type;
  public String region;
  public Date collectDate;
  public String floraWeight;
  public String behaviorPercent;

  public List<CollectionRecord> collectionList = Lists.newArrayList();


}
