import {Component, Inject, ViewChild} from "@angular/core";
import {MatDialogRef} from "@angular/material";
import {FileUploaderComponent} from "../../share/file-uploader/file-uploader.component";
import {HttpService} from "../../HttpService";

export class CollectionRecord{
  public collection:string;
  public measure:string;

  public static from(collection: string, measure: string): CollectionRecord {
    let d = new CollectionRecord();
    d.collection = collection;
    d.measure = measure;
    return d;
  }
}


export class UseElement {
  public num: number;
}

@Component({
  selector: 'import-data-dialog-window',
  template: require('./import_data_dialog.window.html'),
  styles: [require('./import_data_dialog.window.css')],
})
export class ImportDataDialogWindow {
  public errorMessage:string;
  @ViewChild(FileUploaderComponent) fileUploaderComponent;

  constructor(public dialogRef: MatDialogRef<ImportDataDialogWindow>,
              private httpService: HttpService) {
  }

  importFile() {
    this.errorMessage = undefined;
    let fileModel = this.fileUploaderComponent.getFileModel();
    if(fileModel) {
      this.httpService.post("/flora/import_flora_data",{toSave:JSON.stringify(fileModel)})
        .toPromise().then(
            result=>{
              this.dialogRef.close();
            },
            error=>{
              console.log(error);
              this.errorMessage = error._body;
            }
        );
    } else {
      this.errorMessage = "Пожалуйста выберите файл";
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
