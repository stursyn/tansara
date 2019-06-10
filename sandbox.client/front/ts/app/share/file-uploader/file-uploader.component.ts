import {ChangeDetectorRef, Component, forwardRef, Input, Output, ViewChild} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {
  AbstractControl,
  ControlValueAccessor,
  NG_VALIDATORS,
  NG_VALUE_ACCESSOR,
  ValidationErrors,
  Validator
} from "@angular/forms";
import {maxFileSize, toCamelCaseLang} from "../../app.metadata";
import {FileModel} from "../../../model/FileModel";

@Component({
  selector: 'app-file-uploader',
  template: require('./file-uploader.component.html'),
  styles: [require('./file-uploader.component.css')],
  providers: [
    {provide: NG_VALUE_ACCESSOR, multi: true, useExisting: forwardRef(() => FileUploaderComponent)},
    {provide: NG_VALIDATORS, multi: true, useExisting: forwardRef(() => FileUploaderComponent)}
  ]
})
export class FileUploaderComponent implements ControlValueAccessor, Validator {

  @ViewChild("fileInput") fileInput;
  @ViewChild("file_upload_input") file_upload_input;

  @Input() placeholder;
  @Input() argName;
  @Input() maxFileSize = maxFileSize;
  @Input() canEditTitle = false;

  uploadedFile: FileModel;

  saving: boolean = false;
  isDisabled = false;
  fileSizeExceeded = false;

  toCamelCaseLang = toCamelCaseLang;

  constructor(private cdr: ChangeDetectorRef) {
  }

  valueChanged = (_: any) => {
    this.cdr.detectChanges();
  };

  validate(c: AbstractControl): ValidationErrors | null {
    let valid = true;
    return valid ? null : {valid: false};
  }

  writeValue(obj: FileModel): void {
    this.uploadedFile = FileModel.fromFileModel(obj);
    this.cdr.detectChanges();
  }

  registerOnChange(fn: any): void {
    this.valueChanged = fn;
  }

  registerOnTouched(fn: any): void {
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  private emmitValueChanged() {
    this.valueChanged(this.uploadedFile);
  }

  public removeFile() {
    this.fileInput.nativeElement.value = '';
    this.uploadedFile = null;
    this.emmitValueChanged();
  }

  getFileModel(): FileModel {
    return this.uploadedFile;
  }

  fileChanged(event) {
    this.fileSizeExceeded = false;
    if (event.target.files.length == 1) {
      let file: File = event.target.files[0];
      if (file.size > maxFileSize) {
        this.fileSizeExceeded = true;
        alert('Max file size is 10Mb');
        return;
      }

      if (file.size == 0) {
        alert('Min file size is 1 byte');
        return;
      }

      let reader = new FileReader();

      reader.onloadend = () => {
        let contractFile = FileModel.fromFile(file);
        contractFile.src = reader.result ? (reader.result.split(",")[1]) : ("");
        this.uploadedFile = contractFile;

        this.emmitValueChanged();
      };

      reader.readAsDataURL(file);
    }
  }

}
