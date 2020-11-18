import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {CmWorkspace} from "../model/cm-workspace";
import {MessageResponse} from "../model/message-response";

@Injectable({
  providedIn: 'root'
})
export class CmWorkspaceService {

  constructor(private http: HttpClient,
              private api: ApiService) {
  }

  url(relativeUrl: string): string {
    return this.api.cmApi(`/workspace/${relativeUrl}`);
  }

  queryAllWorkspaces(): Observable<MessageResponse<CmWorkspace[]>> {
    // @ts-ignore
    return this.http.post(this.url(''),
      "{}",
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': 'query-all'
        })
      });
  }

  newWorkspaces(woarkspace: CmWorkspace): Observable<MessageResponse<CmWorkspace>> {
    // @ts-ignore
    return this.http.post(this.url(''),
      woarkspace,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': 'new-workspace'
        })
      });
  }
}
