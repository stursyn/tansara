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
  DateAdapter, MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MatButtonModule,
  MatCardModule, MatDatepickerModule, MatDialogModule, MatFormFieldModule, MatIconModule, MatInputModule, MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatSelectModule,
  MatTableModule, MatTooltipModule
} from "@angular/material";
import {EmptyNumDialogWindow} from "./main_form/empty_num_dialog_window/empty_num_dialog.window";
import {MAT_MOMENT_DATE_FORMATS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {MpDateAdapter} from "./MpDateAdapter";
import {AdminFormComponent} from "./admin_form/admin_form.component";
import {DictEditDialogWindow} from "./admin_form/edit_dialog_window/dict_edit_dialog.window";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FileUploaderComponent} from "./share/file-uploader/file-uploader.component";
import {ImportDataDialogWindow} from "./main_form/import_data_dialog_window/import_data_dialog.window";
import {PhotoDialogWindow} from "./admin_form/photo_dialog_window/photo_dialog.window";

@NgModule({
  imports: [
      CdkTableModule,
      CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpModule,
    HttpClientModule,
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
    RootComponent, MainFormComponent,
      EditDialogWindow, EmptyNumDialogWindow,
      AdminFormComponent, DictEditDialogWindow,
      ImportDataDialogWindow, PhotoDialogWindow,
      FileUploaderComponent
  ],
  bootstrap: [RootComponent],
  providers: [HttpService,
    {provide: MAT_DATE_LOCALE, useValue: 'ru-BY'},
    {provide: DateAdapter, useClass: MpDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS}
    ],
  entryComponents: [EditDialogWindow,
      EmptyNumDialogWindow,
      DictEditDialogWindow,
      PhotoDialogWindow,
      ImportDataDialogWindow],
})
export class AppModule {
}
