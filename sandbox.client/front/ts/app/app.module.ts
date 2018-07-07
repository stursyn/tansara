import {NgModule} from "@angular/core";
import {HttpModule} from "@angular/http";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RootComponent} from "./root.component";
import {MainFormComponent} from "./main_form/main_form.component";
import {HttpService} from "./HttpService";
import {
  MatButtonModule,
  MatCardModule, MatDialogModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule, MatMenuModule,
  MatPaginatorModule,
  MatTableModule
} from "@angular/material";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {BrowserModule} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {CdkTableModule} from "@angular/cdk/table";
import {EditDialogWindow} from "./main_form/edit_dialog_window/edit_dialog.window";

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

  ],
  declarations: [
    RootComponent, MainFormComponent, EditDialogWindow
  ],
  bootstrap: [RootComponent],
  providers: [HttpService],
  entryComponents: [EditDialogWindow],
})
export class AppModule {
}