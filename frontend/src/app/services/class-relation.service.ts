import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BaseService} from "./base-service";
import {NzMessageService} from "ng-zorro-antd/message";
import {ClassRelationFilter} from "../model/class-relation-filter";
import {RelationResult} from "../model/relation-result";

@Injectable({
  providedIn: 'root'
})
export class ClassRelationService  extends BaseService {

  constructor(http: HttpClient,
              message: NzMessageService,
  ) {
    super('class-relation', http, message);
  }

  queryRelation(fitler: ClassRelationFilter): Observable<RelationResult> {
    return this.sendRequest('', 'query-relation', fitler);
  }
}
