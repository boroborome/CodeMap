import { TestBed } from '@angular/core/testing';

import { CmWorkspaceService } from './cm-workspace.service';

describe('CmWorkspaceService', () => {
  let service: CmWorkspaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CmWorkspaceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
