import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

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
  id: string;
  name?: string;
  includes?: string;
  excludes?: string;
  highlight?: string;
  relationTypes: CheckItem[] = [
    { label: 'inherit', value: 'inherit', checked: true },
    { label: 'member', value: 'member', checked: true },
    { label: 'reference', value: 'reference', checked: true },
    { label: 'other', value: 'other', checked: true },
  ];

  selected?: string;
  refCount?: number;
  fileToAnalyze?: string;
  fileRanges?: string;

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
