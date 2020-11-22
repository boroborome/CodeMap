import { TestBed } from '@angular/core/testing';

import { ClassRelationService } from './class-relation.service';

describe('ClassRelationService', () => {
  let service: ClassRelationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassRelationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
