package kz.greetgo.sandbox.controller.model;

public class FileModel {
  public String id;
  public String name;
  public String src;
  public boolean newFile = false;

  public FileModel() {
  }

  public FileModel(String id, String name, String src) {
    this.id = id;
    this.name = name;
    this.src = src;
  }
}
