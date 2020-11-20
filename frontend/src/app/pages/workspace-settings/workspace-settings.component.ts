import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {CmWorkspace} from "../../model/cm-workspace";
import {NzModalService} from "ng-zorro-antd/modal";
import {CmWorkspaceUi} from "../../model/cm-workspace-ui";

class CheckItem {
  label: string;
  value: string;
  checked: boolean;
}
@Component({
  selector: 'app-workspace-settings',
  templateUrl: './workspace-settings.component.html',
  styleUrls: ['./workspace-settings.component.scss']
})
export class WorkspaceSettingsComponent implements OnInit {
  workspaceUi: CmWorkspaceUi = new CmWorkspaceUi();

  validateForm!: FormGroup;
  constructor(private fb: FormBuilder,
              private workSpaceService: CmWorkspaceService,
              private message: NzMessageService,
              private router: Router,
              private route: ActivatedRoute,
              private modal: NzModalService,
  ) {}

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      name: [null, [Validators.required]],
      includes: [null, []],
      highlight: [null, []],
      excludes: [null, []],
      relationTypes: [null, []],
      selected: [null, []],
      refCount: [null, []],
      fileToAnalyze: [null, []],
      fileRanges: [null, []],
    });
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.workSpaceService.querySingle(id)
        .subscribe(workspace => this.workspaceUi.showWorkspace(workspace));
    });
  }

  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
    const nw: CmWorkspace = this.workspaceUi.collectCwFromUi();
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
      nzContent: `Workspace Name: ${this.workspaceUi.name}`,
      nzOkText: 'Yes',
      nzOkType: 'danger',
      nzOnOk: () => this.doDeleteWorkspace(),
      nzCancelText: 'No'
    });
  }

  private doDeleteWorkspace() {
    this.workSpaceService.deleteSingle(this.workspaceUi.id)
      .subscribe(workspace => this.router.navigate(["success-result"]));
  }
}
