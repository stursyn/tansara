import {HttpService} from "../../HttpService";
import {DictSimple} from "../admin_form.component";
import {Component, Inject, ViewChild} from "@angular/core";
import {MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {FileUploaderComponent} from "../../share/file-uploader/file-uploader.component";
import {FileModel} from "../../../model/FileModel";

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


export class ToSaveElement {
  public edit:boolean;
  public code: string;
  public title: string;
  public parentCode: string;
  public dictType: string;
  public fileModel: FileModel;
  public description:string;

  public page: number;
  public pageSize: number;
}

@Component({
  selector: 'dict-edit-dialog-window',
  template: require('./dict_edit_dialog.window.html'),
  styles: [require('./dict_edit_dialog.window.css')]
})
export class DictEditDialogWindow {
  public toSave:ToSaveElement = new ToSaveElement();

  public parentDict: Array<DictSimple> = [];
  public dictTypeDict: Array<DictSimple> = [];
  public edit:boolean = false;
  public errorMessage:string;
  @ViewChild(FileUploaderComponent) fileUploaderComponent;

  constructor(
      public dialogRef: MatDialogRef<DictEditDialogWindow>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private httpService: HttpService) {
    this.loadDict();

    if(data && data.dictId){
      this.edit = true;
      this.detail(data.dictId);
    }
  }

  loadDict() {
    this.httpService.get("/dict/dictTypeDict").toPromise().then(
      result => {
        this.dictTypeDict = result.json();
      });
  }

    loadParentDict(dictType) {
      if(!dictType) return;
      this.httpService.get("/dict/parentDict", {
          toFilter: JSON.stringify({
              dictType: dictType
          })
      }).toPromise().then(
          result => {
              this.parentDict = result.json();
          });
    }


    detail(id){
    this.httpService.post("/dict/detail",{dictId:id})
        .toPromise().then(
            result=>{
              this.toSave = result.json();
              this.loadParentDict(this.toSave.dictType);
            }
    );
  }

    selectionChange(event) {
      this.loadParentDict(event.value);
    }

  save(){
    this.errorMessage = undefined;
    this.toSave.edit = this.edit;
    this.toSave.fileModel = this.fileUploaderComponent.getFileModel();
    this.httpService.post("/dict/save",{toSave:JSON.stringify(this.toSave)})
        .toPromise().then(
            result=>{
              this.dialogRef.close();
            },
      error=>{
              console.log(error);
              this.errorMessage = error._body;
            }
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
