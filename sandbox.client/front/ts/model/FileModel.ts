export class FileModel {
  public id: string;
  public name: string;
  public src;

  public static fromFile(f: File): FileModel {
    if (f == null) return null;
    let ret = new FileModel();
    ret.id = null;
    ret.name = f.name;
    return ret;
  }

  public static fromFileModel(f: FileModel): FileModel {
    if (f == null) return null;
    let ret = new FileModel();
    ret.id = f.id;
    ret.name = f.name;
    ret.src = f.src;

    return ret;
  }

}
