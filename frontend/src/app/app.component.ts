import {Component, OnInit} from '@angular/core';
import {CmWorkspaceService} from "./services/cm-workspace.service";
import {CmWorkspace} from "./model/cm-workspace";
import {MessageResponse} from "./model/message-response";
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  isCollapsed = false;
  workspaces : CmWorkspace[] = [];

  constructor(private workSpaceService: CmWorkspaceService,
              private message: NzMessageService,
  ) {

  }

  ngOnInit(): void {
    this.reloadWorkspaces();
  }

  private reloadWorkspaces() {
    this.workSpaceService.queryAllWorkspaces()
      .subscribe(messageResponse => {
        const mr: MessageResponse<CmWorkspace[]> = MessageResponse.from(messageResponse);
        if (mr.isSuccess()) {
          this.workspaces = mr.data;
        } else {
          this.message.create('error', mr.errorMessage());
        }
      });
  }
}
