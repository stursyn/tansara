import {HttpService} from "../HttpService";
import {EditDialogWindow} from "./edit_dialog_window/edit_dialog.window";
import {FormControl} from "@angular/forms";
import * as _moment from 'moment';
import {Moment} from 'moment';
import {MomentDateAdapter} from "@angular/material-moment-adapter";
import {AfterViewInit, Component, ViewChild} from "@angular/core";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatDialog, MatPaginator} from "@angular/material";
import {EmptyNumDialogWindow} from "./empty_num_dialog_window/empty_num_dialog.window";
import {ImportDataDialogWindow} from "./import_data_dialog_window/import_data_dialog.window";

const moment = _moment;


export class ToFilterElement {
  public num: number;
  public catalog: string;
  public collection: string;
  public measure: string;
  public usage: string;
  public family: string;
  public genus: string;
  public type: string;
  public region: string;
  public lifeForm: string;

  public collectPlace: string;
  public collectCoordinate: string;
  public collectAltitude: string;
  public collectDate: number;
  public floraWeight: string;
  public behaviorPercent: string;
  public collectedBy: string;

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
  selector: 'main-form-component',
  template: require('./main_form.component.html'),
  styles: [require('./main_form.component.css')],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ],
})
export class MainFormComponent implements AfterViewInit {
  public toFilter: ToFilterElement = new ToFilterElement();
  public displayedColumns: string[] = ['num', 'collectionTitle', 'catalog', 'familyTitle', 'typeTitle', 'collectDate', 'action'];
  public dataSource: Array<any> = [];
  public resultsLength: number = 0;
  public collectionDict: Array<DictSimple> = [];
  public measureDict: Array<DictSimple> = [];
  public regionDict: Array<DictSimple> = [];
  public usageDict: Array<DictSimple> = [];
  public familyDict: Array<DictSimple> = [];
  public genusDict: Array<DictSimple> = [];
  public typeDict: Array<DictSimple> = [];
  public lifeFormDict: Array<DictSimple> = [];


  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private httpService: HttpService,
              public dialog: MatDialog) {
  }

  ngAfterViewInit() {
    this.find();
    this.loadDict();
  }

  find() {

    this.toFilter.page = this.paginator.pageIndex;
    this.toFilter.pageSize = this.paginator.pageSize;

    this.httpService.post("/flora/list", {toFilter: JSON.stringify(this.toFilter)}).toPromise()
      .then(result => {
        this.dataSource = result.json();
      });
    this.httpService.post("/flora/count", {toFilter: JSON.stringify(this.toFilter)}).toPromise()
      .then(result => {
        this.resultsLength = result.json();
      });
  }

  loadDict() {
    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'COLLECTION'
      })
    }).toPromise().then(
      result => {
        this.collectionDict = result.json();
      });

    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'MEASURE'
      })
    }).toPromise().then(
      result => {
        this.measureDict = result.json();
      });

    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'REGION'
      })
    }).toPromise().then(
      result => {
        this.regionDict = result.json();
      });

    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'USAGE'
      })
    }).toPromise().then(
      result => {
        this.usageDict = result.json();
      });

    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'FAMILY'
      })
    }).toPromise().then(
      result => {
        this.familyDict = result.json();
      });

    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'LIFE_FORM'
      })
    }).toPromise().then(
        result => {
          this.lifeFormDict = result.json();
        });

    this.loadTypeDict(undefined);
    this.loadGenusDict(undefined);
  }

  loadGenusDict(familyCode) {
    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'GENUS',
        parentCode: familyCode
      })
    }).toPromise().then(
      result => {
        this.genusDict = result.json();
      });
  }

  loadTypeDict(genusCode) {
    this.httpService.post("/flora/dict_simple", {
      toFilter: JSON.stringify({
        dictType: 'TYPE',
        parentCode: genusCode
      })
    }).toPromise().then(
      result => {
        this.typeDict = result.json();
      });
  }

  selectionChange(type, event) {
    console.log(event);
    switch (type) {
      case 'FAMILY':
        this.loadGenusDict(event.value);
        this.typeDict = [];
        this.toFilter.genus = undefined;
        this.toFilter.type = undefined;
        break;
      case 'GENUS':
        this.loadTypeDict(event.value);
        this.toFilter.type = undefined;
    }
  }

  edit(row) {
    console.log(row);
    const dialogRef = this.dialog.open(EditDialogWindow, {
      width: '800px',
      data: {floraId: row ? row.num : row}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.find();
    });
  }

  view(row) {
    console.log(row);
    const dialogRef = this.dialog.open(EditDialogWindow, {
      width: '800px',
      data: {floraId: row ? row.num : row}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.find();
    });
  }

  remove(row) {
    this.httpService.post("/flora/remove", {
      floraId: row ? row.num : row
    }).toPromise().then(
        result => {
          this.find();
        });
  }

  date = new FormControl(moment());

  chosenYearHandler(normalizedYear: Moment, dp) {
    this.toFilter.collectDate = normalizedYear.year();
    let x = this.date.value;
    x.year(normalizedYear.year());
    this.date.setValue(x);
    dp.close();
  }

  addFlora() {
    this.edit(undefined);
  }

  clear(){
    this.toFilter = new ToFilterElement();

    this.loadTypeDict(undefined);
    this.loadGenusDict(undefined);
  }

  importData(){
    const dialogRef = this.dialog.open(ImportDataDialogWindow, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.clear();
      this.find();
    });
  }

  getEmptyNums(){
    const dialogRef = this.dialog.open(EmptyNumDialogWindow, {
      width: '250px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.clear();
      this.find();
    });
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

    // window.open((<any>window).urlPrefix+'/flora/download-report' + post);
    this.httpService.downloadResource("/flora/download-report", {toFilter: JSON.stringify(this.toFilter)});
  }
}
