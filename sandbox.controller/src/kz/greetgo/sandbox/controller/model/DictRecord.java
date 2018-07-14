package kz.greetgo.sandbox.controller.model;

public class DictRecord{
  public String code;
  public String title;
  public String parent;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DictRecord that = (DictRecord) o;

    if (code != null ? !code.equals(that.code) : that.code != null) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    return parent != null ? parent.equals(that.parent) : that.parent == null;
  }

  @Override
  public int hashCode() {
    int result = code != null ? code.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (parent != null ? parent.hashCode() : 0);
    return result;
  }
}
