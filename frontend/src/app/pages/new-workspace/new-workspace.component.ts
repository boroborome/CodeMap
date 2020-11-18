import {Component, OnInit} from '@angular/core';
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CmWorkspace} from "../../model/cm-workspace";
import {MessageResponse} from "../../model/message-response";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-workspace',
  templateUrl: './new-workspace.component.html',
  styleUrls: ['./new-workspace.component.scss']
})
export class NewWorkspaceComponent implements OnInit {
  newWorkspaceName: string = "New Workspace";
  newWorkspaceFileRange: string = "";

  validateForm!: FormGroup;
  fileList: string;
  constructor(private fb: FormBuilder,
              private workSpaceService: CmWorkspaceService,
              private message: NzMessageService,
              private router: Router,
  ) {}

  submitForm(): void {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
    const nw: CmWorkspace = {};
    nw.name = this.newWorkspaceName;
    nw.fileRanges = this.newWorkspaceFileRange.split("\n");
    this.workSpaceService.newWorkspaces(nw)
      .subscribe(messageResponse => {
        const mr: MessageResponse<CmWorkspace[]> = MessageResponse.from(messageResponse);
        if (mr.isSuccess()) {
          this.router.navigate(["/workspace/view", nw.name]);
        } else {
          this.message.create('error', mr.errorMessage());
        }
      });
  }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      name: [null, [Validators.required]],
      fileList: [null, [Validators.required]],
    });
  }
}
