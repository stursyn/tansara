import {Component, OnInit} from "@angular/core";
import {HttpService} from "./HttpService";
import {AuthInfo} from "../model/AuthInfo";

@Component({
  selector: 'root-component',
  template: `
      <div class="mat-app-background basic-container">
          <button mat-button color="primary" (click)="showAdmin?showAdmin=false:showAdmin=true">Изменить тип</button>
          <main-form-component *ngIf="!showAdmin"></main-form-component>
          <admin-form-component *ngIf="showAdmin"></admin-form-component>
      </div>
  `
})
export class RootComponent implements OnInit {
  public showAdmin:boolean = false;
  constructor(private httpService: HttpService) {}

  ngOnInit(): void {

  }
}
