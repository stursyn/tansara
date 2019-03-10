import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {Headers, Http, Request, RequestOptionsArgs, Response} from "@angular/http";
import {isUndefined} from "util";
import {HttpHeaders, HttpParams, HttpResponse, HttpClient} from "@angular/common/http";

class OptionsBuilder {
  private appendingHeaders: { [key: string]: string }[] = [];
  private appendingParams: { [key: string]: string }[] = [];
  private responseType?: 'json' | 'text' | null;

  public appendHeader(key: string, value: string | null): void {
    if (!isUndefined(value)) this.appendingHeaders.push({key: key, value: value});
  }

  public appendParams(key: string, value: string | null): void {
    if (!isUndefined(value)) this.appendingParams.push({key: key, value: value});
  }

  private get params(): HttpParams {
    let ret: HttpParams = new HttpParams();
    this.appendingParams.forEach(h => {ret = ret.append(h['key'], h['value']);});
    return ret;
  }

  private get headers(): HttpHeaders {
    let ret: HttpHeaders = new HttpHeaders();
    this.appendingHeaders.forEach(h => {ret = ret.append(h['key'], h['value']);});
    return ret;
  }

  private get oldHeaders(): Headers {
    let ret = new Headers();
    this.appendingHeaders.forEach(h => ret.append(h['key'], h['value']));
    return ret;
  }

  public get(): RequestOptionsArgs | undefined {
    if (this.appendingHeaders.length == 0) return undefined;
    return {headers: this.oldHeaders};
  }

  public getBlob(): RequestOptions {
    return {
      observe: 'response',
      headers: this.headers,
      params: this.params,
      reportProgress: false,
      withCredentials: true,
      responseType: 'blob'
    }
  }
}

class RequestOptions {
  observe: 'response';
  headers?: HttpHeaders | {
    [header: string]: string | string[];
  };
  params?: HttpParams | {
    [param: string]: string | string[];
  };
  reportProgress?: boolean;
  withCredentials?: boolean;
  responseType: 'blob'
}

@Injectable()
export class HttpService {

  public pageSize:number = 10;

  constructor(private http: Http, private newHttpClient: HttpClient) {}

  private prefix(): string {
    return (<any>window).urlPrefix;
  }

  public get token(): string | null {
    return sessionStorage.getItem("token");
  }

  public set token(value: string | null) {
    if (value) sessionStorage.setItem("token", value);
    else sessionStorage.removeItem("token")
  }

  public url(urlSuffix: string): string {
    return this.prefix() + urlSuffix;
  }

  public get(urlSuffix: string, keyValue?: { [key: string]: string | number | null }): Observable<Response> {
    let post: string = '';

    if (keyValue) {

      let data = new URLSearchParams();
      let appended = false;
      for (let key in keyValue) {
        let value = keyValue[key];
        if (value) {
          data.append(key, value as string);
          appended = true;
        }
      }

      if (appended) post = '?' + data.toString();
    }

    return this.http.get(this.url(urlSuffix) + post, this.newOptionsBuilder().get());
  }

  private newOptionsBuilder(): OptionsBuilder {
    let ob = new OptionsBuilder();
    if (this.token) ob.appendHeader('Token', this.token);
    return ob;
  }

  public post(urlSuffix: string, keyValue: { [key: string]: string | number | boolean | null }): Observable<Response> {
    let data = new URLSearchParams();
    for (let key in keyValue) {
      let value = keyValue[key];
      if (value) data.append(key, value as string);
    }

    let ob = this.newOptionsBuilder();
    ob.appendHeader('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.url(urlSuffix), data.toString(), ob.get());
  }

  public delete(urlSuffix: string, keyValue?: { [key: string]: string | number | boolean | null }): Observable<Response> {
    let post: string = '';

    if (keyValue) {

      let data = new URLSearchParams();
      let appended = false;
      for (let key in keyValue) {
        let value = keyValue[key];
        if (value) {
          data.append(key, value as string);
          appended = true;
        }
      }

      if (appended) post = '?' + data.toString();
    }

    return this.http.delete(this.url(urlSuffix) + post, this.newOptionsBuilder().get());
  }

  public async downloadResource(urlSuffix: string,
                                 keyValue?: { [key: string]: string | number | boolean | null }): Promise<HttpResponse<Blob>> {
    let ob: OptionsBuilder = this.newOptionsBuilder();

    if (keyValue) {
      for (let key in keyValue) {
        let value = keyValue[key];
        if (!isUndefined(value)) {
          ob.appendParams(key, value as string);
        }
      }
    }

    const response = await this.newHttpClient.get(this.url(urlSuffix), ob.getBlob()).toPromise();
    const url = window.URL.createObjectURL(response.body);
    const filename: string = response.headers.get('Content-Disposition')
        .split(';')[1]
        .split('=')[1]
        .replace(/["]/g, '');

    var link = document.createElement('a');
    link.href = url;
    link.download = decodeURI(filename);
    link.click();

    window.URL.revokeObjectURL(url);
    return response;
  }
}
