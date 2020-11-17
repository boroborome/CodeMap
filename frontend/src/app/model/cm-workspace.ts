export class CmWorkspace {
  id?: string;
  name?: string;
  includes?: string[];
  excludes?: string[];
  relationTypes?: string[];
  selected?: string[];
  refCount?: number;
  fileRanges?: string[];
}
