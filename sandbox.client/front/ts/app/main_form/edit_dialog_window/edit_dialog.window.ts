import {HttpService} from "../../HttpService";
import {DictSimple} from "../main_form.component";
import {Component, Inject} from "@angular/core";
import {MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";

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
  public num: number;
  public catalog: number;
  public usage: string;
  public collectPlace: string;
  public collectCoordinate: string;
  public collectAltitude: string;
  public collectedBy: number;
  public family: string;
  public genus: string;
  public type: string;
  public region: string;
  public collectDate: Date;
  public floraWeight: string;
  public behaviorPercent: string;
  public lifeForm: string;

  public usageList: Array<string> = [];
  public collectionList: Array<CollectionRecord> = [];

  public page: number;
  public pageSize: number;
}

@Component({
  selector: 'edit-dialog-window',
  template: require('./edit_dialog.window.html'),
  styles: [require('./edit_dialog.window.css')]
})
export class EditDialogWindow {
  public toSave:ToSaveElement = new ToSaveElement();
  public collectionList:Array<CollectionRecord> = [];
  public usageList:Array<string> = [];


  public collectionDict: Array<DictSimple> = [];
  public measureDict: Array<DictSimple> = [];
  public regionDict: Array<DictSimple> = [];
  public usageDict: Array<DictSimple> = [];
  public familyDict: Array<DictSimple> = [];
  public genusDict: Array<DictSimple> = [];
  public typeDict: Array<DictSimple> = [];
  public lifeFormDict: Array<DictSimple> = [];
  public defaultCollection:string;
  public defaultMeasure:string;
  public edit:boolean = false;
  public errorMessage:string;
  public defaultUsage:string;

  constructor(
      public dialogRef: MatDialogRef<EditDialogWindow>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private httpService: HttpService) {
    this.loadDict();

    if(data && data.floraId){
      this.edit = true;
      this.detail(data.floraId);
    }
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
        this.toSave.genus = undefined;
        this.toSave.type = undefined;
        break;
      case 'GENUS':
        this.loadTypeDict(event.value);
        this.toSave.type = undefined;
    }
  }

  detail(id){
    this.httpService.post("/flora/detail",{floraId:id})
        .toPromise().then(
            result=>{
              this.toSave = result.json();
              if(this.toSave.collectDate) this.toSave.collectDate = new Date(this.toSave.collectDate);

              this.loadGenusDict(this.toSave.family);
              this.loadTypeDict(this.toSave.genus);

              if(this.toSave.collectionList){
                this.toSave.collectionList.forEach((item, index)=>{
                  if(index == 0) {
                    this.defaultCollection = item.collection;
                    this.defaultMeasure = item.measure;
                  } else {
                    this.collectionList.push(item);
                  }
                });
              }

              if(this.toSave.usageList){
                this.toSave.usageList.forEach((item, index)=>{
                  if(index == 0) {
                    this.defaultUsage = item;
                  } else {
                    this.usageList.push(item);
                  }
                });
              }
            }
    );
  }

  save(){

    this.toSave.usageList = [];
    this.toSave.usageList.push(this.defaultUsage);

    this.usageList.forEach((item)=>{
      this.toSave.usageList.push(item);
    });

    this.toSave.collectionList = [];
    this.toSave.collectionList.push(CollectionRecord.from(this.defaultCollection, this.defaultMeasure));

    this.collectionList.forEach((item)=>{
      this.toSave.collectionList.push(CollectionRecord.from(item.collection, item.measure));
    });

    this.errorMessage = undefined;
    this.toSave.edit = this.edit;
    this.httpService.post("/flora/save",{toSave:JSON.stringify(this.toSave)})
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

  addUsage(){
    this.usageList.push("");
  }

  removeUsage(i){
    this.usageList.splice(i,1);
  }

  addCollection(){
    this.collectionList.push(new CollectionRecord());
  }

  removeCollection(i){
    this.collectionList.splice(i,1);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
