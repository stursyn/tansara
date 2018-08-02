/**
 * Created by Khamit Mateyev on 5/21/18.
 */
import {NativeDateAdapter} from "@angular/material";
import {MomentDateAdapter} from "@angular/material-moment-adapter";
import {Moment} from "moment";

export class MpDateAdapter extends MomentDateAdapter {

  format(date: Moment, displayFormat: string): string {
    // if (displayFormat == "input") {
    let day = date.date();
    let month = date.month() + 1;
    let year = date.year();

    return ((day / 10 < 1) ? '0' : '') + day + "." + ((month / 10 < 1) ? '0' : '') + month + "." + year;
    // } else {
    //   return date.toDateString();
    // }
  }
}
