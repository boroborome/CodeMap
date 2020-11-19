import { TestBed } from '@angular/core/testing';

import { BackendTaskService } from './backend-task.service';

describe('BackendTaskService', () => {
  let service: BackendTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BackendTaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
