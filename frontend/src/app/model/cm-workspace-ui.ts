import {CheckItem} from "./check-item";
import {CmWorkspace} from "./cm-workspace";

export class CmWorkspaceUi {
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


  showWorkspace(cw: CmWorkspace) {
    this.id = cw.id;
    this.name = cw.name;

    this.includes = this.connectStr(cw.includes);
    this.highlight = this.connectStr(cw.highlight);
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

  collectCwFromUi(): CmWorkspace {
    const cw: CmWorkspace = new CmWorkspace();
    cw.id = this.id;
    cw.name = this.name;
    cw.includes = this.splitStr(this.includes);
    cw.highlight = this.splitStr(this.highlight);
    cw.excludes = this.splitStr(this.excludes);
    cw.relationTypes = this.collectRelationTypes();
    cw.selected = this.splitStr(this.selected);
    cw.refCount = this.refCount;
    cw.fileToAnalyze = this.splitStr(this.fileToAnalyze);
    cw.fileRanges = this.splitStr(this.fileRanges);
    return cw;
  }

  private collectRelationTypes(): string[] {
    return this.relationTypes.filter(item => item.checked)
      .map(item => item.value);
  }

}
