import {HttpClient, HttpHeaders} from "@angular/common/http";
import {NzMessageService} from "ng-zorro-antd/message";
import {AsyncSubject, Observable} from "rxjs";
import {MessageResponse} from "../model/message-response";
import {CmWorkspace} from "../model/cm-workspace";

export abstract class BaseService {
  protected constructor(protected serviceBaseUrl: string,
              protected http: HttpClient,
              protected message: NzMessageService,
  ) {
  }

  url(relativeUrl: string): string {
    return `/${this.serviceBaseUrl}/${relativeUrl}`;
  }

  sendRequest<T>(url: string, cmd: string, params: object): Observable<T> {
    const subject: AsyncSubject<T> = new AsyncSubject();

    // @ts-ignore
    this.http.post(this.url(url),
      params,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'cmd': cmd
        })
      }).subscribe(messageResponse => {
      const mr: MessageResponse<CmWorkspace> = MessageResponse.from(messageResponse);
      if (mr.isSuccess()) {
        // @ts-ignore
        subject.next(mr.data);
        subject.complete();
      } else {
        this.message.create('error', mr.errorMessage());
      }
    });
    return subject;
  }
}
