import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AsyncSubject, BehaviorSubject, Observable} from "rxjs";
import {CmWorkspace} from "../model/cm-workspace";
import {NzMessageService} from "ng-zorro-antd/message";
import {BaseService} from "./base-service";

@Injectable({
  providedIn: 'root'
})
export class CmWorkspaceService extends BaseService {
  cache: CmWorkspace[] = null;
  cacheSubject: BehaviorSubject<CmWorkspace[]> = new BehaviorSubject([]);

  constructor(http: HttpClient,
              message: NzMessageService,
  ) {
    super('workspace', http, message);
  }

  queryAllWorkspaces(): Observable<CmWorkspace[]> {
    this.sendRequest<CmWorkspace>('', 'query-all', {})
      .subscribe(workspaces => {
        //@ts-ignore
        this.cache = workspaces;
        this.cacheSubject.next(this.cache);
      });
    return this.cacheSubject;
  }

  newWorkspaces(workspace: CmWorkspace): Observable<CmWorkspace> {
    const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();

    this.sendRequest<CmWorkspace>('', 'new-workspace', workspace)
      .subscribe(newWorkspace => {
        this.cache.push(newWorkspace);
        subject.next(newWorkspace);
        subject.complete();

        this.cacheSubject.next(this.cache);
      });
    return subject;
  }

  updateWorkspaces(workspace: CmWorkspace): Observable<CmWorkspace> {
    const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();
    this.sendRequest<CmWorkspace>(workspace.id, 'update-single', workspace)
      .subscribe(ws => {
        subject.next(ws);

        const index = this.cache.findIndex(item => item.id == ws.id);
        this.cache[index] = ws;
        subject.complete();

        this.cacheSubject.next(this.cache);
      });
    return subject;
  }

  querySingle(id: string): Observable<CmWorkspace> {
    if (this.cache == null) {
      const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();
      this.queryAllWorkspaces()
        .subscribe(_ => {
          const index = this.cache.findIndex(item => item.id == id);
          subject.next(this.cache[index]);
          subject.complete();
        });
      return subject;
    } else {
      return new Observable(subscriber => {
        const index = this.cache.findIndex(item => item.id == id);
        subscriber.next(this.cache[index]);
      });
    }
  }

  deleteSingle(id: string): Observable<CmWorkspace> {
    const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();
    this.sendRequest<CmWorkspace>(id, 'delete-single', {})
      .subscribe(workspace => {
        subject.next(workspace);
        subject.complete();

        this.removeItemWithId(workspace.id);
        this.cacheSubject.next(this.cache);
      });
    return subject;
  }

  private removeItemWithId(id: string) {
    const index = this.cache.findIndex(item => item.id == id);
    if (index >= 0) {
      for (let i = index + 1; i < this.cache.length; i++) {
        this.cache[i - 1] = this.cache[i];
      }
      this.cache.pop();
    }
  }
}
