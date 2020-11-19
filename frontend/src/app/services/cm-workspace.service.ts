import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "./api.service";
import {AsyncSubject, Observable} from "rxjs";
import {CmWorkspace} from "../model/cm-workspace";
import {MessageResponse} from "../model/message-response";
import {NzMessageService} from "ng-zorro-antd/message";

@Injectable({
  providedIn: 'root'
})
export class CmWorkspaceService {
  cache: CmWorkspace[] = null;
  cacheSubject: AsyncSubject<CmWorkspace[]> = new AsyncSubject();

  constructor(private http: HttpClient,
              private api: ApiService,
              private message: NzMessageService,
  ) {
  }

  url(relativeUrl: string): string {
    return this.api.cmApi(`/workspace/${relativeUrl}`);
  }

  queryAllWorkspaces(): Observable<CmWorkspace[]> {
    // @ts-ignore
    this.http.post(this.url(''),
      "{}",
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': 'query-all'
        })
      }).subscribe(messageResponse => {
      const mr: MessageResponse<CmWorkspace[]> = MessageResponse.from(messageResponse);
      if (mr.isSuccess()) {
        this.cache = mr.data;
        this.cacheSubject.next(this.cache);
        this.cacheSubject.complete();
      } else {
        this.message.create('error', mr.errorMessage());
      }
    });
    return this.cacheSubject;
  }

  newWorkspaces(workspace: CmWorkspace): Observable<CmWorkspace> {
    const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();
    // @ts-ignore
    this.http.post(this.url(''),
      workspace,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': 'new-workspace'
        })
      }).subscribe(messageResponse => {
      const mr: MessageResponse<CmWorkspace> = MessageResponse.from(messageResponse);
      if (mr.isSuccess()) {
        this.cache.push(mr.data);
        subject.next(mr.data);
        subject.complete();

        this.cacheSubject.next(this.cache);
        this.cacheSubject.complete();
      } else {
        this.message.create('error', mr.errorMessage());
      }
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
    this.sendRequest(id, 'delete-single', {})
      .subscribe(messageResponse => {
      const mr: MessageResponse<CmWorkspace> = MessageResponse.from(messageResponse);
      if (mr.isSuccess()) {
        this.removeItemWithId(id);

        subject.next(mr.data);
        subject.complete();

        this.cacheSubject.next(this.cache);
        this.cacheSubject.complete();
      } else {
        this.message.create('error', mr.errorMessage());
      }
    });
    return subject;
  }

  sendRequest(url: string, cmd: string, params: object): Observable<MessageResponse<object>> {
    // @ts-ignore
    return this.http.post(this.url(url),
      params,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': cmd
        })
      });
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
