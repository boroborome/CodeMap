import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {CmWorkspace} from "../../model/cm-workspace";
import {NzModalService} from "ng-zorro-antd/modal";

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
  id: string;
  name?: string;
  includes?: string;
  excludes?: string;
  relationTypes: CheckItem[] = [
    { label: 'inherit', value: 'inherit', checked: true },
    { label: 'member', value: 'member', checked: true },
    { label: 'reference', value: 'reference', checked: true },
    { label: 'other', value: 'other', checked: true },
  ];

  selected?: string;
  refCount?: number;
  fileRanges?: string;

  validateForm!: FormGroup;
  fileList: string;
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
      excludes: [null, []],
      relationTypes: [null, []],
      selected: [null, []],
      refCount: [null, []],
      fileRanges: [null, []],
    });
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.workSpaceService.querySingle(id)
        .subscribe(workspace => this.showWorkspace(workspace));
    });
  }

  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
    const nw: CmWorkspace = this.collectCwFromUi();
    // nw.name = this.newWorkspaceName;
    // nw.fileRanges = this.newWorkspaceFileRange.split("\n");
    // this.workSpaceService.newWorkspaces(nw)
    //   .subscribe(messageResponse => {
    //     const mr: MessageResponse<CmWorkspace[]> = MessageResponse.from(messageResponse);
    //     if (mr.isSuccess()) {
    //       this.router.navigate(["/backend-tasks"]);
    //     } else {
    //       this.message.create('error', mr.errorMessage());
    //     }
    //   });
  }

  private showWorkspace(cw: CmWorkspace) {
    this.id = cw.id;
    this.name = cw.name;

    this.includes = this.connectStr(cw.includes);
    this.excludes = this.connectStr(cw.excludes);
    this.updateRelationTypes(cw.relationTypes);
    this.selected = this.connectStr(cw.selected);
    this.refCount = cw.refCount;
    this.fileRanges = this.connectStr(cw.fileRanges);
  }

  private connectStr(strArray: string[]): string {
    if (strArray == null || strArray.length == 0) {
      return "";
    } else if (strArray.length == 1) {
      return strArray[0];
    } else {
      return strArray.join(",");
    }
  }

  private splitStr(str: string): string[] {
    if (str == null || str.length == 0) {
      return [];
    }
    return str.split(",");
  }

  private updateRelationTypes(relationTypes: string[]) {
    const typeSet: Set<string> = new Set(relationTypes);
    for (let index = 0; index < this.relationTypes.length; index++) {
      const item: CheckItem = this.relationTypes[index];
      item.checked = typeSet.has(item.value);
    }
  }

  private collectCwFromUi(): CmWorkspace {
    const cw: CmWorkspace = new CmWorkspace();
    cw.id = this.id;
    cw.name = this.name;
    cw.includes = this.splitStr(this.includes);
    cw.excludes = this.splitStr(this.excludes);
    cw.relationTypes = this.collectRelationTypes();
    cw.selected = this.splitStr(this.selected);
    cw.refCount = this.refCount;
    cw.fileRanges = this.splitStr(this.fileRanges);
    return cw;
  }

  private collectRelationTypes(): string[] {
    return this.relationTypes.filter(item => item.checked)
      .map(item => item.value);
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
      .subscribe(workspace => null);
  }
}
