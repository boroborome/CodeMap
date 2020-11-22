import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {CmWorkspace} from "../../model/cm-workspace";
import {NzModalService} from "ng-zorro-antd/modal";
import {CmWorkspaceSingle} from "../workspace-common/workspace-single.component";

@Component({
  selector: 'app-workspace-settings',
  templateUrl: './workspace-settings.component.html',
  styleUrls: ['./workspace-settings.component.scss']
})
export class WorkspaceSettingsComponent extends CmWorkspaceSingle implements OnInit {

  constructor(fb: FormBuilder,
              workSpaceService: CmWorkspaceService,
              route: ActivatedRoute,
              private message: NzMessageService,
              private router: Router,
              private modal: NzModalService,
  ) {
    super(fb, workSpaceService, route)
  }

  ngOnInit(): void {
    super.initWorkspace();
  }

  submitForm(): void {
    this.check();
    const nw: CmWorkspace = this.collectCwFromUi();
    this.workSpaceService.updateWorkspaces(nw)
      .subscribe(workspace => {
        this.message.create('success', "Success in updating workspace.");
        if (workspace.fileToAnalyze != null && workspace.fileToAnalyze.length > 0) {
          this.router.navigate(["backend-tasks"]);
        }
      });
  }

  deleteWorkspace() {
    this.modal.confirm({
      nzTitle: 'Are you sure delete this workspace?',
      nzContent: `Workspace Name: ${this.name}`,
      nzOkText: 'Yes',
      nzOkType: 'danger',
      nzOnOk: () => this.doDeleteWorkspace(),
      nzCancelText: 'No'
    });
  }

  private doDeleteWorkspace() {
    this.workSpaceService.deleteSingle(this.id)
      .subscribe(workspace => this.router.navigate(["success-result"]));
  }
}
