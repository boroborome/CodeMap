import {CheckItem} from "../../model/check-item";
import {CmWorkspace} from "../../model/cm-workspace";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CmWorkspaceService} from "../../services/cm-workspace.service";
import {ActivatedRoute} from "@angular/router";
import {Directive} from "@angular/core";
import {AsyncSubject, Observable} from "rxjs";
import {StringUtil} from "../../utils/string-util";

@Directive()
export abstract class CmWorkspaceSingle {
  id: string;
  name?: string;
  includes?: string;
  highlight?: string;
  excludes?: string;
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

  validateForm!: FormGroup;

  protected fb: FormBuilder;
  protected workSpaceService: CmWorkspaceService;
  protected route: ActivatedRoute;

  protected constructor(fb: FormBuilder,
                        workSpaceService: CmWorkspaceService,
                        route: ActivatedRoute,
              ) {
    this.fb = fb;
    this.workSpaceService = workSpaceService;
    this.route = route;
  }

  initWorkspace(): Observable<CmWorkspace> {
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
    const subject: AsyncSubject<CmWorkspace> = new AsyncSubject();
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.workSpaceService.querySingle(id)
        .subscribe(workspace => {
          this.showWorkspace(workspace);
          subject.next(workspace);
          subject.complete();
        });
    });
    return subject;
  }

  protected check() {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }
  }

  showWorkspace(cw: CmWorkspace) {
    this.id = cw.id;
    this.name = cw.name;

    this.includes = StringUtil.connectStr(cw.includes);
    this.highlight = StringUtil.connectStr(cw.highlight);
    this.excludes = StringUtil.connectStr(cw.excludes);
    this.updateRelationTypes(cw.relationTypes);
    this.selected = StringUtil.connectStr(cw.selected);
    this.refCount = cw.refCount;
    this.fileRanges = StringUtil.connectStr(cw.fileRanges);
  }

  private updateRelationTypes(relationTypes: string[]) {
    const typeSet: Set<string> = new Set(relationTypes);
    for (let index = 0; index < this.relationTypes.length; index++) {
      const item: CheckItem = this.relationTypes[index];
      item.checked = typeSet.has(item.value);
    }
  }

  collectCwFromUi(): CmWorkspace {
    const cw: CmWorkspace = new CmWorkspace();
    cw.id = this.id;
    cw.name = this.name;
    cw.includes = StringUtil.splitStr(this.includes);
    cw.highlight = StringUtil.splitStr(this.highlight);
    cw.excludes = StringUtil.splitStr(this.excludes);
    cw.relationTypes = this.collectRelationTypes();
    cw.selected = StringUtil.splitStr(this.selected);
    cw.refCount = this.refCount;
    cw.fileToAnalyze = StringUtil.splitStr(this.fileToAnalyze);
    cw.fileRanges = StringUtil.splitStr(this.fileRanges);
    return cw;
  }

  private collectRelationTypes(): string[] {
    return this.relationTypes.filter(item => item.checked)
      .map(item => item.value);
  }

}
