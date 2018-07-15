import {HttpService} from "../../HttpService";
import {DictSimple} from "../main_form.component";
import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

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
  selector: 'empty-num-dialog-window',
  template: require('./empty_num_dialog.window.html'),
  styles: [require('./empty_num_dialog.window.css')],
})
export class EmptyNumDialogWindow {
  public displayedColumns: string[] = ['num'];
  public dataSource: Array<any> = [];

  constructor(public dialogRef: MatDialogRef<EmptyNumDialogWindow>,
      private httpService: HttpService) {
    this.loadDict();
  }

  loadDict() {
    this.httpService.post("/flora/empty_nums",{}).toPromise().then(
      result => {
        this.dataSource = result.json();
      });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
