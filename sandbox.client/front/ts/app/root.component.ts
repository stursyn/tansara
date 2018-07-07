import {Component, OnInit} from "@angular/core";
import {HttpService} from "./HttpService";
import {AuthInfo} from "../model/AuthInfo";

@Component({
  selector: 'root-component',
  template: `
      <div class="mat-app-background basic-container">
          <main-form-component></main-form-component>
      </div>
  `
})
export class RootComponent implements OnInit {

  constructor(private httpService: HttpService) {}

  ngOnInit(): void {
  }
}
