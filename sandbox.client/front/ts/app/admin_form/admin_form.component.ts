import {HttpService} from "../HttpService";
import {FormControl} from "@angular/forms";
import * as _moment from 'moment';
import {Moment} from 'moment';
import {MomentDateAdapter} from "@angular/material-moment-adapter";
import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDialog, MatPaginator} from "@angular/material";
import {DictEditDialogWindow} from "./edit_dialog_window/dict_edit_dialog.window";
import {PhotoDialogWindow} from "./photo_dialog_window/photo_dialog.window";

const moment = _moment;


export class ToFilterElement {
  public name: string;
  public dictType: string;

  public page: number;
  public pageSize: number;
}

export class DictSimple {
  public code: string;
  public title: string;
}

export const MY_FORMATS = {
  parse: {
    dateInput: 'YYYY',
  },
  display: {
    dateInput: 'YYYY',
    monthYearLabel: 'YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'YYYY',
  },
};
@Component({
  selector: 'admin-form-component',
  template: require('./admin_form.component.html'),
  styles: [require('./admin_form.component.css')],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class AdminFormComponent implements AfterViewInit {
  public toFilter: ToFilterElement = new ToFilterElement();
  public displayedColumns: string[] = ['code', 'title', 'parentCode', 'parentTitle', 'dictType', 'action'];
  public dataSource: Array<any> = [];
  public resultsLength: number = 0;
  public dictTypeDict: Array<DictSimple> = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private httpService: HttpService,
              public dialog: MatDialog) {
    this.loadDict();
  }

  ngAfterViewInit() {
    this.find();
  }

  loadDict() {
    this.httpService.get("/dict/dictTypeDict").toPromise().then(
        result => {
          this.dictTypeDict = result.json();
        });
  }

  find() {

    this.toFilter.page = this.paginator.pageIndex;
    this.toFilter.pageSize = this.paginator.pageSize;

    this.httpService.post("/dict/list", {toFilter: JSON.stringify(this.toFilter)}).toPromise()
      .then(result => {
        this.dataSource = result.json();
      });
    this.httpService.post("/dict/count", {toFilter: JSON.stringify(this.toFilter)}).toPromise()
      .then(result => {
        this.resultsLength = result.json();
      });
  }

  edit(row) {
    const dialogRef = this.dialog.open(DictEditDialogWindow, {
      width: '500px',
      data: {
        dictId: row ? row.code : row,
        dictType: row?.dictTypeCode
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.find();
    });
  }

  view(row) {
    const dialogRef = this.dialog.open(PhotoDialogWindow, {
      width: '800px',
      data: {
        dictId: row ? row.code : row,
        dictType: row?.dictTypeCode
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      this.find();
    });
  }

  remove(row) {
    this.httpService.post("/dict/remove",
        {
          code: row.code,
          dictType: row?.dictTypeCode
        }).toPromise()
        .then(result => {
          this.find();
        });
  }

  addFlora() {
    this.edit(undefined);
  }

  downloadReport(){
    let post: string = '';
    let keyValue = {toFilter: JSON.stringify(this.toFilter)};

    if (keyValue) {

      let data = new URLSearchParams();
      let appended = false;
      for (let key in keyValue) {
        let value = keyValue[key];
        if (value) {
          data.append(key, value as string);
          appended = true;
        }
      }

      if (appended) post = '?' + data.toString();
    }

    this.httpService.downloadResource("/dict/download-report", {toFilter: JSON.stringify(this.toFilter)});
  }

  clear(){
    this.toFilter = new ToFilterElement();
  }
}
