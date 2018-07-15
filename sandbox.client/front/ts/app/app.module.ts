import {RootComponent} from "./root.component";
import {MainFormComponent} from "./main_form/main_form.component";
import {HttpService} from "./HttpService";
import {EditDialogWindow} from "./main_form/edit_dialog_window/edit_dialog.window";
import {CdkTableModule} from "@angular/cdk/table";
import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpModule} from "@angular/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {
  MAT_DATE_LOCALE,
  MatButtonModule,
  MatCardModule, MatDatepickerModule, MatDialogModule, MatFormFieldModule, MatIconModule, MatInputModule, MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatSelectModule,
  MatTableModule, MatTooltipModule
} from "@angular/material";
import {e} from "@angular/core/src/render3";
import {EmptyNumDialogWindow} from "./main_form/empty_num_dialog_window/empty_num_dialog.window";

@NgModule({
  imports: [
      CdkTableModule,
      CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpModule,
    FormsModule,
    MatTableModule,
    MatIconModule,
      MatCardModule,
    MatFormFieldModule,
      MatInputModule,
      ReactiveFormsModule,
      MatPaginatorModule,
      MatButtonModule,
    MatDialogModule,
      MatMenuModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTooltipModule

  ],
  declarations: [
    RootComponent, MainFormComponent, EditDialogWindow, EmptyNumDialogWindow
  ],
  bootstrap: [RootComponent],
  providers: [HttpService,
    {provide: MAT_DATE_LOCALE, useValue: 'ru-RU'}],
  entryComponents: [EditDialogWindow, EmptyNumDialogWindow],
})
export class AppModule {
}