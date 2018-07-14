import {AfterViewInit, Component, EventEmitter, ViewChild} from "@angular/core";
import {HttpService} from "../HttpService";
import {MatDialog, MatPaginator, PageEvent} from "@angular/material";
import {EditDialogWindow} from "./edit_dialog_window/edit_dialog.window";
import {noUndefined} from "@angular/compiler/src/util";

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

  public collectPlace: string;
  public collectCoordinate: string;
  public collectAltitude: string;
  public collectDate: string;
  public floraWeight: string;
  public behaviorPercent: string;
  public collectedBy: string;

  public page: number;
  public pageSize: number;
}

@Component({
  selector: 'main-form-component',
  template: require('./main_form.component.html'),
  styles: [require('./main_form.component.css')],
})
export class MainFormComponent implements AfterViewInit{
  public toFilter: ToFilterElement = new ToFilterElement();
  public displayedColumns: string[] = ['num', 'catalog', 'familyTitle', 'genusTitle', 'typeTitle', 'collectDate', 'action'];
  public dataSource:Array<any> = [];
  public resultsLength:number = 0;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private httpService: HttpService,
              public dialog: MatDialog) {
  }

  ngAfterViewInit(){
    this.find();
  }

  find(){
    console.log(this.paginator);

      this.toFilter.page = this.paginator.pageIndex;
      this.toFilter.pageSize = this.paginator.pageSize;

    this.httpService.post("/flora/list", { toFilter: JSON.stringify(this.toFilter)}).toPromise()
        .then(result => {
            this.dataSource = result.json();
            });
    this.httpService.post("/flora/count", { toFilter: JSON.stringify(this.toFilter)}).toPromise()
        .then(result => {
          this.resultsLength = result.json();
        });
  }

  edit(row){
    console.log(row);
    const dialogRef = this.dialog.open(EditDialogWindow, {
      width: '800px',
      data: {floraId: row?row.id:row}
    });

    dialogRef.afterClosed().subscribe(result => {
      this.find();
    });
  }

  addFlora(){
    this.edit(undefined);
  }
}
