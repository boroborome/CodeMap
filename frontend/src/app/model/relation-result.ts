import {ClassInfo} from "./class-info";
import {ClassRelation} from "./class-relation";

export class RelationResult {
  nodes: ClassInfo;
  relations: ClassRelation[];
  limitedResult: boolean;
}
