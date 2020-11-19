import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "./api.service";
import {Observable} from "rxjs";
import {MessageResponse} from "../model/message-response";
import {BackendTask} from "../model/backend-task";

@Injectable({
  providedIn: 'root'
})
export class BackendTaskService {

  constructor(private http: HttpClient,
              private api: ApiService,
              ) { }


  url(relativeUrl: string): string {
    return this.api.cmApi(`/backend-task/${relativeUrl}`);
  }

  queryAllTasks(): Observable<MessageResponse<BackendTask[]>> {
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
}
