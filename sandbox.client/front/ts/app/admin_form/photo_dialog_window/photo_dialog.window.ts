import {HttpService} from "../../HttpService";
import {Component, Inject} from "@angular/core";
import {MAT_DATE_LOCALE, MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {DomSanitizer} from "@angular/platform-browser";


@Component({
  selector: 'photo-dialog-window',
  template: require('./photo_dialog.window.html'),
  styles: [require('./photo_dialog.window.css')]
})
export class PhotoDialogWindow {
  public header:string = "Фотография";
  public photoBase64:string;

  constructor(
      public sanitizer:DomSanitizer,
      @Inject(MAT_DIALOG_DATA) public data: any,
      private httpService: HttpService) {
    if(data && data.dictId){
      this.httpService.post("/dict/flora-image", {
        code: data.dictId,
        dictType: data.dictType
      }).toPromise().then(
          result => {
            this.photoBase64 = result.json();
          });
    }
  }

  photoURL() {
    return this.photoBase64?this.sanitizer.bypassSecurityTrustResourceUrl(this.photoBase64):this.photoBase64;
  }
}
