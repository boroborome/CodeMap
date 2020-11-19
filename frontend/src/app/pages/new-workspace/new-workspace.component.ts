import {Component, OnInit} from '@angular/core';
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CmWorkspace} from "../../model/cm-workspace";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-workspace',
  templateUrl: './new-workspace.component.html',
  styleUrls: ['./new-workspace.component.scss']
})
export class NewWorkspaceComponent implements OnInit {
  name: string = "New Workspace";
  fileToAnalyze: string = "";

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
    nw.name = this.name;
    nw.fileToAnalyze = this.fileToAnalyze.split("\n");
    this.workSpaceService.newWorkspaces(nw)
      .subscribe(workspace => this.router.navigate(["/backend-tasks"]));
  }

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      name: [null, [Validators.required]],
      fileToAnalyze: [null, [Validators.required]],
    });
  }
}
