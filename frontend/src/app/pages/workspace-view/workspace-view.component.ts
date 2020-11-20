import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {CmWorkspaceUi} from "../../model/cm-workspace-ui";

class CheckItem {
  label: string;
  value: string;
  checked: boolean;
}

@Component({
  selector: 'app-workspace-view',
  templateUrl: './workspace-view.component.html',
  styleUrls: ['./workspace-view.component.scss']
})
export class WorkspaceViewComponent implements OnInit {
  workspaceUi: CmWorkspaceUi = new CmWorkspaceUi();

  isCollapse = false;
  validateForm!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.validateForm = this.fb.group({
      name: [null, [Validators.required]],
      includes: [null, []],
      excludes: [null, []],
      highlight: [null, []],
      relationTypes: [null, []],
      selected: [null, []],
      refCount: [null, []],
      fileToAnalyze: [null, []],
      fileRanges: [null, []],
    });
  }

  resetForm(): void {
    this.validateForm.reset();
  }
  toggleCollapse(): void {
    this.isCollapse = !this.isCollapse;
  }
}
