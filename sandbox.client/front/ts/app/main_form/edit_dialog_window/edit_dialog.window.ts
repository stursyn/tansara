
import {Component, Inject} from "@angular/core";
import {HttpService} from "../../HttpService";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

export class ToSaveElement {
  public num: string;
  public catalog: number;
  public collectedBy: number;
  public typeTitle: string;
  public familyTitle: string;
  public floraNum: string;
  public collectPlace: string;
  public collectCoordinate: string;
  public collectAltitude: string;
  public collectDate: string;
  public floraWeight: string;
  public behaviorPercent: string;
  public useReason: string;
  public page: number;
  public pageSize: number;
}

@Component({
  selector: 'edit-dialog-window',
  template: require('./edit_dialog.window.html'),
  styles: [require('./edit_dialog.window.css')],
})
export class EditDialogWindow {
  public toSave:ToSaveElement = new ToSaveElement();
  constructor(
      public dialogRef: MatDialogRef<EditDialogWindow>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private httpService: HttpService) {
    console.log(data);
    if(data && data.floraId){
      this.detail(data.floraId);
    }
  }

  detail(id){
    this.httpService.post("/flora/detail",{floraId:id})
        .toPromise().then(
            result=>{
              this.toSave = result.json();
            }
    );
  }

  save(){
    this.httpService.post("/flora/save",{toSave:JSON.stringify(this.toSave)})
        .toPromise().then(
            result=>{
              this.dialogRef.close();
            }
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
