export class CmWorkspace {
  id?: string;
  name?: string;
  includes?: string[];
  excludes?: string[];
  relationTypes?: string[];
  highlight?: string[];
  selected?: string[];
  refCount?: number;
  fileToAnalyze?: string[];
  fileRanges?: string[];
}
