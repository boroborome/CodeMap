import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BackendTask} from "../model/backend-task";
import {BaseService} from "./base-service";
import {NzMessageService} from "ng-zorro-antd/message";

@Injectable({
  providedIn: 'root'
})
export class BackendTaskService extends BaseService {

  constructor(http: HttpClient,
              message: NzMessageService,
  ) {
    super('backend-task', http, message);
  }

  queryAllTasks(): Observable<BackendTask[]> {
    return this.sendRequest('', 'query-all', {});
  }
}
